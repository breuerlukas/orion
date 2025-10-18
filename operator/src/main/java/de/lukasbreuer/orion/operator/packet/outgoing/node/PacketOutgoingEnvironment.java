package de.lukasbreuer.orion.operator.packet.outgoing.node;

import de.lukasbreuer.orion.core.packet.PacketBuffer;
import de.lukasbreuer.orion.core.packet.PacketOutgoing;

import java.util.List;

public final class PacketOutgoingEnvironment extends PacketOutgoing {
  private final String yourAddress;
  private final List<String> neighborAddresses;

  public PacketOutgoingEnvironment(
    String yourAddress, List<String> neighborAddresses
  ) {
    super(0x05);
    this.yourAddress = yourAddress;
    this.neighborAddresses = neighborAddresses;
  }

  @Override
  public void write(PacketBuffer buffer) throws Exception {
    buffer.writeString(yourAddress);
    buffer.writeVarInt(neighborAddresses.size());
    for (var neighbor : neighborAddresses) {
      buffer.writeString(neighbor);
    }
  }
}
