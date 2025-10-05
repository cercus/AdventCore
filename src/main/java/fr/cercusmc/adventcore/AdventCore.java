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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
        setKernel(new Kernel(this));
        getKernel().createInfo("AdventCore " + getDescription().getVersion()+ "has been enabled!");
        moduleManager = new ModuleManager();
        File moduleFile = new File(getDataFolder(), "modules");
        if (!moduleFile.exists()) {
            moduleFile.mkdirs();
        }
        moduleManager.loadModules();
        moduleManager.registerCommands(Arrays.asList(new RegisterCommand(this), new UnregisterCommand(this)));
    }

    @Override
    public void onDisable() {
        modules.forEach((k, v) -> moduleManager.unloadModule(v));
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
