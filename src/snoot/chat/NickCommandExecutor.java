package snoot.chat;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import snoot.Main;
import snoot.parents.SnootCommandExecutor;
import snoot.util.MessageFormat;

import java.util.List;
import java.util.Map;

public class NickCommandExecutor extends SnootCommandExecutor {

    NickCommandExecutor() {
        super("family of commands to change your nickname");
        addSubCommand(new SubCommand(
                "remove",
                "",
                "remove your current nickname",
                "snoot.chat.nick",
                Map.of(0, NickCommandExecutor::commandRemove),
                true));
        addSubCommand(new SubCommand(
                "set",
                "",
                "change your current nickname",
                "snoot.chat.nick",
                Map.of(1, NickCommandExecutor::commandSet),
                true));
        initialize();
    }

    private static void commandRemove(CommandSender sender, List<String> args) {
        if (!Main.getChatManager().hasNick((Player)sender)) {
            sender.sendMessage(MessageFormat.error("You do not have a nickname."));
        } else {
            Main.getChatManager().setNick((Player) sender, null);
            sender.sendMessage(MessageFormat.success("Your nickname has been removed."));
        }
    }

    private static void commandSet(CommandSender sender, List<String> args) {
        Main.getChatManager().setNick((Player)sender, ChatColor.translateAlternateColorCodes('&', args.get(0)) + ChatColor.RESET);
        sender.sendMessage(MessageFormat.success("Your nickname has been set to " + ((Player)sender).getDisplayName() + "."));
    }

}