package snoot.commands.survival;

import snoot.commands.parents.SnootTabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SurvivalTabCompleter extends SnootTabCompleter {

    @Override
    protected List<String> onTabComplete(String[] args) {
        if (args.length == 0) {
            return new ArrayList<>();
        } else if (args.length == 1) {
            return Arrays.asList("join", "leave");
        } else if (args[0].equals("join")) {
            return new ArrayList<>();
        } else if (args[0].equals("leave")) {
            return new ArrayList<>();
        } else if (args[0].equals("list")) {
            return new ArrayList<>();
        }
        return null;
    }

}
