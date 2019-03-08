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
    public static final String HOVER_PERMISSION_RELOAD_KEY = "hover.reload";
    public static final String HOVER_PERMISSION_STOP_KEY = "hover.stop";
    public static final String HOVER_PERMISSION_START_KEY = "hover.start";
    public static final String HOVER_PERMISSION_ADD_KEY = "hover.add";

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

        // register for permissions
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        Set<Permission> existingPermissions = pluginManager.getPermissions();
        Set<Permission> hoverPermissions = hoverPermissions();
        for (Permission p : hoverPermissions) {
            if (!existingPermissions.contains(p)) {
                pluginManager.addPermission(p);
            }
        }

        // setup /hover command executors
        executor = new HoverCommandExecutor(this);
        getCommand("hover").setExecutor(executor);
        
        // setup listener for chat events to add hover overlay
        listener = new HoverPlayerChatListener(this);
        pluginManager.registerEvents(listener, this); 
    }

    public static Set<Permission> hoverPermissions() {
        Set<Permission> permissionSet = new HashSet<Permission>();
        Permission addPermission = new Permission("hover.add");
        permissionSet.add(addPermission);
        
        Permission startPermission = new Permission("hover.start");
        permissionSet.add(startPermission);

        Permission stopPermission = new Permission("hover.stop");
        permissionSet.add(stopPermission);

        Permission reloadPermission = new Permission("hover.reload");
        permissionSet.add(reloadPermission);

        return permissionSet;
    }

    public static boolean senderHasPermission(CommandSender sender, PermissionType type) {
        if (!(sender instanceof Player)) {
            return false;
        }

        return playerHasPermission((Player)sender, type);
    }

    public static boolean playerHasPermission(CommandSender player, PermissionType type) {
        if (player.isOp() == true) {
            return true;
        }

        switch (type) {
            case RELOAD:
                return player.hasPermission(HOVER_PERMISSION_RELOAD_KEY);
            case STOP:
                return player.hasPermission(HOVER_PERMISSION_STOP_KEY);
            case START:
                return player.hasPermission(HOVER_PERMISSION_START_KEY);
            case ADD:
                return player.hasPermission(HOVER_PERMISSION_ADD_KEY);
        }

        return false;
    }
}