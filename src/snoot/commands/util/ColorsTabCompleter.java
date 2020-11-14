package snoot.commands.util;

import snoot.commands.parents.SnootTabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ColorsTabCompleter extends SnootTabCompleter {

    @Override
    protected List<String> onTabComplete(String[] args) {
        if (args.length == 0) {
            return new ArrayList<>();
        } if (args.length == 1) {
            return Arrays.asList("list", "test");
        }
        return null;
    }
}
