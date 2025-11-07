package fr.cercusmc.adventcore.commands;

import fr.cercusmc.adventcore.AdventCore;
import fr.cercusmc.adventcore.core.Module;
import fr.cercusmc.adventcore.utils.commands.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class UnregisterCommand extends Command {

    public UnregisterCommand() {
        super("unloadModule", "Decharger un module", "/unloadModule <moduleName>", Collections.emptyList(), "adventcore.unloadModule", (sender, args) -> {
            Module module = AdventCore.getInstance().getModules().get(args[0]);
            if(module != null) {
                AdventCore.getInstance().getModuleManager().unloadModule(module);
            }
        });
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
