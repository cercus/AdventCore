package fr.cercusmc.adventcore.utils.messages;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Class to manage logging categories for the plugin.
 *
 * @since 1.0.0
 */
public class LoggingCategory {

    private LoggingCategory() {
        // Do nothing
    }

    /**
     * Get the kernel for logging in console
     * @param prefix The plugin
     * @return The kernel for logging in console
     */
    public static Kernel getKernel(String prefix) {
        return new Kernel(prefix);
    }

    /**
     * Get the kernel for logging in the player's chat
     * @param player The player
     * @return The kernel for logging in the player's chat
     */
    public static UserMessage getUserMessage(Player player) {
        return new UserMessage(player);
    }

    /**
     * Used to format messages in gui, items...
     * @return The UserMessage to format messages in gui, items...
     */
    public static UserMessage getUserMessage() {
        return new UserMessage();
    }
}
