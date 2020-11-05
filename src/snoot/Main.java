package snoot;

import org.bukkit.plugin.java.JavaPlugin;
import snoot.chat.ChatManager;

public final class Main extends JavaPlugin {

    private static Main instance;
    private ChatManager chatManager;

    public void onEnable()  {
        instance = this;
        chatManager = new ChatManager();
    }

    public void onDisable() {
        chatManager.writeFile();
    }

    public static Main getInstance() {
        return instance;
    }

    public static ChatManager getChatManager() {
        return instance.chatManager;
    }

}
