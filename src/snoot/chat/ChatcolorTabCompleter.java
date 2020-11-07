package snoot.chat;

import org.bukkit.ChatColor;
import snoot.parents.SnootTabCompleter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ChatcolorTabCompleter extends SnootTabCompleter {

    private final static List<String> chatColorListRaw = ChatManager.chatColorList.stream().map(ChatColor::stripColor).collect(Collectors.toList());

    @Override
    protected List<String> onTabComplete(String[] args) {
        if (args.length == 0) {
            return null;
        } else if (args.length == 1) {
            return Arrays.asList("list", "remove", "set");
        } else if (args[0].equals("list")) {
            if (args.length == 2) {
                return null;
            }
        } else if (args[0].equals("remove")) {
            if (args.length == 2) {
                return null;
            }
        } else if (args[0].equals("set")) {
            if (args.length == 2) {
                return chatColorListRaw;
            }
        }
        return null;
    }

}
