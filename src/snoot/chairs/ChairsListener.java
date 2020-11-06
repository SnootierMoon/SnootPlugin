package snoot.chairs;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.spigotmc.event.entity.EntityDismountEvent;
import snoot.Main;

public class ChairsListener implements Listener {
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!event.hasBlock() || event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Block block = event.getClickedBlock();
        if (block == null ||
                !(block.getBlockData() instanceof Stairs) ||
                block.getRelative(BlockFace.DOWN).getType() == Material.AIR ||
                !player.hasPermission("snoot.chairs") ||
                !Main.getChairsManager().chairsEnabled(player)) {
            return;
        } else if (!player.isSneaking() && player.getVehicle() != null) {
            player.getVehicle().remove();
            return;
        }

        Location location = block.getLocation().add(0.5, -0.5, 0.5).setDirection(((Stairs)block.getBlockData()).getFacing().getOppositeFace().getDirection());
        World world = location.getWorld();
        if (world == null) {
            return;
        }
        ArmorStand armorStand = world.spawn(location, ArmorStand.class);
        armorStand.addPassenger(event.getPlayer());
        armorStand.setGravity(false);
        armorStand.setSmall(true);
        armorStand.setInvisible(true);
        armorStand.setSilent(true);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Entity vehicle = event.getPlayer().getVehicle();
        if (vehicle != null) {
            getUp(event.getPlayer(), vehicle);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDismount(EntityDismountEvent event) {
        if (event.getEntity() instanceof Player) {
            getUp((Player)event.getEntity(), event.getDismounted());
        }
    }

    private void getUp(Player player, Entity armorStand) {
        if (armorStand.getType() == EntityType.ARMOR_STAND) {
            armorStand.removePassenger(player);
            armorStand.remove();
        }
    }

}
