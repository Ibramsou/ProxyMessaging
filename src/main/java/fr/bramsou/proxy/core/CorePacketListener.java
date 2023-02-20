package fr.bramsou.proxy.core;

import fr.bramsou.netty.messaging.MessagingNetwork;

public class CorePacketListener implements CorePacketListenerHandler {

    private final MessagingNetwork network;

    public CorePacketListener(MessagingNetwork network) {
        this.network = network;
    }

    @Override
    public void handle(CorePacket packet) {
        System.out.println("Received random: " + packet.getRandom());
    }

    @Override
    public MessagingNetwork getNetwork() {
        return this.network;
    }
}
