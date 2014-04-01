package io.minecast.minecast.tweet;

import io.minecast.minecast.Minecast;
import io.minecast.minecast.api.MinecastAPI;
import io.minecast.minecast.exceptions.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;

public class PendingTweet implements Listener {

    private Player player;
    private String tweet;

    public PendingTweet(Player player, String tweet) {
        this.player = player;
        this.tweet = tweet;
        Minecast.getInstance().getServer().getPluginManager().registerEvents(this, Minecast.getInstance());
        show();
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

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (event.getPlayer().getName().equalsIgnoreCase(player.getName())) {
            String cmd = event.getMessage().replace("/", "");
            if (cmd.equalsIgnoreCase("yes") || cmd.equalsIgnoreCase("y")) {
                try {
                    sendTweet(player.getUniqueId().toString(), tweet);
                    player.sendMessage("Sending tweet!");
                } catch (Exception e) {
                    Minecast.getInstance().getLogger().log(Level.SEVERE, "Something went wrong when " + player.getName() + " tried to send a tweet.");
                    e.printStackTrace();
                }
                Minecast.getInstance().getLogger().log(Level.INFO, player.getName() + " sent a tweet!");
            } else if(cmd.equalsIgnoreCase("no")) {
                player.sendMessage("Not sending tweet.");
                Minecast.getInstance().getLogger().log(Level.INFO, player.getName() + " refused to send a tweet.");
            }
            event.setCancelled(true);
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
    protected void sendTweet(String uuid, String tweet) throws Exception {
        if (MinecastAPI.getKey() == null) {
            Minecast.getInstance().getLogger().log(Level.WARNING, "Key is null.");
        }

        String urlParameters = null;
        urlParameters = "tweet=" + java.net.URLEncoder.encode(tweet, "UTF-8");
        URL url = new URL("http://minecast.io/api/v1/" + MinecastAPI.getKey() + "/tweet/" + uuid.replaceAll("-", ""));
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


}
