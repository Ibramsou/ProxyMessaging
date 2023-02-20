package fr.bramsou.proxy.bukkit;

import fr.bramsou.netty.messaging.MessagingNetwork;
import fr.bramsou.netty.messaging.handler.PacketListenerHandler;
import fr.bramsou.netty.messaging.packet.impl.TokenPacket;
import fr.bramsou.netty.messaging.registry.PacketRegistryState;
import fr.bramsou.netty.messaging.session.MessagingClientSession;
import fr.bramsou.netty.messaging.session.MessagingSessionListener;
import fr.bramsou.netty.messaging.util.DisconnectReason;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitPlugin extends JavaPlugin {

    private final MessagingSessionListener listener = new MessagingSessionListener() {
        @Override
        public PacketRegistryState getDefaultPacketState(MessagingNetwork network) {
            return MESSAGING_STATE;
        }

        @Override
        public PacketListenerHandler getDefaultPacketListener(MessagingNetwork network) {
            return new BukkitPacketListener(network);
        }

        @Override
        public void connected(MessagingNetwork network) {
            System.out.println("CONNECTED !");
            network.sendPacket(new TokenPacket("Password123", Bukkit.getPort()));
        }

        @Override
        public void disconnected(MessagingNetwork network, DisconnectReason reason, Throwable cause) {
            final String message = reason == DisconnectReason.EXCEPTION_CAUGHT ? cause.getMessage() : reason.getMessage();
            Bukkit.getLogger().info("Server closed for: " + message);
        }
    };

    @Override
    public void onEnable() {
        final MessagingClientSession session = new MessagingClientSession(this.listener);
        session.setAutoReconnect(true);
        session.setReconnectTime(5000);
        session.createConnection("localhost", 27777);
    }
}
