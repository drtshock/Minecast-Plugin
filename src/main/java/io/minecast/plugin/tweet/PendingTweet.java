package io.minecast.plugin.tweet;

import io.minecast.plugin.Minecast;
import io.minecast.plugin.api.MinecastAPI;
import io.minecast.plugin.api.MojangUtil;
import io.minecast.plugin.exceptions.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;

public class PendingTweet {

    private Player player;
    private String tweet;
    private String uuid;

    /**
     * Creates a new PendingTweet for the user. Server will ask user to confirm.
     *
     * @param player - player that's tweeting.
     * @param tweet - tweet being sent.
     */
    public PendingTweet(final Player player, String tweet) {
        this.player = player;
        this.tweet = tweet;
        this.uuid = MojangUtil.getUUID(player.getName()).replaceAll("-", "");
        show();
        MinecastAPI.getPendingTweets().put(player.getName(), this);
        Minecast.getInstance().getServer().getScheduler().runTaskLater(Minecast.getInstance(), new BukkitRunnable() {
            @Override
            public void run() {
                if (MinecastAPI.hasPendingTweet(player.getName())) {
                    MinecastAPI.getPendingTweets().remove(player.getName());
                }
            }
        }, Minecast.getInstance().getConfig().getLong("tweet-expire", 100L));
    }

    private void show() {
        for (String s : Minecast.getInstance().getConfig().getStringList("pending-tweet")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', s.replace("{tweet}", tweet)));
        }
    }

    public Player getPlayer() {
        return this.player;
    }

    public String getTweet() {
        return this.tweet;
    }

    /**
     * Send a tweet from a defined user.
     *
     * @return TweetResult for this attempt.
     */
    public void sendTweet() throws Exception {
        if (MinecastAPI.getKey() == null) {
            Minecast.getInstance().getLogger().log(Level.WARNING, "Cant send tweets while key is null.");
        }

        String urlParameters = "tweet=" + java.net.URLEncoder.encode(tweet, "UTF-8");
        URL url = new URL("https://www.minecast.io/api/v1/" + MinecastAPI.getKey() + "/tweet/" + uuid);
        URLConnection conn = url.openConnection();

        conn.setDoOutput(true);

        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

        writer.write(urlParameters);
        writer.flush();

        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String ret = "";
        while ((line = reader.readLine()) != null) {
            ret = ret + line;
        }
        writer.close();
        reader.close();

        JSONParser parser = new JSONParser();

        // parse string
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(ret);
            if (!jsonObject.containsValue("errors")) return;

            Integer errors = Integer.valueOf((String) jsonObject.get("errors"));
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


}
