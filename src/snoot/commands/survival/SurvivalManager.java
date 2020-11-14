package snoot.commands.survival;

import org.bukkit.World;
import snoot.Main;
import snoot.commands.parents.SnootFeatureManager;

import java.util.List;
import java.util.stream.Collectors;

public class SurvivalManager extends SnootFeatureManager {

    public enum WorldType {
        CREATIVE,
        NONE,
        SURVIVAL
    }

    public SurvivalManager() {
        super("survival_config.yml");
        Main.addCommand("survival", new SurvivalCommandExecutor(), new SurvivalTabCompleter());
        Main.getInstance().getServer().getWorlds().get(0).getUID();
    }

    public WorldType setWorldType(World world, String type) {
        WorldType worldType;
        if (type == null) {
            data.set(world.getUID().toString(), null);
            return null;
        }
        try {
            worldType = WorldType.valueOf(type.toUpperCase());
        } catch (java.lang.IllegalArgumentException e) {
            return null;
        }
        data.set(world.getUID().toString(), worldType.name());
        return worldType;
    }

    public WorldType getWorldType(World world) {
        WorldType worldType;
        try {
            String string = data.getString(world.getUID().toString());
            if (string == null) {
                return WorldType.NONE;
            }
            worldType = WorldType.valueOf(string);
        } catch (java.lang.IllegalArgumentException e) {
            return WorldType.NONE;
        }
        Main.getInstance().getLogger().info(world.getName() + " = " + worldType.name());
        return worldType;
    }

    public List<World> getWorlds(WorldType worldType) {
        return Main.getInstance().getServer().getWorlds().stream().filter(world -> getWorldType(world) == worldType).collect(Collectors.toList());
    }

}
