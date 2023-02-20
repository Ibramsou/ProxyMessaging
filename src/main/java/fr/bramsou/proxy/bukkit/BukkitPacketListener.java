package fr.bramsou.proxy.bukkit;

import com.google.gson.JsonObject;
import fr.bramsou.netty.messaging.MessagingNetwork;
import fr.bramsou.netty.messaging.handler.MessagingPacketListenerHandler;
import fr.bramsou.netty.messaging.packet.impl.CompressionPacket;
import fr.bramsou.netty.messaging.packet.impl.JsonMessagePacket;
import fr.bramsou.proxy.core.CorePacketListener;
import fr.bramsou.proxy.core.CorePacketListenerHandler;

public class BukkitPacketListener implements MessagingPacketListenerHandler {

    private final MessagingNetwork network;

    public BukkitPacketListener(MessagingNetwork network) {
        this.network = network;
    }

    @Override
    public void handle(CompressionPacket packet) {}

    @Override
    public void handle(JsonMessagePacket packet) {
        JsonObject object = packet.getObject();
        long timestamp = object.get("timestamp").getAsLong();
        boolean isTrue = object.get("isTrue").getAsBoolean();
        System.out.println("Timestamp: " + timestamp);
        System.out.println("isTrue: " + isTrue);
        if (isTrue) {
            this.network.setState(CorePacketListenerHandler.CORE_STATE, new CorePacketListener(this.network));
        }
    }

    @Override
    public MessagingNetwork getNetwork() {
        return this.network;
    }
}
