package me.insanj.hover;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;

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

        executor = new HoverCommandExecutor(this);
        getCommand("hover").setExecutor(executor);
        
        listener = new HoverPlayerChatListener(this);
        Bukkit.getServer().getPluginManager().registerEvents(listener, this); 
    }

    public static boolean playerHasPermission(Player player, PermissionType type) {
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