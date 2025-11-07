package fr.cercusmc.adventcore;

import fr.cercusmc.adventcore.commands.RegisterCommand;
import fr.cercusmc.adventcore.commands.UnregisterCommand;
import fr.cercusmc.adventcore.core.Module;
import fr.cercusmc.adventcore.core.ModuleManager;
import fr.cercusmc.adventcore.utils.messages.Kernel;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * The main class for the AdventCore plugin.
 */
public final class AdventCore extends JavaPlugin {


    private static AdventCore instance;
    private static Kernel kernel;
    private ModuleManager moduleManager;
    private Map<String, Module> modules;

    @Override
    public void onEnable() {
        setInstance(this);
        modules = new HashMap<>();
        setKernel(new Kernel("AdventCore"));
        getKernel().createInfo("AdventCore " + getDescription().getVersion()+ "has been enabled!");
        moduleManager = new ModuleManager();
        File moduleFile = new File(getDataFolder(), "modules");
        if (!moduleFile.exists()) {
            moduleFile.mkdirs();
        }
        moduleManager.loadModules();
        moduleManager.registerCommands(Arrays.asList(new RegisterCommand(), new UnregisterCommand()));
    }

    @Override
    public void onDisable() {

        List<String> modulesTmp = new ArrayList<>();
        for(Map.Entry<String, Module> moduleEntry : getModules().entrySet()) {
            moduleManager.unloadModule(moduleEntry.getValue());
            modulesTmp.add(moduleEntry.getKey());
        }
        modulesTmp.forEach(k -> modules.remove(k));
    }

    /**
     * Get the instance of the AdventCore plugin.
     *
     * @return The instance of the plugin.
     */
    public static AdventCore getInstance() {
        return instance;
    }

    public static void setInstance(AdventCore instance) {
        AdventCore.instance = instance;
    }

    public static Kernel getKernel() {
        return kernel;
    }

    public static void setKernel(Kernel kernel) {
        AdventCore.kernel = kernel;
    }

    public URLClassLoader getClassLoader(URL[] urls) {
        return new URLClassLoader(urls, this.getClassLoader());
    }

    public Map<String, Module> getModules() {
        return modules;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }
}
