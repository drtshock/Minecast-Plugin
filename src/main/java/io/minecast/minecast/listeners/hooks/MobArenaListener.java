package io.minecast.minecast.listeners.hooks;

import com.garbagemule.MobArena.events.ArenaCompleteEvent;
import io.minecast.minecast.api.MinecastAPI;
import io.minecast.minecast.util.Lang;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MobArenaListener implements Listener {

    @EventHandler
    public void onMobArena(ArenaCompleteEvent event) {
        for (Player p : event.getSurvivors()) {
            MinecastAPI.sendTweet(p, Lang.MOBARENA_SURVIVE.toString());
        }
    }
}