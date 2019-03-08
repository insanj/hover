package me.insanj.hover;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Hover extends JavaPlugin {
	@Override
	public void onEnable() {
        saveDefaultConfig();
        reloadConfig();

        HoverCommandExecutor executor = new HoverCommandExecutor(this);
        getCommand("hover").setExecutor(executor);
        
        Bukkit.getServer().getPluginManager().registerEvents(new HoverPlayerChatListener(this), this); 
    }
}