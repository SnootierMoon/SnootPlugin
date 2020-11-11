package snoot.chat;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import org.bukkit.event.Listener;
import snoot.Main;

import java.util.ArrayList;
import java.util.List;

public class ChatListener implements Listener {

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        Main.getChatManager().updateNick(event.getPlayer());
        String nick = Main.getChatManager().getNick(event.getPlayer());
        BaseComponent baseComponent = new TextComponent(nick + ChatColor.GRAY + " joined the server.");
        baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(nick + ChatColor.DARK_GRAY + " AKA " + ChatColor.GRAY + event.getPlayer().getName())));
        Bukkit.spigot().broadcast(baseComponent);
        event.setJoinMessage(null);
    }

    @EventHandler
    public void onChatPlayer(final AsyncPlayerChatEvent event) {
        String tag = Main.getChatManager().getPlayerTagName(event.getPlayer());
        String nick = Main.getChatManager().getNick(event.getPlayer());
        List<BaseComponent> baseComponents = new ArrayList<>();
        BaseComponent baseComponent = new TextComponent(nick);
        baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(nick + ChatColor.DARK_GRAY + " AKA " + ChatColor.GRAY + event.getPlayer().getName())));
        baseComponents.add(baseComponent);
        if (tag != null) {
            baseComponents.add(new TextComponent(ChatColor.DARK_GRAY + " [" + tag + ChatColor.DARK_GRAY + "]"));
        }
        baseComponents.add(new TextComponent(ChatColor.GRAY + ": "));
        if (event.getMessage().startsWith("./")) {
            event.setMessage(event.getMessage().substring(1));
        }
        baseComponent = new TextComponent(event.getMessage());
        baseComponent.setColor(Main.getChatManager().getColor(event.getPlayer()).asBungee());
        baseComponents.add(baseComponent);
        Bukkit.spigot().broadcast(baseComponents.toArray(new BaseComponent[0]));
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        String nick = Main.getChatManager().getNick(event.getPlayer());
        BaseComponent baseComponent = new TextComponent(nick + ChatColor.GRAY + " left the server.");
        baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(nick + ChatColor.DARK_GRAY + " AKA " + ChatColor.GRAY + event.getPlayer().getName())));
        Bukkit.spigot().broadcast(baseComponent);
        event.setQuitMessage(null);
    }

}
