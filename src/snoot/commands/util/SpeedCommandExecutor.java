package snoot.commands.util;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import snoot.Colors;
import snoot.commands.parents.SnootCommandExecutor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;

public class SpeedCommandExecutor extends SnootCommandExecutor {

    enum SpeedType {
        BOTH, FLY, WALK

    }

    public SpeedCommandExecutor() {
        super(Arrays.asList(new HelpArg("speed", "the speed", "number (-10 to 10) or \"reset\"", true),
                new HelpArg("type", "the speed type", "\"walk\" or \"fly\"", false)), "set your speed", "snoot.creative.speed", new HashMap<Integer, BiConsumer<CommandSender, List<String>>>() {{
            put(1,  SpeedCommandExecutor::commandSpeed);
            put(2,  SpeedCommandExecutor::commandSpeed2);
        }}, true);
    }

    private static void commandSpeed(CommandSender sender, List<String> args) {
        args.add(((Player)sender).isFlying() ? "fly" : "walk");
        commandSpeed2(sender, args);
    }

    private static void commandSpeed2(CommandSender sender, List<String> args) {
        float speed10, speed;
        SpeedType speedType;
        try {
            if (args.get(0).toLowerCase().equals("reset")) {
                speed10 = 2;
            } else {
                speed10 = Float.parseFloat(args.get(0));
            }
            speed = speed10 / 10;
            speedType = SpeedType.valueOf(args.get(1).toUpperCase());
        } catch (NumberFormatException e) {
            Colors.sendError(sender, "Invalid number for speed.");
            return;
        } catch (java.lang.IllegalArgumentException e) {
            Colors.sendError(sender, "Invalid speed type.");
            return;
        }
        if (speedType == SpeedType.BOTH || speedType == SpeedType.WALK) ((Player)sender).setWalkSpeed(speed);
        if (speedType == SpeedType.BOTH || speedType == SpeedType.FLY) ((Player)sender).setFlySpeed(speed);
        Colors.sendSuccess(sender, "Set ", speedType.name().toLowerCase(), " speed to ", Float.toString(speed10));
    }

}
