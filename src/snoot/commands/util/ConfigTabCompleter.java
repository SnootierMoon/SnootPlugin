package snoot.commands.util;

import org.bukkit.World;
import snoot.Main;
import snoot.commands.parents.SnootTabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigTabCompleter extends SnootTabCompleter {

    @Override
    protected List<String> onTabComplete(String[] args) {
        if (args.length == 0) {
            return new ArrayList<>();
        } else if (args.length == 1) {
            return Arrays.asList("set-option", "world-type");
        } else if (args[0].equals("world-type")) {
            if (args.length == 2) {
                return Main.getInstance().getServer().getWorlds().stream().map(World::getName).collect(Collectors.toList());
            } if (args.length == 3) {
                return Arrays.asList("creative", "none", "survival");
            }
        }
        return null;
    }
}
