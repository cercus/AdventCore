package fr.cercusmc.adventcore.utils.messages;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a user message with a player.
 *
 * @since 1.0.0
 *
 */
public class UserMessage {

    private static final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
    private final Player player;

    /**
     * Creates a new user message with the specified player.
     * @param player The player associated with this message.
     */
    public UserMessage(Player player) {
        this.player = player;
    }

    /**
     * Creates a new user message without the specified player, used to display messages in gui for exemple
     */
    public UserMessage() {
        this(null);
    }

    /**
     * Replaces placeholders in a list of messages with corresponding values from a map.
     *
     * @param messages A list of messages containing placeholders in the format %key%.
     * @param values A map where keys correspond to placeholders in the messages, and values are the replacements.
     * @return A new list of messages with placeholders replaced by their corresponding values.
     */
    private List<String> replacePlaceholder(List<String> messages, Map<String, Object> values) {
        List<String> res = new ArrayList<>();
        for(String i : messages) {
            for(Map.Entry<String, Object> entry : values.entrySet()) {
                i = i.replace("%"+entry.getKey()+"%", entry.getValue().toString());
            }
            res.add(i);
        }

        return res;
    }

    /**
     * Format a message without send to player
     * @param message The message
     * @return The message formatted
     */
    public String formatMessage(String message, Map<String, Object> values) {
        Matcher match = pattern.matcher(message);
        while(match.find()) {
            String color = message.substring(match.start(), match.end());
            message = message.replace(color, ChatColor.of(color) + "");
            match = pattern.matcher(message);
        }
        List<String> msgValues = replacePlaceholder(Collections.singletonList(message), values);

        return ChatColor.translateAlternateColorCodes('&', msgValues.getFirst());
    }

    /**
     * Format a list of message without send to player
     * @param messages The message
     * @return The message formatted
     */
    public List<String> formatMessages(List<String> messages, Map<String, Object> values) {
        List<String>  result = new ArrayList<>();
        messages.forEach((String msg) -> result.add(formatMessage(msg, values)));
        return result;
    }

    /**
     * Send a message to the player
     * @param message The message
     * @param values A map where keys correspond to placeholders in the messages, and values are the replacements.
     */
    public void send(String message, Map<String, Object> values) {
        this.player.sendMessage(formatMessage(message, values));
    }

    /**
     * Send a list of message to the player
     * @param messages The message
     * @param values A map where keys correspond to placeholders in the messages, and values are the replacements.
     */
    public void send(List<String> messages, Map<String, Object> values) {
        messages.forEach((String msg) -> this.player.sendMessage(formatMessage(msg, values)));
    }

    /**
     * Send a message to the player
     * @param message The message
     */
    public void send(String message) {
        this.player.sendMessage(formatMessage(message, new HashMap<>()));
    }

    /**
     * Send a list of message to the player
     * @param messages The message
     */
    public void send(List<String> messages) {
        messages.forEach((String msg) -> this.player.sendMessage(formatMessage(msg, new HashMap<>())));
    }



    public Player getPlayer() {
        return player;
    }
}
