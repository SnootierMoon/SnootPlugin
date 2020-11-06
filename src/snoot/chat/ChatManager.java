package snoot.chat;

import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import snoot.Main;
import snoot.MessageFormat;
import snoot.SnootFeatureManager;

public final class ChatManager extends SnootFeatureManager {

    public final static List<String> chatColorList = Arrays.stream(ChatColor.values()).filter(ChatColor::isColor).map(MessageFormat::coloredColor).collect(Collectors.toList());

    public ChatManager() {
        super("chat_config.yml");
        PluginCommand chatcolorCommand = Main.getInstance().getCommand("chatcolor");
        PluginCommand nickCommand = Main.getInstance().getCommand("nick");
        PluginCommand tagsCommand = Main.getInstance().getCommand("tags");
        if (chatcolorCommand == null) {
            Main.getInstance().getLogger().info("Internal error: Failed to find \"chatcolor\" command.");
        } else {
            chatcolorCommand.setExecutor(new ChatcolorCommandExecutor());
            chatcolorCommand.setTabCompleter(new ChatcolorTabCompleter());
        }
        if (nickCommand == null) {
            Main.getInstance().getLogger().info("Internal error: Failed to find \"nick\" command.");
        } else {
            nickCommand.setExecutor(new NickCommandExecutor( ));
            nickCommand.setTabCompleter(new NickTabCompleter());
        }
        if (tagsCommand == null) {
            Main.getInstance().getLogger().info("Internal error: Failed to find \"tags\" command.");
        } else {
            tagsCommand.setExecutor(new TagsCommandExecutor());
            tagsCommand.setTabCompleter(new TagsTabCompleter());
        }
        Main.getInstance().getServer().getPluginManager().registerEvents(new ChatListener(), Main.getInstance());
    }

    public boolean hasNick(final Player player) {
        return data.getString(player.getUniqueId().toString() + ".nick") != null;
    }

    public String getNick(final Player player) {
        String nick = data.getString(player.getUniqueId().toString() + ".nick");
        if (nick == null) {
            return MessageFormat.info(player.getName());
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
