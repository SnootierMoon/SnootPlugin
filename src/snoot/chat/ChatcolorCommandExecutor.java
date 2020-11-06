package snoot.chat;

import com.mojang.brigadier.Message;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import snoot.Main;
import snoot.MessageFormat;

import javax.annotation.Nonnull;

public class ChatcolorCommandExecutor implements CommandExecutor {

    private final static String chatColorList = "Choose a chat color from the list:\n" + String.join(" ",  ChatManager.chatColorList);

    private final static String listUsage = MessageFormat.command("chatcolor", "list");
    private final static String listHelp = MessageFormat.commandHelp("Lists all chat colors.");
    private final static String removeUsage = MessageFormat.command("chatcolor", "remove");
    private final static String removeHelp = MessageFormat.commandHelp("Removes your current chat color.");
    private final static String setUsage = MessageFormat.command("chatcolor", "set <color>");
    private final static String setHelp = MessageFormat.commandHelp("Sets your chat color to <color>.");

    private final static String fullUsage = String.join("\n", listUsage, removeUsage, setUsage);

    public boolean onCommand(@Nonnull CommandSender sender, final @Nonnull Command command, @Nonnull final String label, @Nonnull final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageFormat.playerExclusive);
        } else if (!sender.hasPermission("snoot.chat.color")) {
            sender.sendMessage(MessageFormat.invalidPermissions);
        } else if (args.length == 0) {
            sender.sendMessage(MessageFormat.usage);
            sender.sendMessage(fullUsage);
        } else if (args[0].equals("list")) {
            if (args.length != 1) {
                sender.sendMessage(MessageFormat.usage);
                sender.sendMessage(listUsage);
            } else {
                sender.sendMessage(chatColorList);
            }
        } else if (args[0].equals("remove")) {
            if (args.length != 1) {
                sender.sendMessage(MessageFormat.usage);
                sender.sendMessage(removeUsage);
            } else if (!Main.getChatManager().hasColor((Player)sender)) {
                sender.sendMessage(MessageFormat.error("You do not have a chat color."));
            } else {
                Main.getChatManager().setColor((Player)sender, null);
                sender.sendMessage(MessageFormat.success("Your chat color has been removed."));
            }
        } else if (args[0].equals("set")) {
            if (args.length != 2) {
                sender.sendMessage(MessageFormat.usage);
                sender.sendMessage(setUsage);
            } else try {
                ChatColor chatColor = ChatColor.valueOf(args[1].toUpperCase());
                if (!chatColor.isColor()) {
                    sender.sendMessage(MessageFormat.error("Invalid chat color."));
                } else {
                    Main.getChatManager().setColor((Player)sender, chatColor);
                    sender.sendMessage(MessageFormat.success("Your chat color has been set to to ") + MessageFormat.coloredColor(chatColor) + MessageFormat.success("."));
                }
            } catch (java.lang.IllegalArgumentException e) {
                sender.sendMessage(MessageFormat.error("Invalid chat color."));
            }
        } else {
            sender.sendMessage(MessageFormat.usage);
            sender.sendMessage(fullUsage);
        }
        return true;
    }

}
