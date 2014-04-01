package io.minecast.minecast.commands;

import io.minecast.minecast.api.MinecastAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class TweetCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can tweet!");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage("You need to tweet something!");
            return true;
        }

        String tweet = args.toString();
        sender.sendMessage(ChatColor.GRAY + "Tweeting: " + ChatColor.RESET + tweet);
        try {
            MinecastAPI.sendTweet((Player) sender, tweet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
