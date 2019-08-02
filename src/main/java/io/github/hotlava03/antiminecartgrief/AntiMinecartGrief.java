package io.github.hotlava03.antiminecartgrief;

import io.github.hotlava03.antiminecartgrief.events.MinecartBreakEvt;
import org.bukkit.plugin.java.JavaPlugin;

public final class AntiMinecartGrief extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new MinecartBreakEvt(), this);
    }

}
