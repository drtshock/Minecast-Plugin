package io.minecast.plugin.api;

import io.minecast.plugin.Minecast;
import io.minecast.plugin.tweet.PendingTweet;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public class MinecastAPI {

    private static HashMap<String, PendingTweet> pendingTweets = new HashMap<>();
    private static String trustedURL;

    /**
     * Give a player a list of tweets that they can send. This will open an inventory interface.
     *
     * @param user - the user to offer the tweets.
     * @param tweets - Strings of possible tweets.
     *
     * @return TweetResult for this attempt.
     */
    public static void sendTweet(String user, List<String> tweets) {
        sendTweet(Bukkit.getPlayerExact(user), tweets);
    }

    /**
     * Give a player a list of tweets that they can send. This will open an inventory interface.
     *
     * @param player - the player to offer the tweets.
     * @param tweets - Strings of possible tweets.
     *
     * @return TweetResult for this attempt.
     */
    public static void sendTweet(Player player, List<String> tweets) {
        Validate.notNull(player, "Player cannot be null");
        for (String s : tweets) {
            try {
                sendTweet(player, s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Send a tweet from a defined user.
     *
     * @param player - the player sending the tweet.
     * @param tweet - the tweet being sent.
     *
     * @return TweetResult for this attempt.
     */
    public static PendingTweet sendTweet(Player player, String tweet) throws Exception {
        if (getKey() == null) {
            Minecast.getInstance().getLogger().log(Level.WARNING, "Key is null.");
            return null;
        }

        if (player == null) {
            Minecast.getInstance().getLogger().log(Level.WARNING, "Cannot send tweet for null player.");
            return null;
        }
        return new PendingTweet(player, tweet);
    }

    /**
     * Get the URL to add this server as trusted.
     *
     * @return
     */
    public static String getTrustedURL() {
        if (trustedURL != null) return trustedURL;

        URL url = null;
        try {
            url = new URL("https://minecast.io/api/v1/" + getKey() + "/trust_url");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        JSONParser parser = new JSONParser();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // parse string
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(sb.toString());
            trustedURL = (String) jsonObject.get("url");
            return trustedURL;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets whether or not this user from uuid trusts this server to tweet for them.
     *
     * @param uuid - uuid of user.
     *
     * @return true if trusted, otherwise false.
     */
    public static boolean trustsThisServer(String uuid) {
        Minecast.getInstance().getLogger().log(Level.SEVERE, "UUID is: " + uuid);
        if (uuid == null) {
            return false;
        }
        URL url = null;
        try {
            url = new URL("https://www.minecast.io/api/v1/" + getKey() + "/trusted/" + uuid.replaceAll("-", ""));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        JSONParser parser = new JSONParser();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // parse string
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(sb.toString());
            if (jsonObject == null || !jsonObject.containsKey(uuid.replaceAll("-", ""))) {
                Minecast.getInstance().getLogger().log(Level.SEVERE, "JsonObject is null or not valid.");
                return false;
            }
            return (boolean) jsonObject.get(uuid.replaceAll("-", ""));
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get the server key. Used to post API requests.
     *
     * @return server key as String.
     */
    public static String getKey() {
        return Minecast.getInstance().getConfig().getString("server-key");
    }

    /**
     * Get if a user has a pending tweet.
     *
     * @param name - name of user.
     *
     * @return - true if has pending tweet, otherwise false.
     */
    public static boolean hasPendingTweet(String name) {
        return pendingTweets.containsKey(name);
    }

    /**
     * Get a users pending tweet.
     *
     * @param name - name of user.
     *
     * @return pending tweet.
     */
    public static PendingTweet getPendingTweet(String name) {
        return pendingTweets.get(name);
    }

    /**
     * Get the map of users with pending tweets.
     *
     * @return - map of usernames and pending tweets.
     */
    public static HashMap<String, PendingTweet> getPendingTweets() {
        return pendingTweets;
    }
}
