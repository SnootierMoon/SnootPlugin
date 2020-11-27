package snoot.commands.chat;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import org.bukkit.event.Listener;
import snoot.Colors;
import snoot.Main;

public class ChatListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Main.getChatManager().updateNick(event.getPlayer());
        BaseComponent[] baseComponents = new ComponentBuilder()
                .append(Main.getChatManager().getNickComponent(event.getPlayer()))
                .append(" joined the server.")
                .event((ClickEvent)null)
                .event((HoverEvent)null)
                .color(Colors.regularColor.asBungee())
                .create();
        Main.getInstance().getServer().spigot().broadcast(baseComponents);
        Main.getInstance().getServer().getConsoleSender().spigot().sendMessage(baseComponents);
        event.setJoinMessage(null);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        BaseComponent[] baseComponents = new ComponentBuilder()
                .append(Main.getChatManager().getNickComponent(event.getPlayer()))
                .append("")
                .event((ClickEvent)null)
                .event((HoverEvent)null)
                .append(Main.getChatManager().getTagComponent(event.getPlayer()))
                .append(": ")
                .event((ClickEvent)null)
                .event((HoverEvent)null)
                .color(Colors.regularColor.asBungee())
                .append(event.getMessage().startsWith("./") ? event.getMessage().substring(1) : event.getMessage())
                .color(Main.getChatManager().getColor(event.getPlayer()).asBungee())
                .create();
        Main.getInstance().getServer().spigot().broadcast(baseComponents);
        Main.getInstance().getServer().getConsoleSender().spigot().sendMessage(baseComponents);
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        BaseComponent[] baseComponents = new ComponentBuilder()
                .append(Main.getChatManager().getNickComponent(event.getPlayer()))
                .append(" left the server.")
                .event((ClickEvent)null)
                .event((HoverEvent)null)
                .color(Colors.regularColor.asBungee())
                .create();
        Main.getInstance().getServer().spigot().broadcast(baseComponents);
        Main.getInstance().getServer().getConsoleSender().spigot().sendMessage(baseComponents);
        event.setQuitMessage(null);
    }

}
