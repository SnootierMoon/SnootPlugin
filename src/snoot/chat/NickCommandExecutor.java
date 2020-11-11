package snoot.chat;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import snoot.Main;
import snoot.parents.SnootCommandExecutor;

import java.util.ArrayList;
import java.util.Collections;

public class NickCommandExecutor extends SnootCommandExecutor {

    public NickCommandExecutor() {
        super("group of commands for changing your nickname", "snoot.chat.nick", true);
        addSubCommand(new SubCommand(
                "remove",
                "",
                "remove your current nickname",
                null,
                Collections.singletonMap(0, NickCommandExecutor::commandRemove)));
        addSubCommand(new SubCommand(
                "set",
                "",
                "change your current nickname",
                null,
                Collections.singletonMap(1, NickCommandExecutor::commandSet),
                true));
    }

    private static void commandRemove(CommandSender sender, ArrayList<String> args) {
        if (!Main.getChatManager().hasNick((Player)sender)) {
            sender.sendMessage(ChatColor.RED + "You do not have a nickname.");
            return;
        }
        Main.getChatManager().setNick((Player) sender, null);
        sender.sendMessage(ChatColor.GREEN + "Your nickname has been removed.");
    }

    private static void commandSet(CommandSender sender, ArrayList<String> args) {
        Main.getChatManager().setNick((Player)sender, ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', args.get(0)) + ChatColor.RESET);
        sender.sendMessage(ChatColor.GREEN + "Your nickname has been set to " + ((Player)sender).getDisplayName() + ChatColor.GREEN + ".");
    }

}