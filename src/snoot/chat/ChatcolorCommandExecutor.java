package snoot.chat;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import snoot.Main;
import snoot.parents.SnootCommandExecutor;
import snoot.util.MessageFormat;

import java.util.List;
import java.util.Map;

public class ChatcolorCommandExecutor extends SnootCommandExecutor {

    private final static String chatColorList = "Choose a chat color from the list:\n" + String.join(" ",  ChatManager.chatColorList);

    ChatcolorCommandExecutor() {
        super("family of commands to change the color of your chat messages");
        addSubCommand(new SubCommand(
                "list",
                "",
                "list all chat colors",
                "snoot.chat.color",
                Map.of(0, ChatcolorCommandExecutor::commandList)));
        addSubCommand(new SubCommand(
                "remove",
                "",
                "remove your current chat color",
                "snoot.chat.color",
                Map.of(0, ChatcolorCommandExecutor::commandRemove)));
        addSubCommand(new SubCommand(
                "set",
                "<color>",
                "change your current chat color",
                "snoot.chat.color",
                Map.of(0, ChatcolorCommandExecutor::commandSet)));
        initialize();
    }

    private static void commandList(CommandSender sender, List<String> args) {
        sender.sendMessage(chatColorList);
    }

    private static void commandRemove(CommandSender sender, List<String> args) {
        if (!Main.getChatManager().hasColor((Player)sender)) {
            sender.sendMessage(MessageFormat.error("You do not have a chat color."));
        } else {
            Main.getChatManager().setColor((Player)sender, null);
            sender.sendMessage(MessageFormat.success("Your chat color has been removed."));
        }
    }

    private static void commandSet(CommandSender sender, List<String> args) {
        ChatColor chatColor;
        try {
            chatColor = ChatColor.valueOf(args.get(0).toUpperCase());
        } catch (java.lang.IllegalArgumentException e) {
            sender.sendMessage(MessageFormat.error("Invalid chat color."));
            return;
        }
        if (!chatColor.isColor()) {
            sender.sendMessage(MessageFormat.error("Invalid chat color."));
        } else {
            Main.getChatManager().setColor((Player)sender, chatColor);
            sender.sendMessage(MessageFormat.success("Your chat color has been set to to ") + MessageFormat.coloredColor(chatColor) + MessageFormat.success("."));
        }
    }

}
