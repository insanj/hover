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

public class HoverMessageComposer {
    public final Hover plugin;
    public HoverMessageComposer(Hover plugin) {
        this.plugin = plugin;
    }

    public void sendMessage(Player sender, Player player, String message) {
        String jsonMessage = composeMessage(sender, player, message);
        sendJsonMessage(player, jsonMessage);
    }

    public String composeMessage(Player sender, Player player, String message) {
        String hoverText = getHoverTextForPlayer(sender);
        String playerName = getPlayerNameString(sender);
        String jsonString = "[\"\", {\"text\":\"" + playerName + "\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + hoverText + "\"}}, {\"text\":\"" + message + "\"}]";
        return jsonString;
    }

    public String getPlayerNameString(Player player) { // override this to customize player name format
        return "<" + player.getName() + "> ";
    }

    public Map<String, Object> getTooltipStringsForPlayer(Player player) {
        ConfigurationSection configSection = this.plugin.getConfig().getConfigurationSection(player.getName());
        if (configSection == null) {
            return null; // nothing configured for player
        }

        Map<String, Object> tooltipStrings = (Map<String, Object>)configSection.getValues(false);
        if (tooltipStrings == null || tooltipStrings.size() <= 0) {
            return null;
        }

        return tooltipStrings;
    }

    public String getHoverTextForPlayer(Player player) {
        Map<String, Object> tooltipStrings = getTooltipStringsForPlayer(player);
        String hoverText = "";
        for (String tooltipKey : tooltipStrings.keySet()) {
            String tooltipContents = (String)tooltipStrings.get(tooltipKey);
            hoverText += tooltipKey + ": " + tooltipContents + "\n";
        }
        return hoverText.trim();
    }

    public static void sendJsonMessage(Player p, String s) {
        ( (CraftPlayer)p ).getHandle().playerConnection.sendPacket( createPacketPlayOutChat(s) );
    }

    public static PacketPlayOutChat createPacketPlayOutChat(String s) {
        IChatBaseComponent comp = ChatSerializer.a(s);
        return new PacketPlayOutChat(comp);
    }
}