package snoot.commands.chairs;

import org.bukkit.entity.Player;
import snoot.Main;
import snoot.commands.parents.SnootFeatureManager;

public class ChairsManager extends SnootFeatureManager {

    public ChairsManager() {
        super("chairs_config.yml");
        Main.addCommand("chairs", new ChairsCommandExecutor(), new ChairsTabCompleter());
        Main.addListener(new ChairsListener());
    }

    public void enableChairs(Player player) {
        data.set(player.getUniqueId().toString(), true);
    }

    public void disableChairs(Player player) {
        data.set(player.getUniqueId().toString(), false);
    }

    public boolean chairsEnabled(Player player) {
        return data.getBoolean(player.getUniqueId().toString());
    }

}
