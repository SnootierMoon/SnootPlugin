package snoot.chat;

import snoot.parents.SnootTabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NickTabCompleter extends SnootTabCompleter {

    @Override
    protected List<String> onTabComplete(String[] args) {
        if (args.length == 0) {
            return new ArrayList<>();
        }
        if (args.length == 1) {
            return Arrays.asList("remove", "set");
        }
        if (args[0].equals("remove")) {
            if (args.length == 2) {
                return new ArrayList<>();
            }
        }
        if (args[0].equals("set")) {
            if (args.length == 2) {
                return new ArrayList<>();
            }
        }
        return null;
    }

}
