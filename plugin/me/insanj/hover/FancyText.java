/*
    FancyText
    written by ResultStatic, Oct 28 2014
    https://bukkit.org/threads/ichatbasecomponent-help.318790/
*/
package me.insanj.hover;

import java.util.LinkedHashMap;

import net.minecraft.server.v1_13_R2.EnumClickAction;

public class FancyText {
 
 
    LinkedHashMap<String, MessageComponent> message = new LinkedHashMap<String, MessageComponent>();
 
 
    public FancyText addText(String text){
        this.message.put(text, new MessageComponent(text, null, null));
        return this;
    }
 
    public FancyText addClickableLink(String text,String link){
        this.message.put(link, new MessageComponent(text, link, EnumClickAction.OPEN_URL));
        return null;
    }
 
    public FancyText addRunnableCommand(String text, String command){
 
        this.message.put(text, new MessageComponent(text, command, EnumClickAction.RUN_COMMAND));
        return this;
    }
 
    public FancyText addChatSuggestion(String text,String suggestion){
 
        this.message.put(text, new MessageComponent(text, suggestion, EnumClickAction.SUGGEST_COMMAND));
 
        return this;
        }
 
 
    public FancyText addHoverEvent(String text, String hover){
 
        this.message.put(text, new MessageComponent(text,hover ,EnumHoverAction.SHOW_TEXT));
 
        return this;
        }
 
 
 
 
    public void sendToPlayer(Player player){
        ChatComponentText master = new ChatComponentText("");
        for (String text: message.keySet()){
            for (IChatBaseComponent m: message.get(text).compile()) {
                master.a(m);
                }
   
            }
        Nms.getCraftPlayer(player).playerConnection.sendPacket(new PacketPlayOutChat(master));
        }
 
    public void sendToAllPlayers(){
        for (Player player: Bukkit.getOnlinePlayers()){
            this.sendToPlayer(player);
        }
    }
    public class MessageComponent {
 
        Enum<?> e;
        String data;
        String text;
        IChatBaseComponent[] chat;
 
        public MessageComponent(String text, String data, Enum<?> e){
        this.e = e;
        this.text = text;
        this.data = data;
        chat = CraftChatMessage.fromString(text);
        }
 
 
 
 
 
        public IChatBaseComponent[] compile(){
            for (IChatBaseComponent c: chat){
            if (data == null || e == null){
   
                return chat;
            }
                if (e instanceof EnumClickAction){
                c.getChatModifier().setChatClickable(new ChatClickable((EnumClickAction) e, data));
                }else if (e instanceof EnumHoverAction){
                    c.getChatModifier().a((new ChatHoverable((EnumHoverAction)e, new ChatComponentText(data))));
                }
            }
   
            return chat;
        }
 
    }
 
 
}