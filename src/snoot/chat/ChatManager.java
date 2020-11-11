package snoot.chat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import snoot.Main;
import snoot.parents.SnootFeatureManager;

public class ChatManager extends SnootFeatureManager {

    public final static List<ChatColor> colorList = Arrays.stream(ChatColor.values()).filter(ChatColor::isColor).collect(Collectors.toList());
    public final static List<String> chatColorList = colorList.stream().map(s -> s + s.name().toLowerCase()).collect(Collectors.toList());
    public final static List<String> colorCodeList = colorList.stream().map(s -> s + "&" + s.getChar()).collect(Collectors.toList());

    public ChatManager() {
        super("chat_config.yml");
        Main.addCommand("chatcolor", new ChatcolorCommandExecutor(), new ChatcolorTabCompleter());
        Main.addCommand("colors", new ColorsCommandExecutor(), new ColorsTabCompleter());
        Main.addCommand("nick", new NickCommandExecutor(), new NickTabCompleter());
        Main.addCommand("tags", new TagsCommandExecutor(), new TagsTabCompleter());
        Main.addListener(new ChatListener());
    }

    public boolean hasNick(final Player player) {
        return data.getString(player.getUniqueId().toString() + ".nick") != null;
    }

    public String getNick(final Player player) {
        String nick = data.getString(player.getUniqueId().toString() + ".nick");
        if (nick == null) {
            return ChatColor.GRAY + player.getName() + ChatColor.RESET;
        }
        return nick;
    }

    public void setNick(final Player player, final String nick) {
        data.set(player.getUniqueId().toString() + ".nick", nick);
        updateNick(player);
    }

    public boolean hasColor(final Player player) {
        return data.getString(player.getUniqueId().toString() + ".color") != null;
    }

    public ChatColor getColor(final Player player) {
        String color = data.getString(player.getUniqueId().toString() + ".color");
        if (color == null) {
            return ChatColor.RESET;
        }
        return ChatColor.getByChar(color);
    }

    public void setColor(final Player player, ChatColor color) {
        if (color == null) {
            data.set(player.getUniqueId().toString() + ".color", null);
            return;
        }
        data.set(player.getUniqueId().toString() + ".color", color.getChar());
    }

    public void updateNick(final Player player) {
        String nick = getNick(player);
        player.setDisplayName(nick);
        player.setPlayerListName(nick);
    }

    public List<String> getTagIDs() {
        ConfigurationSection tags = data.getConfigurationSection("tags");
        if (tags != null) {
            return new ArrayList<>(tags.getKeys(true));
        }
        return null;
    }

    public boolean tagExists(String tagID) {
        return getTagName(tagID) != null;
    }

    public String getTagName(String tagID) {
        return data.getString("tags." + tagID);
    }

    public void setTagName(String tagID, String value) {
        data.set("tags." + tagID, value);
    }

    public String getPlayerTagID(Player player) {
        String tagID = data.getString(player.getUniqueId().toString() + ".tag");
        if (tagID == null) {
            setPlayerTagID(player, null);
        }
        return tagID;
    }

    public void setPlayerTagID(Player player, String tagID) {
        data.set(player.getUniqueId().toString() + ".tag", tagID);
    }

    public String getPlayerTagName(Player player) {
        return getTagName(getPlayerTagID(player));
    }

}
