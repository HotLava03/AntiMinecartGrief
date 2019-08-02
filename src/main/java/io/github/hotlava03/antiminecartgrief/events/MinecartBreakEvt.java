package io.github.hotlava03.antiminecartgrief.events;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import io.github.hotlava03.antiminecartgrief.AntiMinecartGrief;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleDestroyEvent;

import java.util.Set;

public class MinecartBreakEvt implements Listener {

    @EventHandler
    public void onMinecartBreak(VehicleDestroyEvent e) {
        if (!(e.getAttacker() instanceof Player))
            return;

        Player player = (Player) e.getAttacker();

        Location coords = e.getVehicle().getLocation();
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

        if (mPlayer.isOverriding() && !allow) {
            AntiMinecartGrief.getPlugin(AntiMinecartGrief.class).getLogger()
                    .info("Player " + mPlayer.getName() + " broke a vehicle with bypass in " +
                            coords.getX() + " " + coords.getY() + " " + coords.getZ());
            player.sendMessage(ChatColor.translateAlternateColorCodes(
                    '&',
                    "&6Warning &8&l\u00BB &7You have broken a vehicle in a disallowed area because you are overriding. Use &f/f admin &7to disable this."
            ));
            allow = true;
        }

        if (!allow) {
            e.setCancelled(true);
            player.sendMessage(ChatColor.translateAlternateColorCodes(
                    '&',
                    "&cFailure &8&l\u00BB &7You cannot break this minecart here."
            ));
        }

    }


}
