package snoot.chat;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import snoot.Main;
import snoot.parents.SnootCommandExecutor;
import snoot.util.MessageFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.min;

public class TagsCommandExecutor extends SnootCommandExecutor {

    TagsCommandExecutor() {
        super("family of commands to create tags and change your tag");
        addSubCommand(new SubCommand(
                "create",
                "<TAG-ID> [DISPLAY-NAME]",
                "add a tag to the tag list with the display format",
                "snoot.chat.tags.modify",
                Map.of(1, TagsCommandExecutor::commandCreate,
                        2, TagsCommandExecutor::commandCreate2)));
        addSubCommand(new SubCommand(
                "delete",
                "<TAG-ID>",
                "delete a tag from the tag list",
                "snoot.chat.tags.modify",
                Map.of(1, TagsCommandExecutor::commandDelete)));
        addSubCommand(new SubCommand(
                "list",
                "[PAGE-NUMBER]",
                "list a page of the tag list",
                "snoot.chat.tags.modify",
                Map.of(0, TagsCommandExecutor::commandList,
                        1, TagsCommandExecutor::commandList2)));
        addSubCommand(new SubCommand(
                "modify",
                "<TAG-ID> <DISPLAY-NAME>",
                "change the display name of a tag",
                "snoot.chat.tags.modify",
                Map.of(2, TagsCommandExecutor::commandModify)));
        addSubCommand(new SubCommand(
                "remove",
                "",
                "remove your current tag",
                "snoot.chat.tags.use",
                Map.of(0, TagsCommandExecutor::commandRemove),
                true));
        addSubCommand(new SubCommand(
                "set",
                "<TAG-ID>",
                "change your current tag",
                "snoot.chat.tags.use",
                Map.of(1, TagsCommandExecutor::commandSet),
                true));
        initialize();
    }
    
    private static void commandCreate(CommandSender sender, List<String> args) {
        args.add(MessageFormat.info("Default"));
        commandCreate2(sender, args);
    }

    private static void commandCreate2(CommandSender sender, List<String> args) {
        if (Main.getChatManager().tagExists(args.get(0))) {
            sender.sendMessage(MessageFormat.error("That tag already exists."));
        } else {
            String tagName = ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', args.get(1)) + ChatColor.RESET;
            Main.getChatManager().setTagName(args.get(0), tagName);
            sender.sendMessage(MessageFormat.success("Created tag \"") + MessageFormat.info(args.get(0)) + MessageFormat.success("\" with the display name \"") + tagName + MessageFormat.success("\"."));
        }
    }

    private static void commandDelete(CommandSender sender, List<String> args) {
        if (!Main.getChatManager().tagExists(args.get(0))) {
            sender.sendMessage(MessageFormat.error("That tag does not exist."));
        } else {
            Main.getChatManager().setTagName(args.get(0), null);
            sender.sendMessage(MessageFormat.success("Deleted tag \"") + MessageFormat.info(args.get(0)) + MessageFormat.success("\"."));
        }
    }

    private static void commandList(CommandSender sender, List<String> args) {
        args.add("1");
        commandList2(sender, args);
    }

    private static void commandList2(CommandSender sender, List<String> args) {
        int pageNumber;
        try {
            pageNumber = Integer.parseInt(args.get(0));
        } catch (NumberFormatException e) {
            sender.sendMessage(MessageFormat.error("Invalid page number."));
            return;
        }
        List<String> tagIDs = Main.getChatManager().getTagIDs();
        if (tagIDs == null || tagIDs.size() == 0) {
            sender.sendMessage(MessageFormat.info("No tags have been created yet."));
            return;
        }
        int pageCount = (tagIDs.size() + 9) / 10;
        if (pageNumber <= 0 || pageNumber > pageCount) {
            sender.sendMessage(MessageFormat.error("Invalid page number (Choose from 0-" + pageCount + ")."));
            return;
        }
        List<BaseComponent> baseComponents = new ArrayList<>();
        {
            BaseComponent message = new TextComponent("Tag List");
            message.setBold(true);
            baseComponents.add(message);
        }
        if (pageNumber - 1 > 0) {
            BaseComponent message = new TextComponent(" [Prev]");
            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tags list " + (pageNumber - 1)));
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to go to the previous page.")));
            message.setColor(net.md_5.bungee.api.ChatColor.GREEN);
            baseComponents.add(message);
        }
        if (pageNumber + 1 <= pageCount) {
            BaseComponent message = new TextComponent(" [Next]");
            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tags list " + (pageNumber + 1)));
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to go to the next page.")));
            message.setColor(net.md_5.bungee.api.ChatColor.GREEN);
            baseComponents.add(message);
        }
        sender.spigot().sendMessage(baseComponents.toArray(new BaseComponent[0]));
        for (int i = pageNumber * 10 - 10; i < min(pageNumber * 10, tagIDs.size()); i++) {
            String tagID = tagIDs.get(i);
            String tagName = Main.getChatManager().getTagName(tagID);
            BaseComponent message = new TextComponent(ChatColor.GRAY + "" + (i + 1) + ". " + ChatColor.BOLD + tagID + ChatColor.DARK_GRAY + " - " + tagName);
            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tags set " + tagID));
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to set your tag to " + tagName)));
            sender.spigot().sendMessage(message);
        }
    }

    private static void commandModify(CommandSender sender, List<String> args) {
        if (!Main.getChatManager().tagExists(args.get(0))) {
            sender.sendMessage(MessageFormat.error("That tag does not exist."));
        } else {
            String tagName = ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', args.get(1)) + ChatColor.RESET;
            Main.getChatManager().setTagName(args.get(0), tagName);
            sender.sendMessage(MessageFormat.success("Changed tag \"") + MessageFormat.info(args.get(0)) + MessageFormat.success("\" to \"") + tagName + MessageFormat.success("\"."));
        }
    }

    private static void commandRemove(CommandSender sender, List<String> args) {
        if (Main.getChatManager().getPlayerTagID((Player)sender) == null) {
            sender.sendMessage(MessageFormat.error("You do not have a tag."));
        } else {
            Main.getChatManager().setPlayerTagID((Player)sender, null);
            sender.sendMessage(MessageFormat.success("Your tag has been removed."));
        }

    }

    private static void commandSet(CommandSender sender, List<String> args) {
        if (!Main.getChatManager().tagExists(args.get(0))) {
            sender.sendMessage(MessageFormat.error("That tag does not exist."));
        } else {
            Main.getChatManager().setPlayerTagID((Player)sender, args.get(0));
            sender.sendMessage(MessageFormat.success("Your tag has been set to ") + Main.getChatManager().getPlayerTagName((Player)sender) + MessageFormat.success("."));
        }
    }

}
