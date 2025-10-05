package fr.cercusmc.adventcore.core;

import fr.cercusmc.adventcore.utils.commands.Command;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a module that can be loaded and unloaded. Any main class of module must extend this class.
 */
public abstract class Module {

    private final List<Listener> listeners;
    private final List<Command> commands;

    protected Module() {

        this.listeners = new ArrayList<>();
        this.commands = new ArrayList<>();
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
    public abstract String getName();

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


}
