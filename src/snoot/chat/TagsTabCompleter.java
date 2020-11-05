package snoot.chat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import snoot.Helper;
import snoot.Main;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TagsTabCompleter implements TabCompleter {


    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        return Helper.filterPrefix(onTabComplete(args), args[args.length - 1]);
    }

    private List<String> onTabComplete(String[] args) {
        if (args.length == 0) {
            return new ArrayList<>();
        } else if (args.length == 1) {
            return Arrays.asList("?", "create", "delete", "list", "modify", "remove", "set");
        } else if (args[0].equals("create")) {
            return Collections.singletonList("?");
        } else if (args[0].equals("delete")) {
            if (args.length == 2) {
                List<String> tagIDs = Main.getChatManager().getTagIDs();
                if (tagIDs == null) {
                    return Collections.singletonList("?");
                }
                tagIDs.add(0, "?");
                return tagIDs;
            }
        } else if (args[0].equals("list")) {
            return Collections.singletonList("?");
        } else if (args[0].equals("modify")) {
            if (args.length == 2) {
                List<String> tagIDs = Main.getChatManager().getTagIDs();
                if (tagIDs == null) {
                    return Collections.singletonList("?");
                }
                tagIDs.add(0, "?");
                return tagIDs;
            }
        } else if (args[0].equals("remove")) {
            return Collections.singletonList("?");
        } else if (args[0].equals("set")) {
            if (args.length == 2) {
                List<String> tagIDs = Main.getChatManager().getTagIDs();
                if (tagIDs == null) {
                    return Collections.singletonList("?");
                }
                tagIDs.add(0, "?");
                return tagIDs;
            }
        }
        return Collections.singletonList("?");
    }

}
