package snoot.commands.util;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import snoot.commands.parents.SnootCommandExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;

public class NvCommandExecutor extends SnootCommandExecutor {

    public NvCommandExecutor() {
        super("group of commands for setting night vision", "snoot.creative.nv", true);
        addSubCommand(new SubCommand(
                "off",
                new ArrayList<>(),
                "turn off night vision",
                null,
                Collections.singletonMap(0, NvCommandExecutor::commandOff)));
        addSubCommand(new SubCommand(
                "on",
                new ArrayList<>(),
                "turn on night vision",
                null,
                Collections.singletonMap(0, NvCommandExecutor::commandOn)));
        addSubCommand(new SubCommand(
                "toggle",
                new ArrayList<>(),
                "toggle night vision",
                null,
                Collections.singletonMap(0, NvCommandExecutor::commandToggle)));

    }

    private static void commandOn(CommandSender sender, List<String> args) {
        ((Player)sender).addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1));
    }

    private static void commandOff(CommandSender sender, List<String> args) {
        ((Player)sender).removePotionEffect(PotionEffectType.NIGHT_VISION);
    }

    private static void commandToggle(CommandSender sender, List<String> args) {
        if (((Player)sender).hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
            commandOff(sender, null);
        } else {
            commandOn(sender, null);
        }
    }

}
