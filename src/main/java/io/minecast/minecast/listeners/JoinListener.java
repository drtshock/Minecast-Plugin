package io.minecast.minecast.listeners;

import io.minecast.minecast.Minecast;
import io.minecast.minecast.api.MinecastAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        Minecast.getInstance().getServer().getScheduler().runTaskLater(Minecast.getInstance(), new BukkitRunnable() {
            @Override
            public void run() {
                event.getPlayer().sendMessage("Make sure to register this server as a trusted server on Minecast!");
                event.getPlayer().sendMessage(MinecastAPI.getTrustedURL());
            }
        }, 40L);
    }
}
