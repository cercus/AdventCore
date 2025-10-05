package fr.cercusmc.adventcore.utils.commands;

import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * This interface represents a sub-command within a command.
 */
public interface SubCommand {

    /**
     * Returns the name of the sub-command.
     *
     * @return The name of the sub-command.
     */
    String getName();

    /**
     * Returns the description of the sub-command.
     * @return The description of the sub-command.
     */
    String getDescription();

    /**
     * Returns the usage message of the sub-command.
     *
     * @return The usage message of the sub-command.
     */
    String getUsage();

    /**
     * Returns the permission required to perform the sub-command.
     *
     * @return The permission required to perform the sub-command.
     */
    String getPermission();

    /**
     * Checks if the given player has the required permission to perform the sub-command.
     *
     * @param sender The player to check.
     * @return True if the player has the required permission, false otherwise.
     * */
    boolean canPerform(CommandSender sender);

    /**
     * Executes the sub-command with the given arguments.
     *
     * @param sender The player executing the sub-command.
     * @param args The arguments passed to the sub-command.
     * */
    void executeSubCommand(CommandSender sender, String[] args);

    /**
     * Returns the aliases of the sub-command.
     *
     * @return The aliases of the sub-command.
     */
    List<String> getAliases();
}
