package snoot.commands.survival;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import snoot.Colors;
import snoot.Main;
import snoot.commands.parents.SnootCommandExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SurvivalCommandExecutor extends SnootCommandExecutor {

    public SurvivalCommandExecutor() {
        super("group of commands to switch between creative/survival worlds easily", "snoot.survival", false);
        addSubCommand(new SubCommand(
                "join",
                new ArrayList<>(),
                "join the survival world",
                null,
                Collections.singletonMap(0, SurvivalCommandExecutor::commandJoin),
                true));
        addSubCommand(new SubCommand(
                "leave",
                new ArrayList<>(),
                "leave the survival world",
                null,
                Collections.singletonMap(0, SurvivalCommandExecutor::commandLeave),
                true));
        addSubCommand(new SubCommand(
                "list",
                new ArrayList<>(),
                "list all players in the survival worlds",
                null,
                Collections.singletonMap(0, SurvivalCommandExecutor::commandList)
        ));
    }

    private static void commandJoin(CommandSender sender, List<String> args) {
        List<World> survivalWorlds = Main.getSurvivalManager().getWorlds(SurvivalManager.WorldType.SURVIVAL);
        if (survivalWorlds.isEmpty()) {
            Colors.sendError(sender, "Could not find any survival worlds.");
            return;
        }
        if (survivalWorlds.contains(((Player)sender).getWorld())) {
            Colors.sendError(sender, "You are already in a survival world.");
            return;
        }
        Location location = survivalWorlds.get(0).getSpawnLocation();
        ((Player)sender).teleport(location);
        ((Player)sender).setGameMode(GameMode.SURVIVAL);
    }

    private static void commandLeave(CommandSender sender, List<String> args) {
        List<World> creativeWorlds = Main.getSurvivalManager().getWorlds(SurvivalManager.WorldType.CREATIVE);
        if (creativeWorlds.isEmpty()) {
            Colors.sendError(sender, "Could not find any creative worlds.");
            return;
        }
        if (creativeWorlds.contains(((Player)sender).getWorld())) {
            Colors.sendError(sender, "You are already in a creative world.");
            return;
        }
        Location location = creativeWorlds.get(0).getSpawnLocation();
        ((Player)sender).teleport(location);
        ((Player)sender).setGameMode(GameMode.CREATIVE);
    }

    private static void commandList(CommandSender sender, List<String> args) {
        List<BaseComponent> playerNicks = new ArrayList<>();
        for (Player player : Main.getInstance().getServer().getOnlinePlayers()) {
            if (Main.getSurvivalManager().getWorlds(SurvivalManager.WorldType.SURVIVAL).contains(player.getWorld())) {
                Collections.addAll(playerNicks, Main.getChatManager().getNickComponent(player));
            }
        }
        if (playerNicks.isEmpty()) {
            Colors.sendInfo(sender, "There are currently no players in survival.");
            return;
        }
        sender.sendMessage("");
        Colors.sendInfo(sender, Colors.title + "Players in Survival:");
        sender.spigot().sendMessage(playerNicks.toArray(new BaseComponent[0]));
    }
}
