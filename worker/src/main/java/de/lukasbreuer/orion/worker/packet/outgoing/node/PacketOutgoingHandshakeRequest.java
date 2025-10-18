package de.lukasbreuer.orion.worker.packet.outgoing.node;

import de.lukasbreuer.orion.core.packet.PacketBuffer;
import de.lukasbreuer.orion.core.packet.PacketOutgoing;

public final class PacketOutgoingHandshakeRequest extends PacketOutgoing {
  private final String key;

  public PacketOutgoingHandshakeRequest(String key) {
    super(0x00);
    this.key = key;
  }

  @Override
  public void write(PacketBuffer buffer) throws Exception {
    buffer.writeString(key);
  }
}
