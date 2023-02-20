package fr.bramsou.proxy.core;

import fr.bramsou.netty.messaging.packet.MessagingPacket;
import fr.bramsou.netty.messaging.packet.PacketBuffer;

import java.util.Objects;

public class CorePacket implements MessagingPacket<CorePacketListenerHandler> {

    private final int random;

    public CorePacket(PacketBuffer buffer) {
        this.random = buffer.readVarInt();
    }

    public CorePacket(int random) {
        this.random = random;
    }

    @Override
    public void serialize(PacketBuffer buffer) {
        buffer.writeVarInt(this.random);
    }

    @Override
    public void read(CorePacketListenerHandler handler) {
        handler.handle(this);
    }

    public int getRandom() {
        return random;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CorePacket that = (CorePacket) o;
        return random == that.random;
    }

    @Override
    public int hashCode() {
        return Objects.hash(random);
    }

    @Override
    public String toString() {
        return "CorePacket{" +
                "random=" + random +
                '}';
    }
}
