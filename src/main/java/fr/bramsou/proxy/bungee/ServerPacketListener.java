package fr.bramsou.proxy.bungee;

import com.google.gson.JsonObject;
import fr.bramsou.netty.messaging.MessagingNetwork;
import fr.bramsou.netty.messaging.MessagingOptions;
import fr.bramsou.netty.messaging.handler.MessagingPacketListenerHandler;
import fr.bramsou.netty.messaging.packet.impl.CompressionPacket;
import fr.bramsou.netty.messaging.packet.impl.JsonMessagePacket;
import fr.bramsou.netty.messaging.packet.impl.TokenPacket;
import fr.bramsou.netty.messaging.util.DisconnectReason;
import fr.bramsou.proxy.core.CorePacket;
import fr.bramsou.proxy.core.CorePacketListener;
import fr.bramsou.proxy.core.CorePacketListenerHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class ServerPacketListener implements MessagingPacketListenerHandler {

    private final BungeePlugin plugin;
    private final MessagingNetwork network;

    private ServerInfo serverInfo;

    public ServerPacketListener(BungeePlugin plugin, MessagingNetwork network) {
        this.plugin = plugin;
        this.network = network;
    }

    @Override
    public void handle(TokenPacket packet) {
        final int port = packet.getPort();
        this.serverInfo = ProxyServer.getInstance().getServers().values().stream()
                .filter(server -> ((InetSocketAddress) server.getSocketAddress()).getPort() == port).findAny().orElse(null);
        if (this.serverInfo == null) {
            this.network.close(DisconnectReason.UNKNOWN_SERVER);
            return;
        }

        if (packet.getToken() != null && packet.getToken().equals("Password123")) {
            this.network.sendPacket(new CompressionPacket(MessagingOptions.COMPRESSION_THRESHOLD));
            final JsonObject object = new JsonObject();
            object.addProperty("timestamp", System.currentTimeMillis());
            object.addProperty("isTrue", true);
            this.network.sendPacket(new JsonMessagePacket(object));
            ProxyServer.getInstance().getLogger().info(serverInfo.getName() + " has connected to messaging system");
            // CHANGING STATE
            ProxyServer.getInstance().getScheduler().schedule(plugin, () -> {
                this.network.setState(CorePacketListenerHandler.CORE_STATE, new CorePacketListener(this.network));
                this.network.sendPacket(new CorePacket(ThreadLocalRandom.current().nextInt(100)));
            }, 1L, TimeUnit.SECONDS);
            return;
        }

        this.network.close(DisconnectReason.INCORRECT_TOKEN);
    }

    @Override
    public MessagingNetwork getNetwork() {
        return this.network;
    }

    public ServerInfo getServerInfo() {
        return serverInfo;
    }
}
