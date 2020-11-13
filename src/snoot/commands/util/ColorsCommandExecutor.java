package snoot.commands.util;

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
        super(new ArrayList<>(), "list all color codes", "snoot.colors", Collections.singletonMap(0, ColorsCommandExecutor::commandColors), false);
    }

    private static void commandColors(CommandSender sender, List<String> args) {
        Colors.sendTitle(sender, "---- Color Codes ----");
        Colors.sendSubTitle(sender, "Colors:");
        Colors.sendInfo(sender, colorCodeList);
        Colors.sendSubTitle(sender, "Formats:");
        Colors.sendInfo(sender, formatList);
    }

}
