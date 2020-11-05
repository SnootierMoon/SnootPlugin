package snoot.chat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import snoot.Helper;
import snoot.MessageFormat;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NickTabCompleter implements TabCompleter {

    private static final String digBicks = MessageFormat.rainbowFormat("B1gD1ckz69");

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        return Helper.filterPrefix(onTabComplete(args), args[args.length - 1]);
    }

    private List<String> onTabComplete(String[] args) {
        if (args.length == 0) {
            return new ArrayList<>();
        } else if (args.length == 1) {
            return Arrays.asList("?", "remove", "set");
        } else if (args[0].equals("remove")) {
            if (args.length == 2) {
                return Collections.singletonList("?");
            }
        } else if (args[0].equals("set")) {
            if (args.length == 2) {
                return Arrays.asList("?", digBicks);
            }
        }
        return new ArrayList<>();
    }

}
