package snoot.chat;

import snoot.parents.SnootTabCompleter;

import java.util.ArrayList;
import java.util.List;

public class ColorsTabCompleter extends SnootTabCompleter {

    @Override
    protected List<String> onTabComplete(String[] args) {
        if (args.length == 0) {
            return new ArrayList<>();
        }
        return null;
    }
}
