package snoot.chairs;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import snoot.Main;
import snoot.parents.SnootCommandExecutor;

import java.util.ArrayList;
import java.util.Collections;

public class ChairsCommandExecutor extends SnootCommandExecutor {

    public ChairsCommandExecutor() {
        super("group of commands for sitting on stairs", "snoot.chairs", true);
        addSubCommand(new SubCommand(
                "off",
                "",
                "turn chairs off",
                null,
                Collections.singletonMap(0, ChairsCommandExecutor::commandOff)));
        addSubCommand(new SubCommand(
                "on",
                "",
                "turn chairs on",
                null,
                Collections.singletonMap(0, ChairsCommandExecutor::commandOn)));
    }

    private static void commandOff(final CommandSender sender, final ArrayList<String> args) {
        if (!Main.getChairsManager().chairsEnabled((Player)sender)) {
            sender.sendMessage(ChatColor.RED + "Chairs is already turned off.");
            return;
        }
        Main.getChairsManager().disableChairs((Player)sender);
        sender.sendMessage( ChatColor.GREEN + "Chairs has been turned off.");
    }


    private static void commandOn(final CommandSender sender, final ArrayList<String> args) {
        if (Main.getChairsManager().chairsEnabled((Player)sender)) {
            sender.sendMessage( ChatColor.RED + "Chairs is already turned on.");
            return;
        }
        Main.getChairsManager().enableChairs((Player)sender);
        sender.sendMessage(ChatColor.GREEN + "Chairs has been turned on.");
    }

}
