package fr.cercusmc.adventcore.commands;

import fr.cercusmc.adventcore.AdventCore;
import fr.cercusmc.adventcore.utils.commands.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class RegisterCommand extends Command {

    public RegisterCommand() {
        super("loadModule", "Charger un module", "/loadModule <moduleName>", Collections.emptyList(), "adventcore.loadModule", (sender, args) -> AdventCore.getInstance().getModuleManager().loadModule(args[0]));
    }

    @Nonnull
    @Override
    public List<String> tabComplete(@Nonnull CommandSender sender, @Nonnull String alias, @Nonnull String[] args) throws IllegalArgumentException {
        if(args.length == 1) {
            return AdventCore.getInstance().getModules().keySet().stream().filter(name -> name.startsWith(args[0])).toList();
        }
        return List.of();
    }
}
