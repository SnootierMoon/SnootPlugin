package snoot.commands.chat;

import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import snoot.Colors;
import snoot.Main;
import snoot.commands.parents.SnootCommandExecutor;


import java.util.*;
import java.util.function.BiConsumer;

import static java.lang.Integer.min;

public class TagsCommandExecutor extends SnootCommandExecutor {

    public TagsCommandExecutor() {
        super("group of commands for creating and equipping your tag", null, false);
        addSubCommand(new SubCommand(
                "create",
                Arrays.asList(new HelpArg("tag-id", "the ID of the new tag", "anything"),
                        new HelpArg("display-name", "what the new tag will look like", "see /colors", false)),
                "add a tag to the tag list with the display format",
                "snoot.chat.tags.modify",
                new HashMap<Integer, BiConsumer<CommandSender, List<String>>>() {{
                    put(1,  TagsCommandExecutor::commandCreate);
                    put(2,  TagsCommandExecutor::commandCreate2);
                }}));
        addSubCommand(new SubCommand(
                "delete",
                Collections.singletonList(new HelpArg("tag-id", "the ID of the tag to delete", "an ID from /tag list")),
                "delete a tag from the tag list",
                "snoot.chat.tags.modify",
                Collections.singletonMap(1, TagsCommandExecutor::commandDelete)));
        addSubCommand(new SubCommand(
                "list",
                Collections.singletonList(new HelpArg("page-number", "the page number of the list", "number",false)),
                "list a page of the tag list",
                "snoot.chat.tags.modify",
                new HashMap<Integer, BiConsumer<CommandSender, List<String>>>() {{
                    put(0,  TagsCommandExecutor::commandList);
                    put(1,  TagsCommandExecutor::commandList2);
                }}));
        addSubCommand(new SubCommand(
                "modify",
                Arrays.asList(new HelpArg("tag-id", "the ID of the tag to modify", "an ID from /tag list"),
                        new HelpArg("display-name", "what the tag will look like", "see /colors")),
                "change the display name of a tag",
                "snoot.chat.tags.modify",
                Collections.singletonMap(2, TagsCommandExecutor::commandModify)));
        addSubCommand(new SubCommand(
                "remove",
                new ArrayList<>(),
                "remove your current tag",
                "snoot.chat.tags.use",
                Collections.singletonMap(0, TagsCommandExecutor::commandRemove),
                true));
        addSubCommand(new SubCommand(
                "set",
                Collections.singletonList(new HelpArg("tag-id", "the ID of the tag to equip", "an ID from /tag list")),
                "change your current tag",
                "snoot.chat.tags.use",
                Collections.singletonMap(1, TagsCommandExecutor::commandSet),
                true));
    }
    
    private static void commandCreate(CommandSender sender, List<String> args) {
        args.add(Colors.regular + "Default");
        commandCreate2(sender, args);
    }

    private static void commandCreate2(CommandSender sender, List<String> args) {
        if (Main.getChatManager().tagExists(args.get(0))) {
            Colors.sendError(sender, "That tag already exists.");
            return;
        }
        Main.getChatManager().setTagDisplayName(args.get(0), args.get(1));
        sender.spigot().sendMessage(new ComponentBuilder()
                .append("Created tag \"")
                .color(Colors.successColor.asBungee())
                .append(Main.getChatManager().getTagIDComponent(args.get(0)))
                .append("\" with the display name \"")
                .event((ClickEvent)null)
                .event((HoverEvent)null)
                .color(Colors.successColor.asBungee())
                .append(Main.getChatManager().getTagComponent(args.get(0)))
                .append("\".")
                .event((ClickEvent)null)
                .event((HoverEvent)null)
                .color(Colors.successColor.asBungee())
                .create());
    }

    private static void commandDelete(CommandSender sender, List<String> args) {
        if (!Main.getChatManager().tagExists(args.get(0))) {
            Colors.sendError(sender, "That tag does not exist.");
            return;
        }
        Main.getChatManager().setTagDisplayName(args.get(0), null);
        Colors.sendSuccess(sender,"Deleted tag \"", Main.getChatManager().getColoredTagID(args.get(0)), "\".");
    }

    private static void commandList(CommandSender sender, List<String> args) {
        commandList2(sender, Collections.singletonList("1"));
    }

