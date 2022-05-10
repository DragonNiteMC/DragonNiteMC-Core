package com.dragonite.mc.dnmc.core.managers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.google.common.annotations.Beta;
import com.dragonite.mc.dnmc.core.main.DragoniteMC;
import org.bukkit.entity.Player;

import java.util.HashSet;


@Beta
public class NickManager {

    private static NickManager nickManager;
    private final HashSet<Player> nicks = new HashSet<>();

    private NickManager() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(DragoniteMC.plugin, PacketType.Play.Server.CHAT) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer container = event.getPacket();
                PacketType type = event.getPacketType();
            }

            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer container = event.getPacket();
                PacketType type = event.getPacketType();
                if (type == PacketType.Play.Server.CHAT) {
                    StructureModifier<WrappedChatComponent> chatComponents = container.getChatComponents();
                    WrappedChatComponent msg = chatComponents.read(0);
                    if (msg == null) return;
                    String message = msg.getJson();
                    nicks.forEach(nick -> {
                        String replace = message.replace(nick.getName(), nick.getDisplayName());
                        msg.setJson(replace);
                    });
                    chatComponents.write(0, msg);
                }


            }
        });
    }

    public static NickManager getInstance() {
        if (nickManager == null) nickManager = new NickManager();
        return nickManager;
    }

    public HashSet<Player> getNicks() {
        return nicks;
    }


}
