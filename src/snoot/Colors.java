package snoot;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Colors {

    public static final List<ChatColor> colorList = Arrays.stream(ChatColor.values()).filter(ChatColor::isColor).collect(Collectors.toList());
    public static final List<ChatColor> formatList = Arrays.stream(ChatColor.values()).filter(color -> !color.isColor()).collect(Collectors.toList());

    public static final ChatColor titleColor = ChatColor.GOLD;
    public static final ChatColor titleColorFormat = ChatColor.BOLD;
    public static final String title = titleColor.toString() + titleColorFormat;

    public static final ChatColor commandColor = ChatColor.AQUA;
//    public static final String command = commandColor.toString();

    public static final ChatColor errorColor = ChatColor.RED;
    public static final String error = errorColor.toString();
    public static final ChatColor successColor = ChatColor.GREEN;
    public static final String success = successColor.toString();

    public static final ChatColor darkColor = ChatColor.DARK_GRAY;
    public static final String dark = darkColor.toString();
    public static final ChatColor regularColor = ChatColor.GRAY;
    public static final String regular = regularColor.toString();

    public static final ChatColor darkButtonColor = ChatColor.DARK_GRAY;
//    public static final String darkButton = darkButtonColor.toString();
    public static final ChatColor regularButtonColor = ChatColor.GREEN;
//    public static final String regularButton = regularButtonColor.toString();


    public static String coloredColor(ChatColor color) { return color + color.name().toLowerCase(); }

    public static void sendColor(CommandSender sender, String color, String... messages) {
        sender.sendMessage(Arrays.stream(messages).map(message -> color + message).collect(Collectors.joining()));
    }

    public static void sendTitle(CommandSender sender, String... messages) {
        sendColor(sender, title, messages);
    }

    public static void sendSubTitle(CommandSender sender, String... messages) { sendColor(sender, titleColor.toString(), messages); }

    public static void sendSuccess(CommandSender sender, String... messages) {
        sendColor(sender, success, messages);
    }

    public static void sendError(CommandSender sender, String... messages) {
        sendColor(sender, error, messages);
    }

    public static void sendInfo(CommandSender sender, String... messages) {
        sendColor(sender, regular, messages);
    }

    private static String translateColorChar(char character) {
        ChatColor color = ChatColor.getByChar(character);
        if (color != null) {
            return color.toString();
        }
        switch (character) {
            case '&': return "&";
            case '_': return " ";
        }
        return "";
    }

    private static int translateColorCharLength(char character) {
        ChatColor color = ChatColor.getByChar(character);
        if (color != null) {
            return 0;
        }
        switch (character) {
            case '&':
            case '_': return 1;
        }
        return 0;
    }


    public static String translateAlternateColorCodes(String colorCodes) {
        int index = 0;
        StringBuilder text = new StringBuilder();
        while (true) {
            index = colorCodes.indexOf('&', index);
            if (index == -1) {
                return text + colorCodes;
            }
            if (index == colorCodes.length() - 1) {
                return text + colorCodes.substring(0, colorCodes.length() - 1);
            }
            text.append(colorCodes, 0, index).append(translateColorChar(colorCodes.charAt(index + 1)));
            colorCodes = colorCodes.substring(index + 2);
        }
    }

    public static int translateAlternateColorCodesLength(String colorCodes) {
        int index = 0;
        int length = 0;
        while (true) {
            index = colorCodes.indexOf('&', index);
            if (index == -1) {
                return length + colorCodes.length();
            }
            if (index == colorCodes.length() - 1) {
                return length + colorCodes.length() - 1;
            }
            length += index + translateColorCharLength(colorCodes.charAt(index + 1));
            colorCodes = colorCodes.substring(index + 2);
        }
    }

}
