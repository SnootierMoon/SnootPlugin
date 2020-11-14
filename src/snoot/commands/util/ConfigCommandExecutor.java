package snoot.commands.util;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import snoot.Colors;
import snoot.Main;
import snoot.commands.parents.SnootCommandExecutor;
import snoot.commands.survival.SurvivalManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ConfigCommandExecutor extends SnootCommandExecutor {

    ConfigCommandExecutor() {
        super("modify the behavior of SnootPlugin", "snoot.config", false);
        addSubCommand(new SubCommand(
                "world-type",
                Arrays.asList(new HelpArg("world", "the world to change", "world name"),
                        new HelpArg("type", "what to change it to", "\"creative\", \"survival\", or \"none\"")),
                "declare a world as creative or survival",
                null,
                Collections.singletonMap(2, ConfigCommandExecutor::worldCommand)));
    }

    private static void worldCommand(CommandSender sender, List<String> args) {
        World world = Main.getInstance().getServer().getWorld(args.get(0));
        if (world == null) {
            Colors.sendError(sender, "That world does not exist.");
            return;
        }
        SurvivalManager.WorldType worldType = Main.getSurvivalManager().setWorldType(world, args.get(1));
        if (worldType == null) {
            Colors.sendError(sender, "Invalid world type.");
            return;
        }
        Colors.sendSuccess(sender, "The world ", world.getName(), " has been set to ", worldType.toString().toLowerCase(), ".");
    }

}
