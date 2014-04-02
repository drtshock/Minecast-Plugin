package io.minecast.plugin.listeners;

import com.garbagemule.MobArena.events.ArenaCompleteEvent;
import io.minecast.plugin.api.MinecastAPI;
import io.minecast.plugin.util.Lang;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MobArenaListener implements Listener {

    @EventHandler
    public void onMobArena(ArenaCompleteEvent event) {
        for (Player p : event.getSurvivors()) {
            try {
                MinecastAPI.sendTweet(p, Lang.MOBARENA_SURVIVE.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
