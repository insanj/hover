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
    public HoverPlayerChatListener(Hover plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String msg = event.getMessage();
        String hoverText = getHoverTextForPlayer(player);

        for (Player recipient : event.getRecipients​()) {
            sendMessage(recipient, msg, hoverText);
        }

        event.setCancelled(true);
    }

    public String getHoverTextForPlayer(Player player) {
        ConfigurationSection configSection = this.plugin.getConfig().getConfigurationSection(player.getUniqueId().toString());
        if (configSection == null) {
            return player.getName(); // nothing configured for player
        }

        Map<String, Object> tooltipStrings = (Map<String, Object>)configSection.getValues(false);
        if (tooltipStrings == null || tooltipStrings.size() <= 0) {
            return player.getName(); // nothing configured for player
        }

        String hoverText = "";
        for (String tooltipKey : tooltipStrings.keySet()) {
            String tooltipContents = (String)tooltipStrings.get(tooltipKey);
            hoverText += tooltipKey + ": " + tooltipContents + "\n";
        }
        return hoverText.trim();
    }

    public void sendMessage(Player player, String message, String hoverText) {
        String formattedMessage = "<" + player.getName() + "> " + message;
        String jsonString = "{\"text\":\"" + formattedMessage + "\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + hoverText + "\"}}";
        sendJsonMessage(player, jsonString);
    }

    public static void sendJsonMessage(Player p, String s) {
        ( (CraftPlayer)p ).getHandle().playerConnection.sendPacket( createPacketPlayOutChat(s) );
    }

    public static PacketPlayOutChat createPacketPlayOutChat(String s) {
        IChatBaseComponent comp = ChatSerializer.a(s);
        return new PacketPlayOutChat(comp);
    }
}