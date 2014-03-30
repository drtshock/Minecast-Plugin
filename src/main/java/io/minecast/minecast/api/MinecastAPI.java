package io.minecast.minecast.api;

import io.minecast.minecast.Minecast;
import io.minecast.minecast.exceptions.*;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.logging.Level;

public class MinecastAPI {

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
                sendTweet(player.getUniqueId().toString(), s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Send a tweet from a defined user.
     *
     * @param uuid - the uuid of the user sending the tweet.
     * @param tweet - the tweet being sent.
     *
     * @return TweetResult for this attempt.
     */
    public static void sendTweet(String uuid, String tweet) throws Exception {
        if (getKey() == null) {
            Minecast.getInstance().getLogger().log(Level.WARNING, "Key is null.");
        }
        String urlParameters = null;
        urlParameters = "tweet=" + java.net.URLEncoder.encode(tweet, "UTF-8");
        URL url = new URL("http://beta.minecast.io/api/v1/" + getKey() + "/tweet/" + uuid);
        URLConnection conn = url.openConnection();

        conn.setDoOutput(true);

        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

        writer.write(urlParameters);
        writer.flush();

        JSONParser parser = new JSONParser();

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
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

            Integer errors = (Integer) jsonObject.get("errors");
            if (errors == null) return;
            switch (errors) {
                case 99:
                    throw new UnknownException();
                case 100:
                    throw new BadAPIRequestException();
                case 101:
                    throw new DuplicateTweetException();
                case 102:
                    throw new BadServerKeyException();
                case 103:
                    throw new ServerNotTrustedException();
                case 104:
                    throw new APILockoutException();
                case 105:
                    throw new APITemporaryBanException();
                case 106:
                    throw new APIBanException();
                case 107:
                    throw new NetworkBanException();
                case 108:
                    throw new AccountBanException();
                case 109:
                    throw new ServiceUnvailableException();
                case 110:
                    throw new BadServerKeyException();
                case 111:
                    throw new BadServerKeyException();
                case 112:
                    throw new ServerNotTrustedException();
                case 113:
                    throw new UnknownException();
                case 114:
                    throw new UnknownException();
                case 115:
                    throw new BadServerKeyException();
                default:
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a tweet from a defined user.
     *
     * @param player - the user sending the tweet.
     * @param tweet - the tweet being sent.
     *
     * @return TweetResult for this attempt.
     */
    public static void sendTweet(Player player, String tweet) throws Exception {
        sendTweet(player.getUniqueId().toString(), tweet);
    }

    /**
     * Send a tweet from the server's default handle.
     *
     * @param tweet - the tweet being sent.
     *
     * @return TweetResult for this attempt.
     */
    public static void sendTweet(String tweet) throws Exception {
        sendTweet(Minecast.getInstance().getConfig().getString("handle", "@Minecst"), tweet);
    }

    public static String getKey() {
        return Minecast.getInstance().getConfig().getString("key");
    }
}
