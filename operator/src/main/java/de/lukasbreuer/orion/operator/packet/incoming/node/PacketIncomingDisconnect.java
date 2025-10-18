package de.lukasbreuer.orion.operator.packet.incoming.node;

import de.lukasbreuer.orion.core.packet.PacketBuffer;
import de.lukasbreuer.orion.core.packet.PacketIncoming;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public final class PacketIncomingDisconnect extends PacketIncoming {
  public PacketIncomingDisconnect() {
    super(0x04);
  }

  @Override
  public void read(PacketBuffer buffer) throws Exception {

  }
}
