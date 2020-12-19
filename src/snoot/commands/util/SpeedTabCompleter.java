package snoot.commands.util;

import snoot.commands.parents.SnootTabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpeedTabCompleter extends SnootTabCompleter   {
    @Override
    protected List<String> onTabComplete(String[] args) {
        if (args.length == 0) {
            return new ArrayList<>();
        } else if (args.length == 1) {
            return Arrays.asList("4.20", "6.9", "reset");
        } else if (args.length == 2) {
            return Arrays.asList("both", "fly", "walk");
        }
        return null;
    }
}
