package snoot;

import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import snoot.commands.chairs.ChairsManager;
import snoot.commands.chat.ChatManager;
import snoot.commands.parents.SnootCommandExecutor;
import snoot.commands.parents.SnootTabCompleter;
import snoot.commands.survival.SurvivalManager;
import snoot.commands.util.UtilManager;

public final class Main extends JavaPlugin {

    private static Main instance;
    private ChatManager chatManager;
    private ChairsManager chairsManager;
    private SurvivalManager survivalManager;
    private UtilManager utilManager;

    @Override
    public void onEnable()  {
        instance = this;
        chatManager = new ChatManager();
        chairsManager = new ChairsManager();
        survivalManager = new SurvivalManager();
        utilManager = new UtilManager();
    }

    @Override
    public void onDisable() {
        chatManager.writeFile();
        chairsManager.writeFile();
        survivalManager.writeFile();
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
    public static ChatManager getChatManager() {
        return instance.chatManager;
    }
    public static ChairsManager getChairsManager() { return instance.chairsManager; }
    public static SurvivalManager survivalManager() { return instance.survivalManager; }

}
