package snoot.commands.chat;

import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import snoot.Colors;
import snoot.Main;
import snoot.commands.parents.SnootCommandExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NickCommandExecutor extends SnootCommandExecutor {

    public NickCommandExecutor() {
        super("group of commands for changing your nickname", "snoot.chat.nick", true);
        addSubCommand(new SubCommand(
                "remove",
                new ArrayList<>(),
                "remove your current nickname",
                null,
                Collections.singletonMap(0, NickCommandExecutor::commandRemove)));
        addSubCommand(new SubCommand(
                "set",
                Collections.singletonList(new HelpArg("nickname", "your new nickname", "see /colors list")),
                "change your current nickname",
                null,
                Collections.singletonMap(1, NickCommandExecutor::commandSet),
                true));
    }

    private static void commandRemove(CommandSender sender, List<String> args) {
        if (!Main.getChatManager().hasNick((Player)sender)) {
            Colors.sendError(sender, "You do not have a nickname.");
            return;
        }
        Main.getChatManager().setNick((Player) sender, null);
        Colors.sendSuccess(sender, "Your nickname has been removed.");
    }

    private static void commandSet(CommandSender sender, List<String> args) {
        Main.getChatManager().setNick((Player)sender, args.get(0));
        sender.spigot().sendMessage(new ComponentBuilder()
                .append("Your nickname has been set to ")
                .color(Colors.successColor.asBungee())
                .append(Main.getChatManager().getNickComponent((Player)sender))
                .append(".")
                .color(Colors.successColor.asBungee())
                .create());
    }
}