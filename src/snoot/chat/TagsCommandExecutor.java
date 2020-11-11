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

import java.util.*;
import java.util.function.BiConsumer;

import static java.lang.Integer.min;

public class TagsCommandExecutor extends SnootCommandExecutor {

    public TagsCommandExecutor() {
        super("group of commands for creating and equipping your tag", null, false);
        addSubCommand(new SubCommand(
                "create",
                "<TAG-ID> [DISPLAY-NAME]",
                "add a tag to the tag list with the display format",
                "snoot.chat.tags.modify",
                new HashMap<Integer, BiConsumer<CommandSender, ArrayList<String>>>() {{
                    put(1,  TagsCommandExecutor::commandCreate);
                    put(2,  TagsCommandExecutor::commandCreate2);
                }}));
        addSubCommand(new SubCommand(
                "delete",
                "<TAG-ID>",
                "delete a tag from the tag list",
                "snoot.chat.tags.modify",
                Collections.singletonMap(1, TagsCommandExecutor::commandDelete)));
        addSubCommand(new SubCommand(
                "list",
                "[PAGE-NUMBER]",
                "list a page of the tag list",
                "snoot.chat.tags.modify",
                new HashMap<Integer, BiConsumer<CommandSender, ArrayList<String>>>() {{
                    put(0,  TagsCommandExecutor::commandList);
                    put(1,  TagsCommandExecutor::commandList2);
                }}));
        addSubCommand(new SubCommand(
                "modify",
                "<TAG-ID> <DISPLAY-NAME>",
                "change the display name of a tag",
                "snoot.chat.tags.modify",
                Collections.singletonMap(2, TagsCommandExecutor::commandModify)));
        addSubCommand(new SubCommand(
                "remove",
                "",
                "remove your current tag",
                "snoot.chat.tags.use",
                Collections.singletonMap(0, TagsCommandExecutor::commandRemove),
                true));
        addSubCommand(new SubCommand(
                "set",
                "<TAG-ID>",
                "change your current tag",
                "snoot.chat.tags.use",
                Collections.singletonMap(1, TagsCommandExecutor::commandSet),
                true));
    }
    
    private static void commandCreate(CommandSender sender, ArrayList<String> args) {
        args.add(ChatColor.GRAY + "Default");
        commandCreate2(sender, args);
    }

    private static void commandCreate2(CommandSender sender, ArrayList<String> args) {
        if (Main.getChatManager().tagExists(args.get(0))) {
            sender.sendMessage(ChatColor.RED + "That tag already exists.");
            return;
        }
        String tagName = ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', args.get(1)) + ChatColor.RESET;
        Main.getChatManager().setTagName(args.get(0), tagName);
        sender.sendMessage(ChatColor.GREEN + "Created tag \"" + ChatColor.GRAY + args.get(0) + ChatColor.GREEN + "\" with the display name \"" + tagName + ChatColor.GREEN + "\".");
    }

    private static void commandDelete(CommandSender sender, ArrayList<String> args) {
        if (!Main.getChatManager().tagExists(args.get(0))) {
            sender.sendMessage(ChatColor.RED + "That tag does not exist.");
            return;
        }
        Main.getChatManager().setTagName(args.get(0), null);
        sender.sendMessage(ChatColor.GREEN + "Deleted tag \"" + ChatColor.GRAY + args.get(0) + ChatColor.GREEN + "\".");
    }

    private static void commandList(CommandSender sender, ArrayList<String> args) {
        args.add("1");
        commandList2(sender, args);
    }

    private static void commandList2(CommandSender sender, ArrayList<String> args) {
        int pageNumber;
        try {
            pageNumber = Integer.parseInt(args.get(0));
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Invalid page number.");
            return;
        }
        List<String> tagIDs = Main.getChatManager().getTagIDs();
        if (tagIDs == null || tagIDs.size() == 0) {
            sender.sendMessage(ChatColor.GRAY +"No tags have been created yet.");
            return;
        }
        int pageCount = (tagIDs.size() + 9) / 10;
        if (pageNumber <= 0 || pageNumber > pageCount) {
            sender.sendMessage(ChatColor.RED + "Invalid page number (Choose from 1-" + pageCount + ").");
            return;
        }
        List<BaseComponent> baseComponents = new ArrayList<>();
        BaseComponent baseComponent = new TextComponent("Tag List") ;
        baseComponent.setBold(true);
        baseComponent.setColor(ChatColor.GOLD);
        baseComponents.add(baseComponent);
        if (pageNumber - 1 > 0) {
            baseComponent = new TextComponent(" [Prev]");
            baseComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tags list " + (pageNumber - 1)));
            baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to go to the previous page.")));
            baseComponent.setColor(ChatColor.GREEN);
            baseComponents.add(baseComponent);
        }
        if (pageNumber + 1 <= pageCount) {
            baseComponent = new TextComponent(" [Next]");
            baseComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tags list " + (pageNumber + 1)));
            baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to go to the next page.")));
            baseComponent.setColor(ChatColor.GREEN);
            baseComponents.add(baseComponent);
        }
        sender.spigot().sendMessage(baseComponents.toArray(new BaseComponent[0]));
        sender.sendMessage(ChatColor.GOLD + "" + ChatColor.GRAY + "Page " + ChatColor.DARK_GRAY + "#" + pageNumber + "/" + pageCount);
        for (int i = pageNumber * 10 - 10; i < min(pageNumber * 10, tagIDs.size()); i++) {
            String tagID = tagIDs.get(i);
            String tagName = Main.getChatManager().getTagName(tagID);
            baseComponent = new TextComponent(ChatColor.GRAY + "" + (i + 1) + ". " + ChatColor.BOLD + tagID + "" + ChatColor.DARK_GRAY + " - " + tagName);
            baseComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tags set " + tagID));
            baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to set your tag to " + tagName)));
            sender.spigot().sendMessage(baseComponent);
        }
    }

    private static void commandModify(CommandSender sender, ArrayList<String> args) {
        if (!Main.getChatManager().tagExists(args.get(0))) {
            sender.sendMessage(ChatColor.RED + "That tag does not exist.");
            return;
        }
        String tagName = ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', args.get(1)) + ChatColor.RESET;
        Main.getChatManager().setTagName(args.get(0), tagName);
        sender.sendMessage(ChatColor.GREEN + "Changed tag \"" + ChatColor.GRAY + args.get(0) + ChatColor.GREEN + "\" to \"" + tagName + ChatColor.GREEN + "\".");
    }

    private static void commandRemove(CommandSender sender, ArrayList<String> args) {
        if (Main.getChatManager().getPlayerTagID((Player)sender) == null) {
            sender.sendMessage(ChatColor.RED + "You do not have a tag.");
            return;
        }
        Main.getChatManager().setPlayerTagID((Player)sender, null);
        sender.sendMessage(ChatColor.GREEN + "Your tag has been removed.");
    }

    private static void commandSet(CommandSender sender, ArrayList<String> args) {
        if (!Main.getChatManager().tagExists(args.get(0))) {
            sender.sendMessage(ChatColor.RED + "That tag does not exist.");
            return;
        }
        Main.getChatManager().setPlayerTagID((Player)sender, args.get(0));
        sender.sendMessage(ChatColor.GREEN + "Your tag has been set to " + Main.getChatManager().getPlayerTagName((Player)sender) + ChatColor.GREEN + ".");
    }

}
