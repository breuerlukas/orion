package de.lukasbreuer.orion.worker.packet.outgoing.node;

import de.lukasbreuer.orion.core.packet.PacketBuffer;
import de.lukasbreuer.orion.core.packet.PacketOutgoing;

public final class PacketOutgoingPong extends PacketOutgoing {
  private final int value;

  public PacketOutgoingPong(int value) {
    super(0x03);
    this.value = value;
  }

  @Override
  public void write(PacketBuffer buffer) throws Exception {
    buffer.writeVarInt(value);
  }
}
