package de.lukasbreuer.orion.core.packet;

import de.lukasbreuer.signar.Event;

public interface PacketEvent<T> {
  /**
   * Creates a new event when packet is received
   * @param client The client that send the packet
   * @param packet The packet that has been received
   * @return The created event
   */
  Event process(T client, PacketIncoming packet);
}
