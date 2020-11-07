package snoot.chairs;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import snoot.Main;
import snoot.parents.SnootCommandExecutor;
import snoot.util.MessageFormat;

import java.util.List;
import java.util.Map;

public class ChairsCommandExecutor extends SnootCommandExecutor {

    ChairsCommandExecutor() {
        super("family of commands to sit on stairs");
        addSubCommand(new SubCommand(
                "off",
                "",
                "turn chairs off",
                "snoot.chairs",
                Map.of(0, ChairsCommandExecutor::commandOff)));
        addSubCommand(new SubCommand(
                "on",
                "",
                "turn chairs on",
                "snoot.chairs",
                Map.of(0, ChairsCommandExecutor::commandOn)));
        initialize();
    }

    private static void commandOff(CommandSender sender, List<String> args) {
        if (!Main.getChairsManager().chairsEnabled((Player)sender)) {
            sender.sendMessage(MessageFormat.error("Chairs is already turned off."));
        } else {
            Main.getChairsManager().disableChairs((Player)sender);
            sender.sendMessage(MessageFormat.success("Chairs has been turned off."));
        }
    }


    private static void commandOn(CommandSender sender, List<String> args) {
        if (Main.getChairsManager().chairsEnabled((Player)sender)) {
            sender.sendMessage(MessageFormat.error("Chairs is already turned on."));
        } else {
            Main.getChairsManager().enableChairs((Player)sender);
            sender.sendMessage(MessageFormat.success("Chairs has been turned on."));
        }
    }

}
