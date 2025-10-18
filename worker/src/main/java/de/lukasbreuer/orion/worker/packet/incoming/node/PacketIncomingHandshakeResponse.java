package de.lukasbreuer.orion.worker.packet.incoming.node;

import de.lukasbreuer.orion.core.packet.PacketBuffer;
import de.lukasbreuer.orion.core.packet.PacketIncoming;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public final class PacketIncomingHandshakeResponse extends PacketIncoming {
  private boolean success;

  public PacketIncomingHandshakeResponse() {
    super(0x01);
  }

  @Override
  public void read(PacketBuffer buffer) throws Exception {
    success = buffer.raw().readBoolean();
  }
}
