package snoot.chat;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import snoot.parents.SnootCommandExecutor;

import java.util.ArrayList;
import java.util.Collections;

public class ColorsCommandExecutor extends SnootCommandExecutor {

    private final static String colorCodeList = ChatColor.GOLD + "" + ChatColor.BOLD + "Color Codes:\n" + String.join(" ",  ChatManager.colorCodeList);

    ColorsCommandExecutor() {
        super("", "list all color codes", "snoot.chat.color", 0, false);
    }

    @Override
    public void onMainCommand(CommandSender sender, ArrayList<String> args) {
        sender.sendMessage(colorCodeList);
    }

}
