package snoot.commands.chat;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import snoot.Colors;
import snoot.Main;
import snoot.commands.parents.SnootCommandExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ChatcolorCommandExecutor extends SnootCommandExecutor {

    public static final BaseComponent[] chatColorList = new ArrayList<BaseComponent>() {{
        for (BaseComponent[] baseComponent : Colors.colorList.stream().map(color -> new ComponentBuilder(color.name().toLowerCase())
                .color(color.asBungee())
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chatcolor set " + color.name().toLowerCase()))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Colors.regular + "Click to set your color to " + color + color.name().toLowerCase() + Colors.regular + ".")))
                .append(" ")
                .event((ClickEvent)null)
                .event((HoverEvent)null)
                .create()).collect(Collectors.toList())) {
            Collections.addAll(this, baseComponent);
        }
    }}.toArray(new BaseComponent[0]);

    public ChatcolorCommandExecutor() {
        super("group of commands for changing the color of your chat messages", "snoot.chat.color", false);
        addSubCommand(new SubCommand(
                "list",
                new ArrayList<>(),
                "list all chat colors",
                null,
                Collections.singletonMap(0, ChatcolorCommandExecutor::commandList)));
        addSubCommand(new SubCommand(
                "remove",
                new ArrayList<>(),
                "remove your current chat color",
                null,
                Collections.singletonMap(0, ChatcolorCommandExecutor::commandRemove),
                true));
        addSubCommand(new SubCommand(
                "set",
                Collections.singletonList(new HelpArg("color", "your new chat color", "see /colors")),
                "change your current chat color",
                null,
                Collections.singletonMap(1, ChatcolorCommandExecutor::commandSet),
                true));
    }

    private static void commandList(CommandSender sender, List<String> args) {
        Colors.sendTitle(sender, "---- Chat Colors ----");
        sender.spigot().sendMessage(chatColorList);
    }

    private static void commandRemove(CommandSender sender, List<String> args) {
        if (!Main.getChatManager().hasColor((Player)sender)) {
            Colors.sendError(sender, "You do not have a chat color.");
            return;
        }
        Main.getChatManager().setColor((Player)sender, null);
        Colors.sendSuccess(sender, "Your chat color has been removed.");
    }

    private static void commandSet(CommandSender sender, List<String> args) {
        ChatColor chatColor = Main.getChatManager().setColor((Player)sender, args.get(0));
        if (chatColor == null) {
            Colors.sendError(sender, "Invalid chat color.");
            return;
        }
        Colors.sendSuccess(sender, "Your chat color has been set to to ", Colors.coloredColor(chatColor), ".");
    }

}
