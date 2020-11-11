package snoot.chairs;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.event.entity.EntityDismountEvent;
import snoot.Main;

public class ChairsListener implements Listener {
    
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (!event.hasBlock() || event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Block block = event.getClickedBlock();
        if (block == null ||
                !(block.getBlockData() instanceof Stairs) ||
                block.getRelative(BlockFace.UP).getType() != Material.AIR ||
                !event.getPlayer().hasPermission("snoot.chairs") ||
                !Main.getChairsManager().chairsEnabled(event.getPlayer()) ||
                event.getPlayer().isSneaking()) {
            return;
        }
        if (event.getPlayer().isGliding()) {
            event.getPlayer().setGliding(false);
        }
        if (event.getPlayer().getVehicle() != null) {
            event.getPlayer().getVehicle().remove();
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
        armorStand.setInvulnerable(true);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (event.getPlayer().getVehicle() != null) {
            getUp(event.getPlayer(), event.getPlayer().getVehicle());
        }
    }

    @EventHandler
    public void onDismount(EntityDismountEvent event) {
        if (event.getEntity() instanceof Player) {
            new BukkitRunnable(){
                @Override
                public void run() {
                    getUp((Player)event.getEntity(), event.getDismounted());
                }
            }.runTaskLater(Main.getInstance(), 1);
        }
    }

    private void getUp(Player player, Entity armorStand) {
        Location location = armorStand.getLocation().add(0, 1, 0);
        location.setDirection(player.getLocation().getDirection());
        if (armorStand.getType() == EntityType.ARMOR_STAND) {
            armorStand.removePassenger(player);
            armorStand.remove();
        }
        player.teleport(location);
    }

}
