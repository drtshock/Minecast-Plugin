package io.minecast.minecast.tweet;

import org.bukkit.entity.Player;

import java.util.List;

public class PendingTweet {

    private Player player;
    private List<String> tweets;

    public PendingTweet(Player player, List<String> tweets) {
        this.player = player;
        this.tweets = tweets;
    }
}
