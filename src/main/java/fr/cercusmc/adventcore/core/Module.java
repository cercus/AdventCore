package fr.cercusmc.adventcore.core;

import fr.cercusmc.adventcore.AdventCore;
import fr.cercusmc.adventcore.utils.commands.Command;
import fr.cercusmc.adventcore.utils.files.YamlFile;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a module that can be loaded and unloaded. Any main class of module must extend this class.
 */
public abstract class Module {

    private final List<Listener> listeners;
    private final List<Command> commands;
    private final String moduleName;
    private final File moduleFolder;
    private final Map<String, YamlFile> files;

    protected Module(String moduleName) {
        this.moduleName = moduleName;
        this.listeners = new ArrayList<>();
        this.commands = new ArrayList<>();
        moduleFolder = new File(AdventCore.getInstance().getDataFolder()+"/modules", moduleName);
        this.files = new HashMap<>();

    }

    /**
     * Called when the module is enabled. In this method, you should register any listeners or commands that your module needs.
     */
    public abstract void onEnable();

    public abstract void onDisable();

    /**
     * Returns the name of the module.
     * @return the name of the module.
     */
    public String getName() {
        return moduleName;
    }

    /**
     * Returns all listeners registered for this module.
     * @return all listeners registered for this module.
     */
    public List<Listener> getListeners() {
        return listeners;
    }

    /**
     * Returns all commands registered for this module.
     * @return all commands registered for this module.
     */
    public List<Command> getCommands() {
        return commands;
    }

    /**
     * Returns the version of the module.
     * @return the version of the module.
     */
    public abstract String getVersion();


    /**
     * Add a listener for this module.
     * @param listener The listner to add
     */
    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    /**
     * Remove a listener for this module.
     * @param listener The listener to remove
     */
    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    /**
     * Add a command for this module.
     * @param command The command to add
     */
    public void addCommand(Command command) {
        commands.add(command);
    }

    /**
     * Remove a command for this module.
     * @param command The command to remove
     */
    public void removeCommand(Command command) {
        commands.remove(command);
    }

    /**
     * Returns the folder of the module.
     * @return the folder of the module.
     */
    public File getModuleFolder() {
        return moduleFolder;
    }

    public void addFile(File folder, String file) {
        files.put(file, new YamlFile(folder, getName(), file));
    }

    public Map<String, YamlFile> getFiles() {
        return files;
    }

    public YamlFile getFile(String file) {
        return this.files.get(file);
    }

    public void registerFiles() {}
}
