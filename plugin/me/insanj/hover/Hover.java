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
    public HoverPlayerChatListener listener;
    public HoverCommandExecutor executor;
    public HoverMessageComposer composer;

	@Override
	public void onEnable() {
        saveDefaultConfig();
        reloadConfig();

        // setup /hover command executors
        executor = new HoverCommandExecutor(this);
        getCommand("hover").setExecutor(executor);

        // setup composer for messages
        composer = new HoverMessageComposer(this);

        // setup listener for chat events to add hover overlay
        listener = new HoverPlayerChatListener(this);
        Bukkit.getServer().getPluginManager().registerEvents(listener, this); 
    }
}