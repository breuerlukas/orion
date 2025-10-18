package de.lukasbreuer.orion.operator.server;

import de.lukasbreuer.orion.operator.channel.ChannelEquipment;
import de.lukasbreuer.orion.operator.client.OperatorClientRegistry;
import de.lukasbreuer.orion.core.packet.PacketEventRepository;
import de.lukasbreuer.orion.core.packet.PacketOutgoing;
import de.lukasbreuer.orion.core.packet.PacketRegistry;
import de.lukasbreuer.signar.EventExecutor;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@RequiredArgsConstructor(staticName = "create")
public final class OperatorServer {
  private final PacketRegistry packetRegistry;
  private final EventExecutor eventExecutor;
  private final OperatorClientRegistry clientRegistry;
  private final PacketEventRepository packetEventRepository;
  @Getter
  private final int port;
  @Getter
  private Channel channel;
  private EventLoopGroup group;

  /**
   * Opens the distribution server in the background
   * @param callback A future that is called when the opening process is completed
   */
  public void openAsync(Runnable callback) {
    new Thread(() -> open(callback)).start();
  }

  private void open(Runnable callback) {
    open();
    callback.run();
  }

  /**
   * Opens the distribution server
   */
  public void open() {
    group = new NioEventLoopGroup();
    channel = new ServerBootstrap()
      .group(group)
      .channel(NioServerSocketChannel.class)
      .childHandler(ChannelEquipment.create(packetRegistry, eventExecutor,
        clientRegistry, packetEventRepository))
      .bind(port)
      .syncUninterruptibly().channel();
  }

  /**
   * Sends an outgoing packet to all registered clients
   * @param packet The packet that will be sent
   */
  public void broadcastPacket(PacketOutgoing packet) {
    for (var client : clientRegistry.findAllClients()) {
      client.sendPacket(packet);
    }
  }

  /**
   * Closes the server
   */
  public void close() {
    group.shutdownGracefully();
    channel.close();
  }
}
