package me.insanj.hover;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HoverCommandExecutor implements CommandExecutor {
    public final Hover plugin;
    public HoverCommandExecutor(Hover plugin) {
        this.plugin = plugin;
    }

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 1) {
            return false;
        } else if (args[0].equalsIgnoreCase("add")) {
            if (!(sender instanceof Player)) {
                return false;
            }

            Player player = (Player)sender;
            
            HashMap defaultContents = new HashMap();
            defaultContents.put("Name", player.getName());
            plugin.getConfig().createSection(player.getUniqueId().toString(), defaultContents);
            plugin.saveConfig();

            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Wrote default config for " + player.getName() + " with UUID " + player.getUniqueId().toString() + "!");
            return true;
        } else if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Hover reloaded!");
            return true;
        }

		return false;
    }
}
