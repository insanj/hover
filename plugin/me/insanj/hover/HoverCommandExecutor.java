package me.insanj.hover;

import java.util.ArrayList;

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
		return false;
    }

}
