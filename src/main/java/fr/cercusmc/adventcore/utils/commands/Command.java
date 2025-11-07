package fr.cercusmc.adventcore.utils.commands;

import fr.cercusmc.adventcore.utils.messages.LoggingCategory;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public abstract class Command extends BukkitCommand {

    private List<SubCommand> subCommands;
    private final BiConsumer<CommandSender, String[]> actionBaseCommand;

    private String description;
    private String usage;
    private String permission;
    private List<String> aliases;
    private String name;

    public Command(@Nonnull String name, @Nonnull String description, @Nonnull String usageMessage, @Nonnull List<String> aliases, String permission, @Nonnull BiConsumer<CommandSender, String[]> actionBaseCommand, SubCommand... subCommands) {
        super(name, description, usageMessage, aliases);
        this.setPermission(permission);
        if(subCommands == null)
            this.subCommands = new ArrayList<>();
        else
            this.subCommands = new ArrayList<>(Arrays.asList(subCommands));
        this.actionBaseCommand = actionBaseCommand;
        this.description = description;
        this.usage = usageMessage;
        this.permission = permission;
        this.aliases = aliases;
        this.name = name;
    }

    /**
     * Add a new subCommand to list of subCommand
     * @param subCommand SubCommand
     * @return this command instance for chaining
     */
    public Command addSubCommand(SubCommand subCommand) {
        this.subCommands.add(subCommand);
        return this;
    }

    /**
     * Add multiple subCommands to list of subCommand
     * @param subCommands SubCommands
     * @return this command instance for chaining
     */
    public Command addSubCommands(SubCommand... subCommands) {
        this.subCommands.addAll(Arrays.asList(subCommands));
        return this;
    }


    /**
     * Executes the command, returning its success
     *
     * @param sender       Source object which is executing this command
     * @param commandLabel The alias of the command used
     * @param args         All arguments passed to the command, split via ' '
     * @return true if the command was successful, otherwise false
     */
    @Override
    public boolean execute(@Nonnull CommandSender sender, @Nonnull String commandLabel, String[] args) {
        if(args.length > 0 && !subCommands.isEmpty()) {

            Optional<SubCommand> subCommandChoosen = this.subCommands.stream().filter((SubCommand s) -> s.getName().equalsIgnoreCase(args[0]) || s.getAliases().contains(args[0])).findFirst();

            if(subCommandChoosen.isEmpty()) {
                return false;
            }
            SubCommand subCommand = subCommandChoosen.get();
            if(subCommand.canPerform(sender) || sender.hasPermission(subCommand.getPermission())) {
                subCommand.executeSubCommand(sender, args.length > 1? Arrays.copyOfRange(args, 1, args.length) : new String[0]);
                return true;
            }

        } else {
            if(actionBaseCommand != null) {
                this.actionBaseCommand.accept(sender, args);
            }
            return true;
        }

        return false;

    }

    @Nonnull
    @Override
    public abstract List<String> tabComplete(@Nonnull CommandSender sender, @Nonnull String alias, @Nonnull String[] args) throws IllegalArgumentException;

    @Nonnull
    @Override
    public List<String> getAliases() {
        return this.aliases;
    }

    @Nonnull
    @Override
    public  String getName() {
        return this.name;
    }

    @Nonnull
    @Override
    public  String getDescription() {
        return this.description;
    }

    @Nonnull
    @Override
    public  String getUsage() {
        return this.usage;
    }

    @Nullable
    @Override
    public String getPermission() {
        return this.permission;
    }

}
