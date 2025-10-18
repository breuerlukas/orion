package de.lukasbreuer.orion.worker.packet.outgoing.node;

import de.lukasbreuer.orion.core.packet.PacketBuffer;
import de.lukasbreuer.orion.core.packet.PacketOutgoing;

public final class PacketOutgoingDisconnect extends PacketOutgoing {
  public PacketOutgoingDisconnect() {
    super(0x04);
  }

  @Override
  public void write(PacketBuffer buffer) throws Exception {

  }
}

