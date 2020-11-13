package snoot.commands.util;

import snoot.commands.parents.SnootTabCompleter;

import java.util.ArrayList;
import java.util.List;

public class NearTabCompleter extends SnootTabCompleter {

    @Override
    protected List<String> onTabComplete(String[] args) {
        if (args.length == 0) {
            return new ArrayList<>();
        }
        return null;
    }
}
