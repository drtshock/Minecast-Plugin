package io.minecast.plugin.commands;

import io.minecast.plugin.api.MinecastAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

        StringBuilder sb = new StringBuilder();
        for (String s : args) {
            sb.append(s).append(" ");
        }

        sender.sendMessage(ChatColor.GRAY + "Tweeting: " + ChatColor.RESET + sb.toString().trim());
        try {
            MinecastAPI.sendTweet((Player) sender, sb.toString().trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
