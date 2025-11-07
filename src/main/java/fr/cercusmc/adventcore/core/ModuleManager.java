package fr.cercusmc.adventcore.core;


import fr.cercusmc.adventcore.AdventCore;
import fr.cercusmc.adventcore.utils.commands.Command;
import fr.cercusmc.adventcore.utils.exceptions.LoadFileException;
import fr.cercusmc.adventcore.utils.exceptions.RegisterCommandException;
import fr.cercusmc.adventcore.utils.exceptions.RegisterModuleException;
import fr.cercusmc.adventcore.utils.exceptions.UnRegisterCommandException;
import fr.cercusmc.adventcore.utils.files.YamlFile;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ModuleManager {

    private final AdventCore instance = AdventCore.getInstance();

    public void loadModules() {

        File moduleFolder = new File(instance.getDataFolder(), "modules");
        File[] moduleFiles = moduleFolder.listFiles((dir, name) -> name.endsWith(".jar"));
        if(moduleFiles == null) {
            return;
        }
        for(File module : moduleFiles) {
                this.loadModule(module.getName().substring(0, module.getName().lastIndexOf(".")));
        }

    }

    public void unloadModule(Module module) {
        //AdventCore.getInstance().getModules().remove(module.getName());
        module.onDisable();
        this.unloadListeners(module.getListeners());
        module.getCommands().forEach(this::unregisterCommand);
        AdventCore.getKernel().createInfo("Module" + module.getName() + " v"+module.getVersion()+" has been unloaded");
    }

    private void unloadListeners(List<Listener> listeners) {
        listeners.forEach(HandlerList::unregisterAll);
    }

    @SuppressWarnings("unchecked")
    private void unregisterCommand(Command command) {
        try {
            Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
            knownCommandsField.setAccessible(true); // NOSONAR
            CommandMap commandMap = getCommandMap();

            if(commandMap!= null) {
                org.bukkit.command.Command commandBukkit = commandMap.getCommand(command.getName());
                if(commandBukkit!= null) {
                    commandBukkit.unregister(commandMap);
                    Method m = commandMap.getClass().getMethod("getKnownCommands");
                    m.setAccessible(true); // NOSONAR
                    Map<String, Command> knownCommands = (Map<String, Command>) m.invoke(commandMap);
                    knownCommands.remove(command.getName());
                    command.getAliases().forEach(knownCommands::remove);
                }

            }

        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new UnRegisterCommandException("Error unregistering command " + command.getName(), e);
        }

    }

    public void loadModule(String moduleName) {
        if (AdventCore.getInstance().getModules().containsKey(moduleName)) return;

        try {
            File moduleFolder = new File(instance.getDataFolder(), "modules");
            File[] moduleFiles = moduleFolder.listFiles((dir, name) -> name.equals(moduleName + ".jar"));
            if (moduleFiles == null || moduleFiles.length == 0) return;

            File jar = moduleFiles[0];
            String mainClass = getMainClassFromJar(jar);
            if (mainClass == null) return;

            Class<?> clazz = Class.forName(mainClass, true, new URLClassLoader(new URL[]{jar.toURI().toURL()}, instance.getClass().getClassLoader()));
            Object moduleObj = clazz.getDeclaredConstructor().newInstance();

            if (!(moduleObj instanceof Module module)) return;


            // 1️⃣ Déclare les fichiers à extraire
            module.registerFiles();

            System.out.println(module.getFiles());
            // 2️⃣ Extrait les fichiers du JAR
            extractResources(jar, module.getFiles());

            // 3️⃣ Active le module
            module.onEnable();
            AdventCore.getInstance().getModules().put(module.getName(), module);



            // 4️⃣ Commandes et listeners
            registerCommands(module.getCommands());
            registerListeners(module.getListeners());

            AdventCore.getKernel().createInfo("Module " + module.getName() + " v" + module.getVersion() + " loaded");

        } catch (Exception e) {
            throw new RegisterModuleException("Error loading module " + moduleName, e);
        }

    }

    private void extractResourceFromJar(File jarFile, String resourcePath, File outputFile) throws IOException {
        try (JarFile jar = new JarFile(jarFile)) {
            JarEntry entry = jar.getJarEntry(resourcePath);
            if (entry == null) {
                AdventCore.getKernel().createWarning("Resource " + resourcePath + " not found in jar.");
                return;
            }
            outputFile.getParentFile().mkdirs();
            try (InputStream in = jar.getInputStream(entry);
                 OutputStream out = new FileOutputStream(outputFile)) {
                in.transferTo(out);
            }
        }
    }

    private void extractResources(File jarFile, Map<String, YamlFile> resources) {
        for (Map.Entry<String, YamlFile> entry : resources.entrySet()) {
            File target = entry.getValue().getFile();
            if (!target.exists()) {
                try {
                    extractResourceFromJar(jarFile, entry.getKey(), target);
                    AdventCore.getKernel().createInfo("Created default " + entry.getKey() + " for " + entry.getValue().getFile());
                } catch (IOException e) {
                    throw new LoadFileException("Failed to extract resource " + entry.getKey(), e);
                }
            }
            entry.getValue().reloadFile();
        }
    }

    public void registerCommands(List<Command> commands) {

        for(Command command : commands) {
            try {
                CommandMap commandMap = getCommandMap();
                if(commandMap != null) {
                    commandMap.register(command.getName(), command);

                }
            } catch(IllegalAccessException | NoSuchFieldException e) {
                throw new RegisterCommandException("Failed to register command : " + command.getName(), e);

            }
        }
    }

    private static CommandMap getCommandMap() throws NoSuchFieldException, IllegalAccessException {
        Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");

        commandMapField.setAccessible(true); // NOSONAR : Necessary to register commands
        return (CommandMap) commandMapField.get(Bukkit.getServer());



    }

    private void registerListeners(List<Listener> listeners) {
        for(Listener l : listeners) {
            AdventCore.getInstance().getServer().getPluginManager().registerEvents(l, AdventCore.getInstance());
        }
    }



    private String getMainClassFromJar(File jarFile) throws IOException {
        try (java.util.jar.JarFile jar = new java.util.jar.JarFile(jarFile)) {
            java.util.jar.Manifest manifest = jar.getManifest();
            if (manifest != null) {
                return manifest.getMainAttributes().getValue("Main-Class");
            }
        }
        return null;
    }

}
