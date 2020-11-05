package snoot.chat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import snoot.Main;
import snoot.MessageFormat;

import javax.annotation.Nonnull;

public final class NickCommandExecutor implements CommandExecutor {

    private final static String removeUsage = MessageFormat.command("nick", "remove");
    private final static String setUsage = MessageFormat.command("nick", "set <nickname>");

    private final static String fullUsage = String.join("\n", removeUsage, setUsage);

    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull final Command command, @Nonnull final String label, @Nonnull final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageFormat.playerExclusive);
            return true;
        } else if (args.length == 0) {
            sender.sendMessage(MessageFormat.usage);
            sender.sendMessage(fullUsage);
        } else if (!sender.hasPermission("snoot.nick")) {
            sender.sendMessage(MessageFormat.invalidPermissions);
        } else if (args[0].equals("remove")) {
            if (args.length != 1) {
                sender.sendMessage(MessageFormat.usage);
                sender.sendMessage(removeUsage);
            } else if (!Main.getChatManager().hasNick((Player)sender)) {
                sender.sendMessage(MessageFormat.error("You do not have a nick."));
            } else {
                Main.getChatManager().setNick((Player)sender, null);
                sender.sendMessage(MessageFormat.success("Your nick has been removed."));
            }
        } else if (args[0].equals("set")) {
            if (args.length != 2) {
                sender.sendMessage(MessageFormat.usage);
                sender.sendMessage(setUsage);
            } else {
                Main.getChatManager().setNick((Player)sender, ChatColor.translateAlternateColorCodes('&', args[1]) + ChatColor.RESET);
                sender.sendMessage(MessageFormat.success("Your nick has been set to " + ((Player)sender).getDisplayName() + "."));
            }
        } else {
            sender.sendMessage(MessageFormat.usage);
            sender.sendMessage(fullUsage);
        }
        return true;
    }

}
