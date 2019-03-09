package me.insanj.hover;

import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.inventory.Inventory;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import net.minecraft.server.v1_13_R2.IChatBaseComponent;
import net.minecraft.server.v1_13_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_13_R2.PacketPlayOutChat;
import net.minecraft.server.v1_13_R2.PlayerConnection;

public class HoverPlayerChatListener implements Listener {
    public final Hover plugin;
    public boolean disabled;
    public HoverPlayerChatListener(Hover plugin) {
        this.plugin = plugin;
        this.disabled = false;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        if (disabled == true) {
            return;
        }

        Player player = event.getPlayer();
        if (plugin.composer.getTooltipStringsForPlayer(player) == null) {
            return; // nothing configured
        }

        String msg = event.getMessage();
        for (Player recipient : event.getRecipients​()) {
            plugin.composer.sendMessage(player, recipient, msg);
        }

        event.setCancelled(true);
    }

}