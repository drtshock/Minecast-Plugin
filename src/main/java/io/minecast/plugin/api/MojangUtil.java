package io.minecast.plugin.api;

import com.mojang.api.profiles.HttpProfileRepository;
import com.mojang.api.profiles.Profile;
import com.mojang.api.profiles.ProfileCriteria;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MojangUtil {

    private static final HttpProfileRepository profileRepository = new HttpProfileRepository();
    private static final String AGENT = "minecraft";

    /**
     * Get a players UUID. Will only query Mojang if server isn't in online mode.
     *
     * @param name - name of player.
     *
     * @return player's UUID as a String.
     */
    public static String getUUID(String name) {
        if (Bukkit.getOnlineMode()) { // Don't query Mojang if server is online mode.
            Player player = Bukkit.getPlayer(name);
            return player != null ? player.getUniqueId().toString() : null;
        }

        Profile[] profiles = profileRepository.findProfilesByCriteria(new ProfileCriteria(name, AGENT));
        if (profiles.length > 0) {
            return profiles[0].getId();
        } else {
            return null;
        }
    }
}
