package de.lukasbreuer.orion.operator.channel;

import de.lukasbreuer.orion.operator.client.OperatorClient;
import de.lukasbreuer.orion.operator.client.OperatorClientRegistry;
import de.lukasbreuer.orion.operator.event.node.NodeDisconnectEvent;
import de.lukasbreuer.orion.operator.event.node.NodeHandshakeRequestEvent;
import de.lukasbreuer.orion.core.packet.PacketEventRepository;
import de.lukasbreuer.orion.core.packet.PacketIncoming;
import de.lukasbreuer.orion.operator.packet.incoming.node.PacketIncomingHandshakeRequest;
import de.lukasbreuer.signar.EventExecutor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor(staticName = "create")
public final class ChannelInbox extends SimpleChannelInboundHandler<PacketIncoming> {
  private final EventExecutor eventExecutor;
  private final OperatorClientRegistry clientRegistry;
  private final PacketEventRepository packetEventRepository;

  @Override
  protected void channelRead0(
    ChannelHandlerContext context, PacketIncoming incomingPacket
  ) {
    var client = findClientByContext(context);
    if (client.isEmpty() || client.get().state().isUnauthorized()) {
      processUnauthorizedChannel(context, incomingPacket);
      return;
    }
    processAuthorizedChannel(client.get(), incomingPacket);
  }

  private void processUnauthorizedChannel(
    ChannelHandlerContext context, PacketIncoming incomingPacket
  ) {
    if (incomingPacket instanceof PacketIncomingHandshakeRequest packet) {
      processHandshakeRequestPacket(context, packet);
    }
  }

  private void processHandshakeRequestPacket(
    ChannelHandlerContext context, PacketIncomingHandshakeRequest packet
  ) {
    eventExecutor.execute(NodeHandshakeRequestEvent.create(context.channel(),
      packet.key()));
  }

  private void processAuthorizedChannel(
    OperatorClient client, PacketIncoming incomingPacket
  ) {
    var event = packetEventRepository.findEvent(incomingPacket.getClass());
    if (event.isEmpty()) {
      return;
    }
    eventExecutor.execute(event.get().process(client, incomingPacket));
  }

  @Override
  public void channelInactive(ChannelHandlerContext context) {
    var client = findClientByContext(context);
    if (client.isEmpty()) {
      return;
    }
    eventExecutor.execute(NodeDisconnectEvent.create(client.get(),
      NodeDisconnectEvent.DisconnectReason.TIME_OUT));
  }

  private Optional<OperatorClient> findClientByContext(ChannelHandlerContext context) {
    var channel = context.channel();
    return clientRegistry.findAllClients().stream()
      .filter(client -> client.channel().equals(channel))
      .findFirst();
  }
}

