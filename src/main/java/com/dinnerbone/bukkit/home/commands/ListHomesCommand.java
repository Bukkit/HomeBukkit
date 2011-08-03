
package com.dinnerbone.bukkit.home.commands;

import com.dinnerbone.bukkit.home.Home;
import com.dinnerbone.bukkit.home.HomeBukkit;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListHomesCommand implements CommandExecutor {
    private final HomeBukkit plugin;

    public ListHomesCommand(HomeBukkit plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = HomeBukkit.getPlayer(sender, args, 0);

        if (player == null) {
            return true;
        }

        List<Home> homes = plugin.getDatabase().find(Home.class).where().ieq("playerName", player.getName()).findList();

        if (homes.isEmpty()) {
            if (sender == player) {
                sender.sendMessage("You have no homes!");
            } else {
                sender.sendMessage("That player has no homes!");
            }
        } else {
        	if(sender == player && player.hasPermission("homebukkit.list.self") ){
        		
        	
            String result = "";

            for (Home home : homes) {
                if (result.length() > 0) {
                    result += ", ";
                }

                result += home.getName();
            }

            sender.sendMessage("All home(s): " + result);
        }
        	else if(sender != player && player.hasPermission("homebukkit.list.other")){
        		 String result = "";

                 for (Home home : homes) {
                     if (result.length() > 0) {
                         result += ", ";
                     }

                     result += home.getName();
                 }

                 sender.sendMessage("All home(s): " + result);
        	}
        	else if(sender == player &&! player.hasPermission("homebukkit.list.self")){
        		player.sendMessage("you dont have permission to list your own homes!");
        	}
        	else if(sender != player &&! player.hasPermission("homebukkit.list.other")){
        		player.sendMessage("you dont have permission to list other peoples homes!");
        	}
        }

        return true;
    }
}
