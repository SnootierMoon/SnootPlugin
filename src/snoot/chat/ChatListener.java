package snoot.chat;
import org.bukkit.ChatColor;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import org.bukkit.event.Listener;
import snoot.Main;
import snoot.MessageFormat;

public class ChatListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        Main.getChatManager().updateNick(player);
        event.setJoinMessage(player.getDisplayName() + MessageFormat.info(" joined the server."));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChatPlayer(AsyncPlayerChatEvent event) {
        String tag = Main.getChatManager().getPlayerTagName(event.getPlayer());
        tag = ((tag == null) ? (ChatColor.DARK_GRAY + ":") : (" " + ChatColor.DARK_GRAY + "[" + tag + ChatColor.DARK_GRAY + "]:")) + ChatColor.RESET;
        event.setFormat("%1$s" + tag + Main.getChatManager().getColor(event.getPlayer()) + " %2$s");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        event.setQuitMessage(player.getDisplayName() + MessageFormat.info(" left the server."));
    }
}
