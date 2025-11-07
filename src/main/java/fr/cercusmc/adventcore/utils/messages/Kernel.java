package fr.cercusmc.adventcore.utils.messages;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class responsible for managing plugin messages via console. <br />
 * It provides methods for logging messages to the console with support for placeholders. <br />
 * The format of placeholder is '{[0-9]+}' (exemple {0}) <br />
 *
 * @since 1.0.0
 */
public class Kernel {

    private final String prefix;
    private final Logger logger;


    /**
     * Creates a new Kernel instance with the given plugin.
     * @param prefix The prefix
     */
    public Kernel(String prefix) {
        this.prefix = prefix;
        this.logger = Logger.getLogger(this.prefix);
    }

    private void createLog(Level level, String message, Throwable throwable, Object... values) {
        if (values != null) {
            int index = 0;
            for (Object value : values) {
                message = message.replaceAll("\\{" + index + "}", value.toString());
                index++;
            }
        }
        if(throwable == null) {
            this.logger.log(level, message);
        } else {
            this.logger.log(level, message, throwable);
        }
    }

    private void createLog(Level level, Collection<String> messages, Throwable throwable, Object... values) {
        if(values == null) {
            messages.forEach((String message) -> createLog(level, message, throwable));
        } else {
            int index = 0;
            List<String> result = new ArrayList<>();
            for(String message : messages) {
                for(Object value : values) {
                    message = message.replaceAll("\\{"+index+"}", value.toString());
                    index++;
                }
                result.add(message);
            }
            result.forEach((String m) -> createLog(level, m, throwable));
        }
    }

    /**
     * Logs a info message. <br />
     * If the variable 'values' is provided, it replaces placeholders in the message with the provided values.
     * @param message The message to log
     * @param values The values to replace placeholders
     */
    public void createInfo(String message, Object... values) {
        createLog(Level.INFO, Collections.singleton(message), null, values);
    }

    /**
     * Logs a info message from a collection of messages. <br />
     * If the variable 'values' is provided, it replaces placeholders in the message with the provided values.
     * @param messages The messages to log
     * @param values The values to replace placeholders
     */
    public void createInfo(Collection<String> messages, Object... values) {
       createLog(Level.INFO, messages, null, values);
    }



    /**
     * Logs a warning message. <br />
     * If the variable 'values' is provided, it replaces placeholders in the message with the provided values.
     * @param message The message to log
     * @param values The values to replace placeholders
     */
    public void createWarning(String message, Object... values) {
        createLog(Level.WARNING, Collections.singleton(message), null, values);
    }

    /**
     * Logs a warning message from a collection of messages. <br />
     * If the variable 'values' is provided, it replaces placeholders in the message with the provided values.
     * @param messages The messages to log
     * @param values The values to replace placeholders
     */
    public void createWarning(Collection<String> messages, Object... values) {
        createLog(Level.WARNING, messages, null, values);
    }

    /**
     * Logs a error message. <br />
     * If the variable 'values' is provided, it replaces placeholders in the message with the provided values.
     * @param message The message to log
     * @param values The values to replace placeholders
     */
    public void createError(String message, Object... values) {
        createLog(Level.SEVERE, Collections.singleton(message), null, values);
    }

    /**
     * Logs a error message with a throwable. <br />
     * If the variable 'values' is provided, it replaces placeholders in the message with the provided values.
     * @param message The message to log
     * @param e The throwable to log
     * @param values The values to replace placeholders
     */
    public void createError(String message, Throwable e, Object... values) {
        createLog(Level.SEVERE, Collections.singleton(message), e, values);
    }



    public String getPrefix() {
        return prefix;
    }
}
