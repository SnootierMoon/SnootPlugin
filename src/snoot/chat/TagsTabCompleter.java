package snoot.chat;

import snoot.parents.SnootTabCompleter;
import snoot.Main;

import java.util.Arrays;
import java.util.List;

public class TagsTabCompleter extends SnootTabCompleter {

    @Override
    protected List<String> onTabComplete(String[] args) {
        if (args.length == 0) {
            return null;
        } else if (args.length == 1) {
            return Arrays.asList("create", "delete", "list", "modify", "remove", "set");
        } else if (args[0].equals("create")) {
            return null;
        } else if (args[0].equals("delete")) {
            if (args.length == 2) {
                return Main.getChatManager().getTagIDs();
            }
        } else if (args[0].equals("list")) {
            return null;
        } else if (args[0].equals("modify")) {
            if (args.length == 2) {
                return Main.getChatManager().getTagIDs();
            }
        } else if (args[0].equals("remove")) {
            return null;
        } else if (args[0].equals("set")) {
            if (args.length == 2) {
                return Main.getChatManager().getTagIDs();
            }
        }
        return null;
    }

}
