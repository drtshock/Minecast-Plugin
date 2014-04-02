package io.minecast.plugin.listeners;

import io.minecast.plugin.Minecast;
import io.minecast.plugin.api.MinecastAPI;
import io.minecast.plugin.util.Lang;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        if (MinecastAPI.trustsThisServer(event.getPlayer().getUniqueId().toString())) return;
        Minecast.getInstance().getServer().getScheduler().runTaskLater(Minecast.getInstance(), new BukkitRunnable() {
            @Override
            public void run() {
                event.getPlayer().sendMessage(Lang.TITLE.toString() + Lang.REGISTER.toString().replace("{url}", MinecastAPI.getTrustedURL()));
            }
        }, 40L);
    }
}
