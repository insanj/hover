package me.insanj.hover;

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

import net.minecraft.server.v1_13_R2.IChatBaseComponent;
import net.minecraft.server.v1_13_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_13_R2.PacketPlayOutChat;
import net.minecraft.server.v1_13_R2.PlayerConnection;

public class HoverPlayerChatListener implements Listener {
    public final Hover plugin;
    public HoverPlayerChatListener(Hover plugin) {
        this.plugin = plugin;
    }
    
    /*
    @EventHandler
    public void onCommandPreprocess(PlayerChatEvent event) {
        Player sender = event.getPlayer();

        if (event.getMessage().equalsIgnoreCase("")) {
            event.setCancelled(true);
        }
    }*/

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String msg = event.getMessage();

        for (Player recipient : event.getRecipients​()) {
            sendMessage(recipient, msg, "Hello world!");
        }

        event.setCancelled(true);
    }
    
    public static PacketPlayOutChat createPacketPlayOutChat(String s){
        IChatBaseComponent comp = ChatSerializer.a(s);
        return new PacketPlayOutChat(comp);
    }

    public static void SendJsonMessage(Player p, String s){
        ( (CraftPlayer)p ).getHandle().playerConnection.sendPacket( createPacketPlayOutChat(s) );
    }
    
    public void sendMessage(Player player, String message, String hoverText) {
        SendJsonMessage(player, "{text:\"" + message + "\",hoverEvent:{action:show_text,value:\"" + hoverText + "\"}}");
        /*SendJsonMessage(player,
            "{text:\"" + message + "\",clickEvent:{action:open_url,value:\"" +
            url + "\"}}");*/
         //         /tellraw @a {text:"HOVER",hoverEvent:{action:show_text,value:"This is a test"}}
    }
}