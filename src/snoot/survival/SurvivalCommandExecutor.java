package snoot.survival;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import snoot.Main;
import snoot.parents.SnootCommandExecutor;

import java.util.ArrayList;
import java.util.Collections;

public class SurvivalCommandExecutor extends SnootCommandExecutor {

    public SurvivalCommandExecutor() {
        super("group of commands to switch between creative/survival worlds easily", null, false);
        addSubCommand(new SubCommand(
                "join",
                "",
                "join the survival world",
                null,
                Collections.singletonMap(0, SurvivalCommandExecutor::commandJoin),
                true));
        addSubCommand(new SubCommand(
                "leave",
                "",
                "leave the survival world",
                null,
                Collections.singletonMap(0, SurvivalCommandExecutor::commandLeave),
                true));
        addSubCommand(new SubCommand(
                "list",
                "",
                "list all players in the survival worlds",
                null,
                Collections.singletonMap(0, SurvivalCommandExecutor::commandList)
        ));
    }

    private static void commandJoin(CommandSender sender, ArrayList<String> args) {
        Player player = (Player)sender;
        Location location = Main.getInstance().getServer().getWorld("survival").getSpawnLocation();
        player.teleport(location);
        player.setGameMode(GameMode.SURVIVAL);
    }

    private static void commandLeave(CommandSender sender, ArrayList<String> args) {
        Player player = (Player)sender;
        Location location = Main.getInstance().getServer().getWorld("world").getSpawnLocation();
        player.teleport(location);
        player.setGameMode(GameMode.CREATIVE);
    }

    private static void commandList(CommandSender sender, ArrayList<String> args) {
    }
}
