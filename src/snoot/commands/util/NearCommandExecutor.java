package snoot.commands.util;

import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import snoot.Colors;
import snoot.Main;
import snoot.commands.parents.SnootCommandExecutor;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class NearCommandExecutor extends SnootCommandExecutor {

    public NearCommandExecutor() {
        super(Collections.singletonList(new HelpArg("max-distance", "the radius within which players should be listed", "number", false)), "show nearby players", "snoot.near", new HashMap<Integer, BiConsumer<CommandSender, List<String>>>() {{
            put(0,  NearCommandExecutor::commandNear);
            put(1,  NearCommandExecutor::commandNear2);
        }}, true);
    }

    private static void commandNear(CommandSender sender, List<String> args) {
        commandNear2(sender, Collections.singletonList("200"));
    }

    private static void commandNear2(CommandSender sender, List<String> args) {
        int distance;
        try {
            distance = Integer.parseInt(args.get(0));
        } catch (NumberFormatException e) {
            Colors.sendError(sender, "Invalid player distance.");
            return;
        }
        Location playerLocation = ((Player)sender).getLocation();
        List<Player> playerWorldList = Main.getInstance().getServer().getOnlinePlayers().stream().filter(player -> player.getWorld() == ((Player)sender).getWorld() && player != sender).collect(Collectors.toList());
        List<Player> nearbyPlayers = new ArrayList<>();
        for (Player target : playerWorldList) {
            if (playerLocation.distance(target.getLocation()) < distance) {
                if (target != sender) {
                    nearbyPlayers.add(target);
                }
            }
        }
        if (nearbyPlayers.isEmpty()) {
            Colors.sendError(sender, "There are no other players in your world.");
            return;
        }
        Colors.sendInfo(sender, Colors.title + "Nearby players:");
        for (Player nearbyPlayer : nearbyPlayers) {
            sender.spigot().sendMessage(new ComponentBuilder()
                    .append(Main.getChatManager().getNickComponent(nearbyPlayer))
                    .append(": " + playerLocation.distance(nearbyPlayer.getLocation()) + " blocks")
                    .color(Colors.darkColor.asBungee())
                    .create());
        }
    }

}
