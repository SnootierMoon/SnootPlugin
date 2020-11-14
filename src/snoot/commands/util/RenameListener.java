package snoot.commands.util;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.meta.ItemMeta;
import snoot.Colors;

public class RenameListener implements Listener {

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        if (event.isCancelled() ||
                event.getInventory().getType() != InventoryType.ANVIL ||
                event.getSlotType() != InventoryType.SlotType.RESULT ||
                event.getCurrentItem() == null) {
            return;
        }
        final ItemMeta meta = event.getCurrentItem().getItemMeta();
        if (meta == null) {
            return;
        }
        final String name = Colors.translateAlternateColorCodes(meta.getDisplayName());
        meta.setDisplayName(name);
        event.getCurrentItem().setItemMeta(meta);
    }


}
