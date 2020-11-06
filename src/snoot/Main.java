package snoot;

import org.bukkit.plugin.java.JavaPlugin;
import snoot.chairs.ChairsManager;
import snoot.chat.ChatManager;

public final class Main extends JavaPlugin {

    private static Main instance;
    private ChatManager chatManager;
    private ChairsManager chairsManager;

    public void onEnable()  {
        instance = this;
        chatManager = new ChatManager();
        chairsManager = new ChairsManager();
    }

    public void onDisable() {
        chatManager.writeFile();
        chairsManager.writeFile();
    }

    public static Main getInstance() {
        return instance;
    }
    public static ChatManager getChatManager() {
        return instance.chatManager;
    }
    public static ChairsManager getChairsManager() { return instance.chairsManager; }

}
