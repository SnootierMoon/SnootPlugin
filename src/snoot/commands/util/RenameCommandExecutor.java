package snoot.commands.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import snoot.Colors;
import snoot.commands.parents.SnootCommandExecutor;

import java.util.Collections;
import java.util.List;

public class RenameCommandExecutor extends SnootCommandExecutor {

    RenameCommandExecutor() {
        super(Collections.singletonList(new HelpArg("name", "the new name of the item", "see /colors", true)), "rename the item in your hand", "snoot.rename", Collections.singletonMap(1, RenameCommandExecutor::commandRename), true);
    }

    private static void commandRename(CommandSender sender, List<String> args) {
        ItemStack item = ((Player)sender).getInventory().getItemInMainHand();
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            Colors.sendError(sender, "You do not have an item in your main hand.");
            return;
        }
        itemMeta.setDisplayName(ChatColor.WHITE + Colors.translateAlternateColorCodes(args.get(0)));
        item.setItemMeta(itemMeta);
        ((Player)sender).getInventory().setItemInMainHand(item);
        Colors.sendSuccess(sender, "Your item's name has been set to ", item.getItemMeta().getDisplayName(), ".");
    }

}
