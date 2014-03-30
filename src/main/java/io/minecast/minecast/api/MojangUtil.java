package io.minecast.minecast.api;

import com.mojang.api.profiles.HttpProfileRepository;
import com.mojang.api.profiles.Profile;
import com.mojang.api.profiles.ProfileCriteria;

public class MojangUtil {

    private static final HttpProfileRepository profileRepository = new HttpProfileRepository();
    private static final String AGENT = "minecraft";

    /**
     * Get a players UUID.
     *
     * @param name - name of player.
     *
     * @return player's UUID as a String.
     */
    public static String getUUID(String name) {
        Profile[] profiles = profileRepository.findProfilesByCriteria(new ProfileCriteria(name, AGENT));
        if (profiles.length > 0) {
            return profiles[0].getId();
        } else {
            return null;
        }
    }
}
