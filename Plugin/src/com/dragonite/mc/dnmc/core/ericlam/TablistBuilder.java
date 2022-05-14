package com.dragonite.mc.dnmc.core.ericlam;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.dragonite.mc.dnmc.core.managers.TabListManager;
import com.dragonite.mc.dnmc.core.main.DragoniteMC;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class TablistBuilder implements TabListManager {

    private PacketContainer packet;

    public TablistBuilder() {
        packet = new PacketContainer(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);
    }

    @Override
    public void setHeader(String header, Player player) {
        packet.getChatComponents().write(0, WrappedChatComponent.fromText(header.replace('&', '§')));
        packet.getChatComponents().write(1, WrappedChatComponent.fromText("§emc.dragonite.net"));

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        } catch (InvocationTargetException | NullPointerException e) {
            DragoniteMC.plugin.getLogger().info("Failed to update header/footer");
            DragoniteMC.plugin.getLogger().info("Try check your configuration");
        }
    }

    @Override
    public void setHeaderFooter(String header, String footer, Player player) {

        packet.getChatComponents().write(0, WrappedChatComponent.fromText(header.replace('&', '§')));
        packet.getChatComponents().write(1, WrappedChatComponent.fromText(footer.replace('&', '§')));

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        } catch (InvocationTargetException | NullPointerException e) {
            DragoniteMC.plugin.getLogger().info("Failed to update header/footer");
            DragoniteMC.plugin.getLogger().info("Try check your configuration");

        }
    }
}
