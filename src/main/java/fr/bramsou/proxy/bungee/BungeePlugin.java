package fr.bramsou.proxy.bungee;

import fr.bramsou.netty.messaging.MessagingNetwork;
import fr.bramsou.netty.messaging.handler.PacketListenerHandler;
import fr.bramsou.netty.messaging.registry.PacketRegistryState;
import fr.bramsou.netty.messaging.session.MessagingServerSession;
import fr.bramsou.netty.messaging.session.MessagingSessionListener;
import fr.bramsou.netty.messaging.util.DisconnectReason;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeePlugin extends Plugin {

    private final MessagingSessionListener listener = new MessagingSessionListener() {

        @Override
        public PacketRegistryState getDefaultPacketState(MessagingNetwork network) {
            return MESSAGING_STATE;
        }

        @Override
        public PacketListenerHandler getDefaultPacketListener(MessagingNetwork network) {
            return new ServerPacketListener(BungeePlugin.this, network);
        }

        @Override
        public void connected(MessagingNetwork network) {}

        @Override
        public void disconnected(MessagingNetwork network, DisconnectReason reason, Throwable cause) {
            ProxyServer.getInstance().getLogger().info("Server has disconnected !");
        }
    };

    @Override
    public void onEnable() {
        new MessagingServerSession(this.listener).bindConnection("localhost", 27777);
    }
}
