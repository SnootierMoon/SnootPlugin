package snoot.commands.chairs;

import snoot.commands.parents.SnootTabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChairsTabCompleter extends SnootTabCompleter {

    @Override
    protected List<String> onTabComplete(String[] args) {
        if (args.length == 0) {
            return new ArrayList<>();
        }
        if (args.length == 1) {
            return Arrays.asList("off", "on");
        }
        if (args[0].equals("off")) {
            if (args.length == 2) {
                return new ArrayList<>();
            }
        }
        if (args[0].equals("on")) {
            if (args.length == 2) {
                return new ArrayList<>();
            }
        }
        return null;
    }

}
