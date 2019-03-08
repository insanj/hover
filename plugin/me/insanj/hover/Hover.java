package me.insanj.hover;

import java.util.Set;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;

public class Hover extends JavaPlugin {
    public static final String HOVER_PERMISSION_ADD_KEY = "hoveradd";
    public static final String HOVER_PERMISSION_START_KEY = "hoverstart";
    public static final String HOVER_PERMISSION_STOP_KEY = "hoverstop";
    public static final String HOVER_PERMISSION_RELOAD_KEY = "hoverreload";

    public static enum PermissionType {
        RELOAD,
        STOP,
        START,
        ADD
    }

    public HoverPlayerChatListener listener;
    public HoverCommandExecutor executor;

	@Override
	public void onEnable() {
        saveDefaultConfig();
        reloadConfig();

        // setup /hover command executors
        executor = new HoverCommandExecutor(this);
        getCommand("hover").setExecutor(executor);
        
        // setup listener for chat events to add hover overlay
        listener = new HoverPlayerChatListener(this);
        Bukkit.getServer().getPluginManager().registerEvents(listener, this); 
    }

    public static boolean senderHasPermission(CommandSender sender, PermissionType type) {
        if (!(sender instanceof Player)) {
            return false;
        }

        switch (type) {
            case RELOAD:
                return sender.hasPermission(HOVER_PERMISSION_RELOAD_KEY) == true;
            case STOP:
                return sender.hasPermission(HOVER_PERMISSION_STOP_KEY) == true;
            case START:
                return sender.hasPermission(HOVER_PERMISSION_START_KEY) == true;
            case ADD:
                return sender.hasPermission(HOVER_PERMISSION_ADD_KEY) == true;
        }

        System.out.println("WHAT?");
        return false;
    }
}