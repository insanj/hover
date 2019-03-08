package me.insanj.hover;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class HoverCommandExecutor implements CommandExecutor {
    public final Hover plugin;
    private final String ERROR_NO_PERMISSIONS = ChatColor.RED + "You do not have the required permission to run this Hover command.";

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
            
            if (Hover.playerHasPermission(player, Hover.PermissionType.ADD) == false) {
                player.sendMessage(ERROR_NO_PERMISSIONS);
                return true;
            }

            HashMap defaultContents = new HashMap();
            defaultContents.put("Name", player.getName());
            plugin.getConfig().createSection(player.getUniqueId().toString(), defaultContents);
            plugin.saveConfig();

            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Wrote default config for " + player.getName() + " with UUID " + player.getUniqueId().toString() + "!");
            return true;
        } else if (args[0].equalsIgnoreCase("reload")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Undefined use of Hover as non-Player. You have to be a Player to do anything!");
                return false;
            }

            if (Hover.playerHasPermission(player, Hover.PermissionType.RELOAD) == false) {
                sender.sendMessage(ERROR_NO_PERMISSIONS);
                return true;
            }

            plugin.reloadConfig();
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Hover reloaded!");
            return true;
        } else if (args[0].equalsIgnoreCase("start")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Undefined use of Hover as non-Player. You have to be a Player to do anything!");
                return false;
            }

            if (Hover.playerHasPermission(player, Hover.PermissionType.START) == false) {
                sender.sendMessage(ERROR_NO_PERMISSIONS);
                return true;
            }

            if (plugin.listener.disabled == false) {
                sender.sendMessage(ChatColor.RED + "Hover is already enabled.");
                return true;
            }

            plugin.listener.disabled = false;
            Bukkit.getServer().getPluginManager().registerEvents(plugin.listener, this); 
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Hover re-enabled!");
            return true;
        } else if (args[0].equalsIgnoreCase("stop")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Undefined use of Hover as non-Player. You have to be a Player to do anything!");
                return false;
            }

            if (Hover.playerHasPermission(sender, Hover.PermissionType.STOP) == false) {
                sender.sendMessage(ERROR_NO_PERMISSIONS);
                return true;
            }

            if (plugin.listener.disabled == true) {
                player.sendMessage(ChatColor.RED + "Hover is already disabled.");
                return true;
            }

            plugin.listener.disabled = true;
            HandlerList.unregisterAll(plugin.listener);
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Hover disabled!");
            return true;
        }

		return false;
    }
}
