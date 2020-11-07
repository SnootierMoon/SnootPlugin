package snoot.chat;

import snoot.parents.SnootTabCompleter;
import snoot.util.MessageFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NickTabCompleter extends SnootTabCompleter {

    private static final String digBicks = MessageFormat.rainbowFormat("B1gD1ckz69");

    @Override
    protected List<String> onTabComplete(String[] args) {
        if (args.length == 0) {
            return new ArrayList<>();
        } else if (args.length == 1) {
            return Arrays.asList("remove", "set");
        } else if (args[0].equals("remove")) {
            if (args.length == 2) {
                return new ArrayList<>();
            }
        } else if (args[0].equals("set")) {
            if (args.length == 2) {
                return Collections.singletonList(digBicks);
            }
        }
        return new ArrayList<>();
    }

}
