package fr.cercusmc.adventcore.core;


import fr.cercusmc.adventcore.AdventCore;
import fr.cercusmc.adventcore.utils.commands.Command;
import fr.cercusmc.adventcore.utils.exceptions.RegisterCommandException;
import fr.cercusmc.adventcore.utils.exceptions.RegisterModuleException;
import fr.cercusmc.adventcore.utils.exceptions.UnRegisterCommandException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Map;

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
        AdventCore.getInstance().getModules().remove(module.getName());
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
        if(AdventCore.getInstance().getModules().containsKey(moduleName)) {
            AdventCore.getKernel().createWarning("Module " + moduleName + " is already loaded.");
            return;
        }
        try {
            File moduleFolder = new File(instance.getDataFolder(), "modules");
            File[] moduleFiles = moduleFolder.listFiles((dir, name) -> name.endsWith(moduleName+".jar"));
            if(moduleFiles == null || moduleFiles.length == 0) {
                return;
            }
            URL[] urls = {moduleFiles[0].toURI().toURL()};
            URLClassLoader classLoader = instance.getClassLoader(urls);

            String mainClass = getMainClassFromJar(moduleFiles[0]);

            Class<?> moduleClass = Class.forName(mainClass, true, classLoader);
            Object moduleInstance = moduleClass.getDeclaredConstructor().newInstance();

            if (!(moduleInstance instanceof Module module)) {
                AdventCore.getKernel().createError("Module " + moduleFiles[0].getName().substring(0, moduleFiles[0].getName().lastIndexOf(".jar")) + " does not implement the Module interface.");
                return;
            }

            AdventCore.getInstance().getModules().put(module.getName(), module);

            module.onEnable();

            registerCommands(module.getCommands());
            registerListeners(module.getListeners());

            AdventCore.getKernel().createInfo("Module" + module.getName() + " v"+module.getVersion()+" has been loaded");
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RegisterModuleException("Error loading module " + moduleName, e);
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
