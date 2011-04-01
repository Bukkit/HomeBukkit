
package com.dinnerbone.bukkit.home.commands;

import com.dinnerbone.bukkit.home.Home;
import com.dinnerbone.bukkit.home.HomeBukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GoHomeCommand implements CommandExecutor {
    private final HomeBukkit plugin;

    public GoHomeCommand(HomeBukkit plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = HomeBukkit.getPlayer(sender, args, 1);

        if (player == null) {
            return true;
        } else if ((player != sender) && (!sender.isOp())) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to go to other players homes");
            return true;
        } else if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "I don't know how to move you!");
            return true;
        } else if (args.length < 1) {
            return false;
        }

        String name = args[0];

        Home home = plugin.getDatabase().find(Home.class).where().ieq("name", name).ieq("playerName", player.getName()).findUnique();

        if (home == null) {
            sender.sendMessage(ChatColor.RED + "I don't know where that is!");
        } else {
            ((Player)sender).teleport(home.getLocation());
        }

        return true;
    }
}
