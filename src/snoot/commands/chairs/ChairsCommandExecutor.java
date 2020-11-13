package snoot.commands.chairs;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import snoot.Colors;
import snoot.Main;
import snoot.commands.parents.SnootCommandExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChairsCommandExecutor extends SnootCommandExecutor {

    public ChairsCommandExecutor() {
        super("group of commands for sitting on stairs", "snoot.commands.chairs", true);
        addSubCommand(new SubCommand(
                "off",
                new ArrayList<>(),
                "turn chairs off",
                null,
                Collections.singletonMap(0, ChairsCommandExecutor::commandOff)));
        addSubCommand(new SubCommand(
                "on",
                new ArrayList<>(),
                "turn chairs on",
                null,
                Collections.singletonMap(0, ChairsCommandExecutor::commandOn)));
    }

    private static void commandOff(CommandSender sender, List<String> args) {
        if (!Main.getChairsManager().chairsEnabled((Player)sender)) {
            Colors.sendError(sender, "Chairs is already off.");
            return;
        }
        Main.getChairsManager().disableChairs((Player)sender);
        Colors.sendSuccess(sender, "Chairs has been turned off.");
    }


    private static void commandOn(CommandSender sender, List<String> args) {
        if (Main.getChairsManager().chairsEnabled((Player)sender)) {
            Colors.sendError(sender, "Chairs is already on.");
            return;
        }
        Main.getChairsManager().enableChairs((Player)sender);
        Colors.sendSuccess(sender, "Chairs has been turned on.");
    }

}
