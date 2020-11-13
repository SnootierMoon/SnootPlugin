package snoot.commands.chat;

import snoot.commands.parents.SnootTabCompleter;
import snoot.Main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TagsTabCompleter extends SnootTabCompleter {

    @Override
    protected List<String> onTabComplete(String[] args) {
        if (args.length == 0) {
            return new ArrayList<>();
        } else if (args.length == 1) {
            return Arrays.asList("create", "delete", "list", "modify", "remove", "set");
        } else if (args[0].equals("create")) {
            return new ArrayList<>();
        } else if (args[0].equals("delete")) {
            if (args.length == 2) {
                return Main.getChatManager().getTagIDs();
            }
        } else if (args[0].equals("list")) {
            return new ArrayList<>();
        } else if (args[0].equals("modify")) {
            if (args.length == 2) {
                return Main.getChatManager().getTagIDs();
            }
        } else if (args[0].equals("remove")) {
            return new ArrayList<>();
        } else if (args[0].equals("set")) {
            if (args.length == 2) {
                return Main.getChatManager().getTagIDs();
            }
        }
        return null;
    }

}
