package snoot.chat;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import snoot.Main;
import snoot.parents.SnootCommandExecutor;

import java.util.ArrayList;
import java.util.Collections;

public class ChatcolorCommandExecutor extends SnootCommandExecutor {

    private final static String chatColorList = ChatColor.GOLD + "" + ChatColor.BOLD + "Chat Colors:\n" + String.join(" ",  ChatManager.chatColorList);

    public ChatcolorCommandExecutor() {
        super("group of commands for changing the color of your chat messages", "snoot.chat.color", false);
        addSubCommand(new SubCommand(
                "list",
                "",
                "list all chat colors",
                null,
                Collections.singletonMap(0, ChatcolorCommandExecutor::commandList)));
        addSubCommand(new SubCommand(
                "remove",
                "",
                "remove your current chat color",
                "snoot.chat.color",
                Collections.singletonMap(0, ChatcolorCommandExecutor::commandRemove),
                true));
        addSubCommand(new SubCommand(
                "set",
                "<color>",
                "change your current chat color",
                "snoot.chat.color",
                Collections.singletonMap(1, ChatcolorCommandExecutor::commandSet),
                true));
    }

    private static void commandList(CommandSender sender, ArrayList<String> args) {
        sender.sendMessage(chatColorList);
    }

    private static void commandRemove(CommandSender sender, ArrayList<String> args) {
        if (!Main.getChatManager().hasColor((Player)sender)) {
            sender.sendMessage(ChatColor.RED + "You do not have a chat color.");
            return;
        }
        Main.getChatManager().setColor((Player)sender, null);
        sender.sendMessage(ChatColor.GREEN + "Your chat color has been removed.");
    }

    private static void commandSet(CommandSender sender, ArrayList<String> args) {
        ChatColor chatColor;
        try {
            chatColor = ChatColor.valueOf(args.get(0).toUpperCase());
        } catch (java.lang.IllegalArgumentException e) {
            sender.sendMessage(ChatColor.RED + "Invalid chat color.");
            return;
        }
        if (!chatColor.isColor()) {
            sender.sendMessage(ChatColor.RED + "Invalid chat color.");
        }
        Main.getChatManager().setColor((Player)sender, chatColor);
        sender.sendMessage(ChatColor.GREEN + "Your chat color has been set to to " + chatColor + chatColor.name().toLowerCase() + ChatColor.GREEN + ".");
    }

}
