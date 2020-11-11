package snoot.survival;

import snoot.Main;
import snoot.parents.SnootFeatureManager;

import java.util.Arrays;

public class SurvivalManager extends SnootFeatureManager {

    public SurvivalManager() {
        super("survival.yml");
        Main.addCommand("survival", new SurvivalCommandExecutor(), new SurvivalTabCompleter());
        data.set("survival", Arrays.asList("survival", "survival_nether", "survival_end"));
        data.set("creative", Arrays.asList("world", "world_nether", "world_the_end", "flatworld"));
    }

}
