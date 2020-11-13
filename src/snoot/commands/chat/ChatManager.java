package snoot.commands.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import snoot.Colors;
import snoot.Main;
import snoot.commands.parents.SnootFeatureManager;

public class ChatManager extends SnootFeatureManager {

    public ChatManager() {
        super("chat_config.yml");
        Main.addCommand("chatcolor", new ChatcolorCommandExecutor(), new ChatcolorTabCompleter());
        Main.addCommand("nick", new NickCommandExecutor(), new NickTabCompleter());
        Main.addCommand("tags", new TagsCommandExecutor(), new TagsTabCompleter());
        Main.addListener(new ChatListener());
    }

    public boolean hasNick(Player player) {
        return getNick(player) != null;
    }

    public String getNick(Player player) {
        String nick = data.getString(player.getUniqueId().toString() + ".nick");
        if (nick == null) {
          return Colors.regular + player.getName();
        }
        return nick;
    }

    public void setNick(Player player, String nick) {
        if (nick == null) {
            data.set(player.getUniqueId().toString() + ".nick", null);
            updateNick(player);
            return;
        }
        data.set(player.getUniqueId().toString() + ".nick", ChatColor.GRAY + Colors.translateAlternateColorCodes(nick));
        updateNick(player);
    }

    public void updateNick(Player player) {
        String nick = getNick(player);
        player.setDisplayName(nick);
        player.setPlayerListName(nick);
    }

    public boolean hasColor(Player player) {
        return data.getString(player.getUniqueId().toString() + ".color") != null;
    }

    public ChatColor getColor(Player player) {
        String color = data.getString(player.getUniqueId().toString() + ".color");
        if (color == null) {
            return ChatColor.WHITE;
        }
        return ChatColor.getByChar(color);
    }

    public ChatColor setColor(Player player, String color) {
        ChatColor chatColor;
        if (color == null) {
            data.set(player.getUniqueId().toString() + ".color", null);
            return null;
        }
        try {
            chatColor = ChatColor.valueOf(color.toUpperCase());
        } catch (java.lang.IllegalArgumentException e) {
            return null;
        }
        if (!chatColor.isColor()) {
            return null;
        }
        data.set(player.getUniqueId().toString() + ".color", chatColor.getChar());
        return chatColor;
    }

    public List<String> getTagIDs() {
        ConfigurationSection tags = data.getConfigurationSection("tags");
        if (tags != null) {
            return new ArrayList<>(tags.getKeys(true));
        }
        return null;
    }

    public boolean tagExists(String tagID) {
        return getTagDisplayName(tagID) != null;
    }

    public void setTagDisplayName(String tagID, String name) {
        if (name == null) {
            data.set("tags." + tagID, null);
            return;
        }
        data.set("tags." + tagID, ChatColor.GRAY + Colors.translateAlternateColorCodes(name));
    }

    public String getTagDisplayName(String tagID) {
        return data.getString("tags." + tagID);
    }

    public String getTagBracketedDisplayName(String tagID) {
        return Colors.dark + "[" + Objects.toString(getTagDisplayName(tagID), "") + Colors.dark + "]";
    }

    public boolean hasTag(Player player) {
        return getTagID(player) != null;
    }

    public String getTagID(Player player) {
        String tagID = data.getString(player.getUniqueId().toString() + ".tag");
        if (tagID == null) {
            setTag(player, null);
        }
        return tagID;
    }

    public void setTag(Player player, String tagID) {
        data.set(player.getUniqueId().toString() + ".tag", tagID);
    }

    public String getColoredTagID(String tagID) { return Colors.regular + tagID; }

    public BaseComponent[] getTagIDComponent(String tagID) { return getTagIDComponent(tagID, true); }

    public BaseComponent[] getTagIDComponent(String tagID, boolean suggest) {
        return new ComponentBuilder()
                .append(tagID)
                .color(Colors.regularColor.asBungee())
                .event(new ClickEvent(suggest ? ClickEvent.Action.SUGGEST_COMMAND : ClickEvent.Action.RUN_COMMAND, "/tags set " + tagID))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Colors.darkColor + getTagBracketedDisplayName(tagID) + Colors.darkColor + "]")))
                .create();
    }

    public BaseComponent[] getTagComponent(Player player) {
        if (!hasTag(player)) {
            return new ComponentBuilder("").create();
        }
        return new ComponentBuilder(" ")
            .append(getTagComponent(player, true)).create(); }

    public BaseComponent[] getTagComponent(Player player, boolean suggest) {
        return getTagComponent(getTagID(player), suggest);
    }

    public BaseComponent[] getTagComponent(String tagID) { return getTagComponent(tagID, true); }

    public BaseComponent[] getTagComponent(String tagID, boolean suggest) {
        return new ComponentBuilder()
                .append("[")
                .event(new ClickEvent(suggest ? ClickEvent.Action.SUGGEST_COMMAND : ClickEvent.Action.RUN_COMMAND, "/tags set " + tagID))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(suggest ? Colors.dark + "ID: " + getColoredTagID(tagID) : Colors.regular + "Click to set your tag to " + getTagBracketedDisplayName(tagID) + Colors.regular + ".")))
                .color(Colors.darkColor.asBungee())
                .append(Objects.toString(getTagDisplayName(tagID), ""))
                .append("]")
                .color(Colors.darkColor.asBungee()).create();
    }

    public BaseComponent[] getNickComponent(Player player) { return getNickComponent(player, true); }

    public BaseComponent[] getNickComponent(Player player, boolean suggest) {
        String nick = getNick(player);
        return new ComponentBuilder()
                .append(nick)
                .event(new ClickEvent(suggest ? ClickEvent.Action.SUGGEST_COMMAND : ClickEvent.Action.RUN_COMMAND, "/msg " + player.getName() + " "))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(nick + Colors.dark + " AKA " + Colors.regular + player.getName())))
                .append("")
                .create();
    }

}
