package de.lukasbreuer.orion.worker.channel;

import de.lukasbreuer.orion.core.packet.PacketEventRepository;
import de.lukasbreuer.orion.core.packet.PacketIncoming;
import de.lukasbreuer.orion.worker.client.WorkerOperatorClient;
import de.lukasbreuer.orion.worker.event.node.NodeDisconnectEvent;
import de.lukasbreuer.signar.EventExecutor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class WorkerChannelInbox extends SimpleChannelInboundHandler<PacketIncoming> {
  private final EventExecutor eventExecutor;
  private final WorkerOperatorClient workerOperatorClient;
  private final PacketEventRepository packetEventRepository;

  @Override
  protected void channelRead0(
    ChannelHandlerContext context, PacketIncoming incomingPacket
  ) {
    if (context.channel() != workerOperatorClient.channel()) {
      return;
    }
    var event = packetEventRepository.findEvent(incomingPacket.getClass());
    if (event.isEmpty()) {
      return;
    }
    eventExecutor.execute(event.get().process(workerOperatorClient, incomingPacket));
  }

  @Override
  public void channelInactive(ChannelHandlerContext context) {
    if (context.channel() != workerOperatorClient.channel()) {
      return;
    }
    eventExecutor.execute(NodeDisconnectEvent.create(
      NodeDisconnectEvent.DisconnectReason.TIME_OUT));
  }
}

