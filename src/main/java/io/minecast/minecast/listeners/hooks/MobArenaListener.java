package io.minecast.minecast.listeners.hooks;

import com.garbagemule.MobArena.events.ArenaCompleteEvent;
import io.minecast.minecast.api.MinecastAPI;
import io.minecast.minecast.util.Lang;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Set;

public class MobArenaListener implements Listener {

    @EventHandler
    public void onMobArena(ArenaCompleteEvent event) {
        Set<Player> players = event.getSurvivors();
        for (Player p : players) {
            MinecastAPI.sendTweet(p, Lang.MOBARENA_SURVIVE.toString());
        }
    }
}
