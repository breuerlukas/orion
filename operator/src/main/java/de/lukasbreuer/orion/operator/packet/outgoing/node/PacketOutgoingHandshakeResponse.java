package de.lukasbreuer.orion.operator.packet.outgoing.node;

import de.lukasbreuer.orion.core.packet.PacketBuffer;
import de.lukasbreuer.orion.core.packet.PacketOutgoing;

public final class PacketOutgoingHandshakeResponse extends PacketOutgoing {
  private final boolean success;

  public PacketOutgoingHandshakeResponse(boolean success) {
    super(0x01);
    this.success = success;
  }

  @Override
  public void write(PacketBuffer buffer) throws Exception {
    buffer.raw().writeBoolean(success);
  }
}
