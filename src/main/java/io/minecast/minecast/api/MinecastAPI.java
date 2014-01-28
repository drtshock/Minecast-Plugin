package io.minecast.minecast.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class MinecastAPI {

    /**
     * Gives the developer the result of the update process.
     */
    public enum TweetResult {
        /**
         * The tweet sent!
         */
        SUCCESS,

        /**
         * The server has a bad API key.
         */
        FAIL_BAD_API_KEY,

        /**
         * The server has a bad server key.
         */
        FAIL_BAD_SERVER_KEY,

        /**
         * Something went wrong with the API call.
         */
        FAIL_BAD_API_CALL,

        /**
         * The account or IP is banned.
         */
        FAIL_BANNED,

        /**
         * The user doesn't have their account linked.
         */
        FAIL_NO_USER_ACCOUNT,

        /**
         * User has not accepted this server as trusted.
         */
        FAIL_UNTRUSTED_SERVER,

        /**
         * The account has reached its tweet limit.
         */
        FAIL_ACCOUNT_LIMIT,

        /**
         * Something went wrong :(
         */
        FAIL_UNKNOWN;
    }

    /**
     * Give a player a list of tweets that they can send. This will open an inventory interface.
     *
     * @param user - the user to offer the tweets.
     * @param tweets - Strings of possible tweets.
     *
     * @return TweetResult for this attempt.
     */
    public static TweetResult sendTweet(String user, List<String> tweets) {
        return sendTweet(Bukkit.getPlayerExact(user), tweets);
    }

    /**
     * Give a player a list of tweets that they can send. This will open an inventory interface.
     *
     * @param player - the player to offer the tweets.
     * @param tweets - Strings of possible tweets.
     *
     * @return TweetResult for this attempt.
     */
    public static TweetResult sendTweet(Player player, List<String> tweets) {
        return TweetResult.FAIL_UNKNOWN;
    }

    /**
     * Send a tweet but ask the user to confirm it first.
     *
     * @param user - the user to offer the tweet.
     * @param tweet - the tweet to offer.
     * @param confirm - whether or not to ask the user to send.
     *
     * @return TweetResult for this attempt.
     */
    public static TweetResult sendTweet(String user, String tweet, boolean confirm) {
        return TweetResult.FAIL_UNKNOWN;
    }

    /**
     * Send a tweet from a defined user.
     *
     * @param user - the user sending the tweet.
     * @param tweet - the tweet being sent.
     *
     * @return TweetResult for this attempt.
     */
    public static TweetResult sendTweet(String user, String tweet) {
        return TweetResult.FAIL_UNKNOWN;
    }

    /**
     * Send a tweet from the server's default handle.
     *
     * @param tweet - the tweet being sent.
     *
     * @return TweetResult for this attempt.
     */
    public static TweetResult sendTweet(String tweet) {
        return TweetResult.FAIL_UNKNOWN;
    }
}
