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
import org.bukkit.permissions.PermissionAttachmentInfo;

public class HoverCommandExecutor implements CommandExecutor {
    public final Hover plugin;
    private final String ERROR_NO_PERMISSIONS = ChatColor.RED + "You do not have the required permission to run this Hover command.";

    public HoverCommandExecutor(Hover plugin) {
        this.plugin = plugin;
    }

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String commandName = label;
        if (commandName.equalsIgnoreCase("hover:help")) {
            sender.sendMessage("/hover help\n/hover add\n/hover start\n/hover stop\n/hover reload");
            return true;
        } else if (commandName.equalsIgnoreCase("hover:add")) {
            Player player = (Player)sender;
            
            HashMap defaultContents = new HashMap();
            defaultContents.put("Name", player.getName());
            plugin.getConfig().createSection(player.getName(), defaultContents);
            plugin.saveConfig();

            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Wrote default config for " + player.getName() + "!");
            return true;
        } else if (commandName.equalsIgnoreCase("hover:reload")) {
            plugin.reloadConfig();
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Hover reloaded!");
            return true;
        } else if (commandName.equalsIgnoreCase("hover:start")) {
            if (plugin.listener.disabled == false) {
                sender.sendMessage(ChatColor.RED + "Hover is already enabled.");
                return true;
            }

            plugin.listener.disabled = false;
            Bukkit.getServer().getPluginManager().registerEvents(plugin.listener, plugin); 
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Hover re-enabled!");
            return true;
        } else if (commandName.equalsIgnoreCase("hover:stop")) {
            if (plugin.listener.disabled == true) {
                sender.sendMessage(ChatColor.RED + "Hover is already disabled.");
                return true;
            }

            plugin.listener.disabled = true;
            HandlerList.unregisterAll(plugin.listener);
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Hover disabled!");
            return true;
        }

        sender.sendMessage(ChatColor.RED + "Hover did not recognize command: " + commandName);
        return false;
    }
}
