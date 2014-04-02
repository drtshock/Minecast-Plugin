package io.minecast.plugin.commands;

import io.minecast.plugin.Minecast;
import io.minecast.plugin.api.MinecastAPI;
import io.minecast.plugin.util.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class ConfirmCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can tweet.");
            return true;
        }

        if (args.length != 0) {
            return false;
        }

        Player player = (Player) sender;

        if (!MinecastAPI.hasPendingTweet(player.getName())) {
            player.sendMessage(Lang.TITLE.toString() + Lang.NO_PENDING.toString());
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("yes")) {
            player.sendMessage(Lang.TITLE.toString() + Lang.SENDING_TWEET.toString());
            try {
                MinecastAPI.getPendingTweet(player.getName()).sendTweet();
            } catch (Exception e) {
                player.sendMessage(Lang.TITLE.toString() + Lang.FAILED_SENDING.toString());
                Minecast.getInstance().getLogger().log(Level.SEVERE, "Failed to send tweet for " + player.getName() + " : " + e.getMessage());
            }

        } else if (cmd.getName().equalsIgnoreCase("no")) {
            player.sendMessage(Lang.TITLE.toString() + Lang.CANCELLING_TWEET.toString());
        } else {
            return false;
        }
        MinecastAPI.getPendingTweets().remove(player.getName());

        return true;
    }
}
