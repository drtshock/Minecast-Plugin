package io.minecast.minecast.commands;

import io.minecast.minecast.Minecast;
import io.minecast.minecast.api.MinecastAPI;
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
            player.sendMessage("You do not have a pending tweet.");
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("yes")) {
            player.sendMessage("Tweeting!");
            try {
                MinecastAPI.getPendingTweet(player.getName()).sendTweet();
            } catch (Exception e) {
                if(e.getMessage() == null || e == null) return true;
                player.sendMessage("Failed to send tweet :c");
                Minecast.getInstance().getLogger().log(Level.SEVERE, "Failed to send tweet for " + player.getName() + " : " + e.getMessage());
            }

        } else if (cmd.getName().equalsIgnoreCase("no")) {
            player.sendMessage("Not sending tweet!");
        } else {
            return false;
        }
        MinecastAPI.getPendingTweets().remove(player.getName());

        return true;
    }
}
