package snoot.chairs;

import com.mojang.brigadier.Message;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import snoot.Main;
import snoot.MessageFormat;

import javax.annotation.Nonnull;

public class ChairsCommandExecutor implements CommandExecutor {

    private final static String offUsage = MessageFormat.command("chairs", "off");
    private final static String offHelp = MessageFormat.commandHelp("Turns chairs off.");
    private final static String onUsage = MessageFormat.command("chairs", "on");
    private final static String onHelp = MessageFormat.commandHelp("Turns chairs on.");

    private final static String fullUsage = String.join("\n", offUsage, onUsage);

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, final @Nonnull Command command, @Nonnull final String label, @Nonnull final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageFormat.playerExclusive);
        } else if (!sender.hasPermission("snoot.chairs")) {
            sender.sendMessage(MessageFormat.invalidPermissions);
        } else if (args.length == 0) {
            sender.sendMessage(MessageFormat.usage);
            sender.sendMessage(fullUsage);
        } else if (args[0].equals("off")) {
            if (args.length != 1) {
                sender.sendMessage(MessageFormat.usage);
                sender.sendMessage(offUsage);
            } else if (!Main.getChairsManager().chairsEnabled((Player)sender)) {
                sender.sendMessage(MessageFormat.error("Chairs is already turned off."));
            } else {
                Main.getChairsManager().disableChairs((Player)sender);
                sender.sendMessage(MessageFormat.success("Chairs has been turned off."));
            }
        } else if (args[0].equals("on")) {
            if (args.length != 1) {
                sender.sendMessage(MessageFormat.usage);
                sender.sendMessage(onUsage);
            } else if (Main.getChairsManager().chairsEnabled((Player)sender)) {
                sender.sendMessage(MessageFormat.error("Chairs is already turned on."));
            } else {
                Main.getChairsManager().enableChairs((Player)sender);
                sender.sendMessage(MessageFormat.success("Chairs has been turned on."));
            }
        } else {
            sender.sendMessage(MessageFormat.usage);
            sender.sendMessage(fullUsage);
        }
        return true;
    }

}
