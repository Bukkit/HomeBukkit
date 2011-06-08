
package com.dinnerbone.bukkit.home.commands;

import com.avaje.ebean.EbeanServer;
import com.dinnerbone.bukkit.home.Home;
import com.dinnerbone.bukkit.home.HomeBukkit;
import java.util.Set;
import java.util.HashSet;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHomeCommand implements CommandExecutor {
    private final HomeBukkit plugin;

    // Properties the EbeanServer must be told to update.  It
    // doesn't appear to be smart enough to figure these out on its own.
    private static final Set<String> updateProps;
    static {
        updateProps = new HashSet<String>();
        updateProps.add("x");
        updateProps.add("y");
        updateProps.add("z");
        updateProps.add("yaw");
        updateProps.add("pitch");
        updateProps.add("world_name");
    }

    public SetHomeCommand(HomeBukkit plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = HomeBukkit.getPlayer(sender, args, 1);

        if (player == null) {
            return true;
        } else if ((player != sender) && (!sender.isOp())) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to set other players homes");
            return true;
        } else if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "I don't know where you are!");
            return true;
        } else if (args.length < 1) {
            return false;
        }

        String name = args[0];

        EbeanServer db = plugin.getDatabase();
        db.beginTransaction();

        try {
            Home home = db.find(Home.class).where().ieq("name", name).ieq("playerName", player.getName()).findUnique();
            boolean isUpdate = false;

            if (home == null) {
                sender.sendMessage(ChatColor.BLUE + "Creating home " + name +  "...");

                home = new Home();
                home.setPlayer(player);
                home.setName(name);
            } else {
                sender.sendMessage(ChatColor.BLUE + "Updating home " + name + "...");

                isUpdate = true;
            }

            home.setLocation(((Player)sender).getLocation());

            if (isUpdate) db.update(home, updateProps);
            db.save(home);
            db.commitTransaction();
        } finally {
            db.endTransaction();
        }
        
        return true;
    }
}
