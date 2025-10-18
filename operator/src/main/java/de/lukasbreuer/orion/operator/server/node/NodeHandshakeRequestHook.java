package de.lukasbreuer.orion.operator.server.node;

import de.lukasbreuer.orion.operator.OperatorDistribution;
import de.lukasbreuer.orion.operator.OperatorDistributionConfiguration;
import de.lukasbreuer.orion.operator.client.OperatorClient;
import de.lukasbreuer.orion.operator.client.OperatorClientRegistry;
import de.lukasbreuer.orion.operator.event.node.NodeHandshakeRequestEvent;
import de.lukasbreuer.orion.operator.packet.outgoing.node.PacketOutgoingHandshakeResponse;
import de.lukasbreuer.signar.EventHook;
import de.lukasbreuer.signar.Hook;
import lombok.RequiredArgsConstructor;

import java.net.InetSocketAddress;

@RequiredArgsConstructor(staticName = "create")
public final class NodeHandshakeRequestHook implements Hook {
  private final OperatorDistributionConfiguration configuration;
  private final OperatorClientRegistry clientRegistry;
  private final OperatorDistribution distribution;

  @EventHook
  private void nodeHandshake(NodeHandshakeRequestEvent event) {
    if (!event.key().equals(configuration.distributionKey())) {
      event.channel().writeAndFlush(new PacketOutgoingHandshakeResponse(false));
      event.channel().close();
      return;
    }
    var channel = event.channel();
    var channelHost = ((InetSocketAddress) channel.remoteAddress())
      .getAddress().getHostAddress();
    var client = OperatorClient.of(channelHost, channel);
    clientRegistry.registerClient(client);
    client.authorize();
    event.channel().writeAndFlush(new PacketOutgoingHandshakeResponse(true));
    distribution.notifyEnvironment();
    System.out.println("Node " + channelHost + " has connected");
  }
}