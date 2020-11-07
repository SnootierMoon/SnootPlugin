package snoot.util;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

public class MessageFormat {

    private static final List<String> rainbow = Arrays.asList("&c", "&6", "&e", "&a", "&b", "&d");

    public static String success(String msg) {
        return ChatColor.GREEN + msg + ChatColor.RESET;
    }
    public static String error(String msg) {
        return ChatColor.RED + msg + ChatColor.RESET;
    }
    public static String info(String msg) { return ChatColor.GRAY + msg + ChatColor.RESET; }

    public static String invalidPermissions = error("You do not have permission to do that!");
    public static String playerExclusive = error("Only players can do that!");

    public static String usage = MessageFormat.error("Usage:");
    public static String commandUsage(String name, String args) { return ChatColor.GRAY + "/" + ChatColor.BOLD + name + ChatColor.AQUA + " " + args + ChatColor.RESET; }
    public static String commandHelp(String name, String args, String help) { return commandUsage(name, args) + ChatColor.GREEN + help + ChatColor.RESET; }

    public static String coloredColor(ChatColor color) { return color + color.name().toLowerCase() + ChatColor.RESET; }

    public static String rainbowFormat(String message) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            result.append(rainbow.get(i % 6));
            result.append(message.charAt(i));
        }
        return result.toString();
    }

}
