package de.lukasbreuer.orion.core.packet;

public abstract class PacketIncoming extends Packet {
  protected PacketIncoming(int id) {
    super(id);
  }

  /**
   * Deserializes an incoming packet
   * @param buffer The buffer from which the information is read out
   * @throws Exception
   */
  public abstract void read(PacketBuffer buffer) throws Exception;
}
