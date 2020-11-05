package snoot.chat;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import snoot.Main;
import snoot.MessageFormat;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.min;

public class TagsCommandExecutor implements CommandExecutor {

    private final static String createUsage = MessageFormat.command("tags", "create TAG-ID");
    private final static String deleteUsage = MessageFormat.command("tags", "delete <tag-id>");
    private final static String listUsage = MessageFormat.command("tags", "list [page]");
    private final static String modifyUsage = MessageFormat.command("tags", "modify <tag-id> <display-name>");
    private final static String removeUsage = MessageFormat.command("tags", "remove");
    private final static String setUsage = MessageFormat.command("tags", "set <tag-id>");

    private final static String fullUsage = String.join("\n", createUsage, deleteUsage, listUsage, modifyUsage, removeUsage, setUsage);

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull final Command command, @Nonnull final String label, @Nonnull final String[] args) {
        if (args.length == 0) {
            sender.sendMessage(MessageFormat.usage);
            sender.sendMessage(fullUsage);
        } else if (!sender.hasPermission("snoot.chat.tags")) {
            sender.sendMessage(MessageFormat.invalidPermissions);
        } else if (args[0].equals("create")) {
            if (args.length != 2) {
                sender.sendMessage(MessageFormat.usage);
                sender.sendMessage(createUsage);
            } else if (Main.getChatManager().tagExists(args[1])) {
                sender.sendMessage(MessageFormat.error("That tag already exists."));
            } else {
                Main.getChatManager().setTagName(args[1], MessageFormat.info("Default"));
                sender.sendMessage(MessageFormat.success("Created tag \"") + MessageFormat.info(args[1]) + MessageFormat.success("\"."));
            }
        } else if (args[0].equals("delete")) {
            if (args.length != 2) {
                sender.sendMessage(MessageFormat.usage);
                sender.sendMessage(deleteUsage);
            } else if (!Main.getChatManager().tagExists(args[1])) {
                sender.sendMessage(MessageFormat.error("That tag does not exist."));
            } else {
                Main.getChatManager().setTagName(args[1], null);
                sender.sendMessage(MessageFormat.success("Deleted tag \"") + MessageFormat.info(args[1]) + MessageFormat.success("\"."));
            }
        } else if (args[0].equals("list")) {
            if (args.length > 2) {
                sender.sendMessage(MessageFormat.usage);
                sender.sendMessage(listUsage);
            } else try {
                int pageNumber = args.length == 1 ? 1 : Integer.parseInt(args[1]);
                List<String> tagIDs = Main.getChatManager().getTagIDs();
                if (tagIDs == null || tagIDs.size() == 0) {
                    sender.sendMessage(MessageFormat.info("No tags have been created yet."));
                } else {
                    int pageCount = (tagIDs.size() + 9) / 10;
                    if (pageNumber <= 0) {
                        sender.sendMessage(MessageFormat.error("Invalid page number (Choose from 0-" + pageCount + ")."));
                    } else if (pageNumber > pageCount) {
                        sender.sendMessage(MessageFormat.error("Page number out of range (Choose from 0-" + pageCount + ")."));
                    } else {
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
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(MessageFormat.error("Invalid page number."));
            }
        } else if (args[0].equals("modify")) {
            if (args.length != 3) {
                sender.sendMessage(MessageFormat.usage);
                sender.sendMessage(modifyUsage);
            } else if (!Main.getChatManager().tagExists(args[1])) {
                sender.sendMessage(MessageFormat.error("That tag does not exist."));
            } else {
                String tagName = ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', args[2]) + ChatColor.RESET;
                Main.getChatManager().setTagName(args[1], tagName);
                sender.sendMessage(MessageFormat.success("Set tag \"") + MessageFormat.info(args[1]) + MessageFormat.success("\" to \"") + tagName + MessageFormat.success("\"."));
            }
        } else if (args[0].equals("remove")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(MessageFormat.playerExclusive);
            } else if (args.length != 1) {
                sender.sendMessage(MessageFormat.usage);
                sender.sendMessage(removeUsage);
            } else if (Main.getChatManager().getPlayerTagID((Player)sender) == null) {
                sender.sendMessage(MessageFormat.error("You do not have a tag."));
            } else {
                Main.getChatManager().setPlayerTagID((Player)sender, null);
                sender.sendMessage(MessageFormat.success("Your tag has been removed."));
            }
        } else if (args[0].equals("set")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(MessageFormat.playerExclusive);
            } else if (args.length != 2) {
                sender.sendMessage(MessageFormat.usage);
                sender.sendMessage(setUsage);
            } else if (!Main.getChatManager().tagExists(args[1])) {
                sender.sendMessage(MessageFormat.error("That tag does not exist."));
            } else {
                Main.getChatManager().setPlayerTagID((Player)sender, args[1]);
                sender.sendMessage(MessageFormat.success("Your tag has been set to ") + Main.getChatManager().getPlayerTagName((Player)sender) + MessageFormat.success("."));
            }
        } else {
            sender.sendMessage(MessageFormat.usage);
            sender.sendMessage(fullUsage);
        }
        return true;
    }
}
