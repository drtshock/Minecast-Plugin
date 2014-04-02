package io.minecast.minecast.api;

import io.minecast.minecast.Minecast;
import io.minecast.minecast.tweet.PendingTweet;
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

    public static String getTrustedURL() {
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

        System.out.println("Response: " + sb.toString());
        // parse string
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(sb.toString());
            return (String) jsonObject.get("url");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean trustsThisServer(String uuid) {
        URL url = null;
        try {
            url = new URL("https://www.minecast.io/api/v1/" + getKey() + "/trusted/" + uuid.replaceAll("-", ""));
            System.out.println("Trust: " + url.toString());
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

        System.out.println("Response: " + sb.toString());
        // parse string
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(sb.toString());
            return (boolean) jsonObject.get(uuid.replaceAll("-", ""));
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getKey() {
        return Minecast.getInstance().getConfig().getString("server-key");
    }

    public static boolean hasPendingTweet(String name) {
        return pendingTweets.containsKey(name);
    }

    public static PendingTweet getPendingTweet(String name) {
        return pendingTweets.get(name);
    }

    public static HashMap<String, PendingTweet> getPendingTweets() {
        return pendingTweets;
    }
}
