package snoot.commands.survival;

import org.bukkit.World;
import snoot.Main;
import snoot.commands.parents.SnootFeatureManager;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SurvivalManager extends SnootFeatureManager {

    public SurvivalManager() {
        super("survival_config.yml");
        Main.addCommand("survival", new SurvivalCommandExecutor(), new SurvivalTabCompleter());
    }

    public List<String> getCreativeWorldNames() {
        List<String> worldNames = data.getStringList("creative");
        if (worldNames.isEmpty()) {
            return Arrays.asList("world", "world_nether", "world_the_end", "flatworld");
        }
        return worldNames;
    }

    public List<String> getSurvivalWorldNames() {
        List<String> worldNames = data.getStringList("survival");
        if (worldNames.isEmpty()) {
            return Arrays.asList("survival", "survival_nether", "survival_end");
        }
        return worldNames;
    }

    public List<World> getCreativeWorlds() {
        return getCreativeWorldNames().stream().map(worldName ->  Main.getInstance().getServer().getWorld(worldName)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public List<World> getSurvivalWorlds() {
        return getSurvivalWorldNames().stream().map(worldName ->  Main.getInstance().getServer().getWorld(worldName)).filter(Objects::nonNull).collect(Collectors.toList());
    }

}
