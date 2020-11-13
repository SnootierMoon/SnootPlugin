package snoot.commands.parents;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class SnootTabCompleter implements TabCompleter {

    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        List<String> suggestions = onTabComplete(args);
        if (suggestions == null) {
            return new ArrayList<>();
        }
        return Stream.concat(Stream.of("?"), suggestions.stream()).filter(suggestion -> suggestion.startsWith(args[args.length - 1])).collect(Collectors.toList());
    }

    protected abstract List<String> onTabComplete(String[] args);

}
