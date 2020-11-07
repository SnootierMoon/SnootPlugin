package snoot.chairs;

import snoot.parents.SnootTabCompleter;

import java.util.Arrays;
import java.util.List;

public class ChairsTabCompleter extends SnootTabCompleter {

    @Override
    protected List<String> onTabComplete(String[] args) {
        if (args.length == 0) {
            return null;
        } else if (args.length == 1) {
            return Arrays.asList("off", "on");
        } else if (args[0].equals("off")) {
            if (args.length == 2) {
                return null;
            }
        } else if (args[0].equals("on")) {
            if (args.length == 2) {
                return null;
            }
        }
        return null;
    }

}
