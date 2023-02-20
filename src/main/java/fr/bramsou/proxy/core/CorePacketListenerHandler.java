package fr.bramsou.proxy.core;

import fr.bramsou.netty.messaging.handler.PacketListenerHandler;
import fr.bramsou.netty.messaging.registry.PacketRegistryState;

public interface CorePacketListenerHandler extends PacketListenerHandler {

    PacketRegistryState CORE_STATE = new PacketRegistryState("CORE").register(0x00, CorePacket.class, CorePacket::new);

    void handle(CorePacket packet);
}
