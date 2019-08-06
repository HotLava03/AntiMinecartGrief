package io.github.hotlava03.antiminecartgrief.events;

import com.massivecraft.factions.engine.EnginePermBuild;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;

public class MinecartBreakEvt implements Listener {

    @EventHandler
    public void onVehicleBreak(VehicleDamageEvent e) {
        if (!(e.getAttacker() instanceof Player))
            return;

        Player player = (Player) e.getAttacker();
        Location coords = e.getVehicle().getLocation();

        if (!verify(player, coords)) {
            e.setCancelled(true);
            player.sendMessage(ChatColor.translateAlternateColorCodes(
                    '&',
                    "&cFailure &8&l\u00BB &7You cannot break this vehicle here."
            ));
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Material m = e.getMaterial();
        boolean isVehicle = m.equals(Material.MINECART)
                || m.equals(Material.TNT_MINECART)
                || m.equals(Material.HOPPER_MINECART)
                || m.equals(Material.FURNACE_MINECART)
                || m.equals(Material.CHEST_MINECART);


        if (!(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && isVehicle))
            return;

        if (!verify(e.getPlayer(), e.getClickedBlock().getLocation())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes(
                    '&',
                    "&cFailure &8&l\u00BB &7You cannot add vehicles here."
            ));
        }
    }

    private boolean verify(Player player, Location coords) {

        MPlayer mPlayer = MPlayer.get(player);

        return EnginePermBuild.canPlayerBuildAt(mPlayer, PS.valueOf(coords), false);
    }


}
