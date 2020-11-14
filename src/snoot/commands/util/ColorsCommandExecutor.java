package snoot.commands.util;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import snoot.Colors;
import snoot.commands.parents.SnootCommandExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ColorsCommandExecutor extends SnootCommandExecutor {

    private static final String colorCodeList = Colors.colorList.stream().map(color -> color + "&" + color.getChar()).collect(Collectors.joining(" "));
    private static final String formatList = Stream.concat(Colors.formatList.stream().map(color -> Colors.regular + "&" + color.getChar() + ": " + ChatColor.WHITE + color.toString() + color.name().toLowerCase()),
            Stream.of(Colors.regular + "&&: " + ChatColor.DARK_GRAY + "<& symbol>" , Colors.regular + "&_: " + ChatColor.DARK_GRAY + "<empty space>")
    ).collect(Collectors.joining("\n"));

    ColorsCommandExecutor() {
        super("group of commands for listing and testing the color codes", "snoot.colors", false);
        addSubCommand(new SubCommand(
                "list",
                new ArrayList<>(),
                "list all color codes",
                null,
                Collections.singletonMap(0, ColorsCommandExecutor::commandList)));
        addSubCommand(new SubCommand(
                "test",
                Collections.singletonList(new HelpArg("test-name", "the name to test", "see /colors list")),
                "test a color coded name",
                null,
                Collections.singletonMap(1, ColorsCommandExecutor::commandTest),
                true));
    }

    private static void commandList(CommandSender sender, List<String> args) {
        Colors.sendTitle(sender, "---- Color Codes ----");
        Colors.sendSubTitle(sender, "Colors:");
        Colors.sendInfo(sender, colorCodeList);
        Colors.sendSubTitle(sender, "Formats:");
        Colors.sendInfo(sender, formatList);
    }

    private static void commandTest(CommandSender sender, List<String> args) {
        sender.spigot().sendMessage(new ComponentBuilder()
                .append(new TextComponent(args.get(0).length() + "->" + Colors.translateAlternateColorCodesLength(args.get(0)) + " characters"))
                .color(Colors.titleColor.asBungee())
                .append(new TextComponent(": \""))
                .color(Colors.darkColor.asBungee())
                .append(new TextComponent(Colors.translateAlternateColorCodes(args.get(0))))
                .event(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, args.get(0)))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Colors.regular + "Click to copy to clipboard")))
                .append(new TextComponent("\""))
                .color(Colors.darkColor.asBungee())
                .event((ClickEvent)null)
                .event((HoverEvent) null)
                .create());
    }

}
