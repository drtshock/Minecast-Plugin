package io.minecast.plugin.listeners;

import io.minecast.plugin.Minecast;
import io.minecast.plugin.api.MinecastAPI;
import io.minecast.plugin.api.MojangUtil;
import io.minecast.plugin.util.Lang;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        try {
            String uuid = MojangUtil.getUUID(event.getPlayer().getName()).replaceAll("-", "");
            if (Minecast.getInstance().getConfig().getBoolean("trust-on-join", false) && !MinecastAPI.trustsThisServer(uuid)) {
                Minecast.getInstance().getServer().getScheduler().runTaskLater(Minecast.getInstance(), new BukkitRunnable() {
                    @Override
                    public void run() {
                        event.getPlayer().sendMessage(Lang.TITLE.toString() + Lang.REGISTER.toString().replace("{url}", MinecastAPI.getTrustedURL()));
                    }
                }, 40L);
            }
        } catch (Exception e) {
            Minecast.getInstance().getLogger().log(Level.SEVERE, "Failed to retrieve UUID for " + event.getPlayer().getName());
        }
    }
}
