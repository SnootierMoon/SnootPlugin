package snoot.commands.util;

import snoot.Main;
import snoot.commands.parents.SnootFeatureManager;

public class UtilManager extends SnootFeatureManager {

    public UtilManager() {
        Main.addCommand("colors", new ColorsCommandExecutor(), new ColorsTabCompleter());
        Main.addCommand("near", new NearCommandExecutor(), new NearTabCompleter());
        Main.addListener(new RenameListener());
    }

}
