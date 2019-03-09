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
    public final String HOVER_PERMISSION_ADD_CMD = "add";
    public final String HOVER_PERMISSION_START_CMD = "start";
    public final String HOVER_PERMISSION_STOP_CMD = "stop";
    public final String HOVER_PERMISSION_RELOAD_CMD = "reload";

    private final String ERROR_NO_PERMISSIONS = ChatColor.RED + "You do not have the required permission to run this Hover command.";

    public final Hover plugin;

    public HoverCommandExecutor(Hover plugin) {
        this.plugin = plugin;
    }

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 1) {
            return false;
        } else if (senderHasPermission(sender, args[0]) == false) {
            sender.sendMessage(ERROR_NO_PERMISSIONS);
            return true;
        }
        
        if (args[0].equalsIgnoreCase(HOVER_PERMISSION_ADD_CMD)) {
            Player player = (Player)sender;
            
            HashMap defaultContents = new HashMap();
            defaultContents.put("Name", player.getName());
            plugin.getConfig().createSection(player.getName(), defaultContents);
            plugin.saveConfig();

            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Wrote default config for " + player.getName() + "!");
            return true;
        } else if (args[0].equalsIgnoreCase(HOVER_PERMISSION_RELOAD_CMD)) {
            plugin.reloadConfig();
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Hover reloaded!");
            return true;
        } else if (args[0].equalsIgnoreCase(HOVER_PERMISSION_START_CMD)) {
            if (plugin.listener.disabled == false) {
                sender.sendMessage(ChatColor.RED + "Hover is already enabled.");
                return true;
            }

            plugin.listener.disabled = false;
            Bukkit.getServer().getPluginManager().registerEvents(plugin.listener, plugin); 
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Hover re-enabled!");
            return true;
        } else if (args[0].equalsIgnoreCase(HOVER_PERMISSION_STOP_CMD)) {
            if (plugin.listener.disabled == true) {
                sender.sendMessage(ChatColor.RED + "Hover is already disabled.");
                return true;
            }

            plugin.listener.disabled = true;
            HandlerList.unregisterAll(plugin.listener);
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Hover disabled!");
            return true;
        }

		return false;
    }

    public final String HOVER_PERMISSION_ADD_KEY = "hover.add";
    public final String HOVER_PERMISSION_START_KEY = "hover.start";
    public final String HOVER_PERMISSION_STOP_KEY = "hover.stop";
    public final String HOVER_PERMISSION_RELOAD_KEY = "hover.reload";

    public enum PermissionType {
        RELOAD,
        STOP,
        START,
        ADD,
        UNKNOWN
    }

    public PermissionType permissionTypeFromString(String arg) {
        if (arg.equalsIgnoreCase(HOVER_PERMISSION_ADD_CMD)) {
            return PermissionType.ADD;
        } else if (arg.equalsIgnoreCase(HOVER_PERMISSION_START_CMD)) {
            return PermissionType.START;
        } else if (arg.equalsIgnoreCase(HOVER_PERMISSION_STOP_CMD)) {
            return PermissionType.STOP;
        } else if (arg.equalsIgnoreCase(HOVER_PERMISSION_RELOAD_CMD)) {
            return PermissionType.RELOAD;
        } else {
            return PermissionType.UNKNOWN;
        }
    }

    public boolean senderHasPermission(CommandSender sender, String arg) {
        if (!(sender instanceof Player)) {
            return false;
        }

        if (sender.isOp() == true) {
            return true;
        }
        
        PermissionType type = permissionTypeFromString(arg);
        switch (type) {
            case RELOAD:
                return sender.hasPermission(HOVER_PERMISSION_RELOAD_KEY) == true;
            case STOP:
                return sender.hasPermission(HOVER_PERMISSION_STOP_KEY) == true;
            case START:
                return sender.hasPermission(HOVER_PERMISSION_START_KEY) == true;
            case ADD:
                return sender.hasPermission(HOVER_PERMISSION_ADD_KEY) == true;
            default:
            case UNKNOWN:
                return false;
        }
    }
}
