package snoot.chairs;

import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import snoot.Main;
import snoot.SnootFeatureManager;
import snoot.chat.ChatcolorCommandExecutor;
import snoot.chat.ChatcolorTabCompleter;

public class ChairsManager extends SnootFeatureManager {

    public ChairsManager() {
        super("chairs_config.yml");
        PluginCommand chairsCommand = Main.getInstance().getCommand("chairs");
        if (chairsCommand == null) {
            Main.getInstance().getLogger().info("Internal error: Failed to find \"chatcolor\" command.");
        } else {
            chairsCommand.setExecutor(new ChairsCommandExecutor());
            chairsCommand.setTabCompleter(new ChairsTabCompleter());
        }
        Main.getInstance().getServer().getPluginManager().registerEvents(new ChairsListener(), Main.getInstance());
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
