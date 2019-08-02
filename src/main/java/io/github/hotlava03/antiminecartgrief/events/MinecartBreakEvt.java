package io.github.hotlava03.antiminecartgrief.events;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import io.github.hotlava03.antiminecartgrief.AntiMinecartGrief;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;

import java.util.Set;

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
                || m.equals(Material.EXPLOSIVE_MINECART)
                || m.equals(Material.HOPPER_MINECART)
                || m.equals(Material.POWERED_MINECART)
                || m.equals(Material.STORAGE_MINECART);


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

        Set<MPlayer> mPlayers = BoardColl.get().getTerritoryAccessAt(PS.valueOf(coords)).getGrantedMPlayers();
        Set<Faction> factions = BoardColl.get().getTerritoryAccessAt(PS.valueOf(coords)).getGrantedFactions();
        boolean allow = false;

        for (MPlayer granted : mPlayers)
            if (granted.equals(mPlayer))
                allow = true;

        for (Faction granted : factions) {
            if (granted.equals(mPlayer.getFaction()))
                allow = true;
        }

        if (BoardColl.get().getFactionAt(PS.valueOf(coords)).isNone())
            allow = true;

        if (mPlayer.isInOwnTerritory() && (!mPlayer.getRole().equals(Rel.RECRUIT)))
            allow = true;

        if (mPlayer.isOverriding() && !allow) {
            AntiMinecartGrief.getPlugin(AntiMinecartGrief.class).getLogger()
                    .info("Player " + mPlayer.getName() + " broke a vehicle with bypass in " +
                            coords.getX() + " " + coords.getY() + " " + coords.getZ());
            player.sendMessage(ChatColor.translateAlternateColorCodes(
                    '&',
                    "&6Warning &8&l\u00BB &7You have hit a vehicle in a disallowed area because you are overriding. Use &f/f admin &7to disable this."
            ));
            allow = true;
        }
        return allow;
    }


}