    private static void commandList2(CommandSender sender, List<String> args) {
        int pageNumber;
        try {
            pageNumber = Integer.parseInt(args.get(0));
        } catch (NumberFormatException e) {
            Colors.sendError(sender, "Invalid page number.");
            return;
        }
        List<String> tagIDs = Main.getChatManager().getTagIDs();
        if (tagIDs == null || tagIDs.size() == 0) {
            Colors.sendError(sender, "No tags have been created yet.");
            return;
        }
        int pageCount = (tagIDs.size() + 9) / 10;
        if (pageNumber <= 0 || pageNumber > pageCount) {
            Colors.sendError(sender,  "Invalid page number (Choose from 1-" + pageCount + ").");
            return;
        }
        boolean canPrev = pageNumber - 1 > 0, canNext = pageNumber + 1 <= pageCount;

        sender.spigot().sendMessage(new ComponentBuilder()
                .append("---- Tag List")
                .bold(true)
                .color(Colors.titleColor.asBungee())
                .append(" ")
                .bold(false)
                .append("[Prev]")
                .color(canPrev ? Colors.regularButtonColor.asBungee() : Colors.darkButtonColor.asBungee())
                .event(canPrev ? new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tags list " + (pageNumber - 1)) : null)
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(canPrev ? "Click to go to the previous page." : "There is no previous page.")))
                .append(" ")
                .event((ClickEvent)null)
                .event((HoverEvent)null)
                .append("[Next]")
                .color(canNext ? Colors.regularButtonColor.asBungee() : Colors.darkButtonColor.asBungee())
                .event(canNext ? new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tags list " + (pageNumber + 1)) : null)
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(canNext ? "Click to go to the next page." : "There is no next page.")))
                .append(" ---- \n")
                .bold(true)
                .color(Colors.titleColor.asBungee())
                .event((ClickEvent)null)
                .event((HoverEvent)null)
                .append("Page ")
                .bold(false)
                .color(Colors.darkColor.asBungee())
                .append("#" + pageNumber + "/" + pageCount)
                .create());

        for (int i = pageNumber * 10 - 10; i < min(pageNumber * 10, tagIDs.size()); i++) {
            String tagID = tagIDs.get(i);
            sender.spigot().sendMessage(new ComponentBuilder()
                    .append((i + 1) + ". ")
                    .color(Colors.regularColor.asBungee())
                    .append(Main.getChatManager().getTagIDComponent(tagID, false))
                    .append(" ")
                    .event((ClickEvent)null)
                    .event((HoverEvent)null)
                    .append(Main.getChatManager().getTagComponent(tagID, false))
                    .create());
        }
    }

    private static void commandModify(CommandSender sender, List<String> args) {
        if (!Main.getChatManager().tagExists(args.get(0))) {
            Colors.sendError(sender, "That tag does not exist.");
            return;
        }
        Main.getChatManager().setTagDisplayName(args.get(0), args.get(1));
        sender.spigot().sendMessage(new ComponentBuilder()
                .append("Changed tag \"")
                .color(Colors.successColor.asBungee())
                .append(Main.getChatManager().getTagIDComponent(args.get(0)))
                .append("\" to \"")
                .event((ClickEvent)null)
                .event((HoverEvent)null)
                .color(Colors.successColor.asBungee())
                .append(Main.getChatManager().getTagComponent(args.get(0)))
                .append("\".")
                .event((ClickEvent)null)
                .event((HoverEvent)null)
                .color(Colors.successColor.asBungee())
                .create());
    }

    private static void commandRemove(CommandSender sender, List<String> args) {
        if (Main.getChatManager().getTagID((Player)sender) == null) {
            Colors.sendError(sender, "You do not have a tag.");
            return;
        }
        Main.getChatManager().setTag((Player)sender, null);
        Colors.sendSuccess(sender, "Your tag has been removed.");
    }

    private static void commandSet(CommandSender sender, List<String> args) {
        if (!Main.getChatManager().tagExists(args.get(0))) {
            Colors.sendError(sender, "That tag does not exist.");
            return;
        }
        Main.getChatManager().setTag((Player)sender, args.get(0));
        sender.spigot().sendMessage(new ComponentBuilder()
                .append("Your tag has been set to")
                .color(Colors.successColor.asBungee())
                .append(Main.getChatManager().getTagComponent((Player)sender))
                .append(".")
                .event((ClickEvent)null)
                .event((HoverEvent)null)
                .color(Colors.successColor.asBungee()).create());
    }

}
