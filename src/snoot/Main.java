package snoot;

import org.bukkit.WorldCreator;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import snoot.commands.chairs.ChairsManager;
import snoot.commands.chat.ChatManager;
import snoot.commands.parents.SnootCommandExecutor;
import snoot.commands.parents.SnootFeatureManager;
import snoot.commands.parents.SnootTabCompleter;
import snoot.commands.util.UtilManager;

import java.util.ArrayList;
import java.util.List;

public final class Main extends JavaPlugin {

    private static Main instance;
    private List<SnootFeatureManager> managers;

    private void enable() {
        instance = this;
        managers = new ArrayList<>();
        managers.add(new ChatManager());
        managers.add(new ChairsManager());
        managers.add(new UtilManager());
    }

    private void disable() {
        if (managers == null) {
            return;
        }
        for (SnootFeatureManager manager : managers) {
            manager.close();
        }
        managers.clear();
        instance = null;
    }

    @Override
    public void onEnable()  {
        disable();
        enable();
        getServer().createWorld(new WorldCreator("world"));
    }

    @Override
    public void onDisable() {
        disable();
    }

    public static void addCommand(String commandName, SnootCommandExecutor commandExecutor, SnootTabCompleter tabCompleter) {
        PluginCommand command = instance.getCommand(commandName);
        if (command == null) {
            instance.getLogger().warning("Failed to find command: \"" + commandName + "\".");
        } else {
            command.setExecutor(commandExecutor);
            command.setTabCompleter(tabCompleter);
        }
    }

    public static void addListener(Listener listener) {
        instance.getServer().getPluginManager().registerEvents(listener, instance);
    }

    public static Main getInstance() {
        return instance;
    }
    public static ChatManager getChatManager() { return (ChatManager)instance.managers.stream().filter(manager -> manager instanceof ChatManager).findFirst().orElse(null); }
    public static ChairsManager getChairsManager() { return (ChairsManager)instance.managers.stream().filter(manager -> manager instanceof ChairsManager).findFirst().orElse(null); }

}
