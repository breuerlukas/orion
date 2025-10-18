package de.lukasbreuer.orion.operator.packet.outgoing.node;

import de.lukasbreuer.orion.core.packet.PacketBuffer;
import de.lukasbreuer.orion.core.packet.PacketOutgoing;

public final class PacketOutgoingPing extends PacketOutgoing {
  private final int value;

  public PacketOutgoingPing(int value) {
    super(0x02);
    this.value = value;
  }

  @Override
  public void write(PacketBuffer buffer) throws Exception {
    buffer.writeVarInt(value);
  }
}
