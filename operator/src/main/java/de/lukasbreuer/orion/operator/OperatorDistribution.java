package de.lukasbreuer.orion.operator;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import de.lukasbreuer.orion.operator.client.OperatorClient;
import de.lukasbreuer.orion.operator.client.OperatorClientRegistry;
import de.lukasbreuer.orion.operator.event.node.NodeDisconnectEvent;
import de.lukasbreuer.orion.operator.event.node.NodePongEvent;
import de.lukasbreuer.orion.core.packet.PacketEventRepository;
import de.lukasbreuer.orion.core.packet.PacketOutgoing;
import de.lukasbreuer.orion.core.packet.PacketRegistry;
import de.lukasbreuer.orion.operator.packet.incoming.node.PacketIncomingDisconnect;
import de.lukasbreuer.orion.operator.packet.incoming.node.PacketIncomingHandshakeRequest;
import de.lukasbreuer.orion.operator.packet.incoming.node.PacketIncomingPong;
import de.lukasbreuer.orion.operator.packet.outgoing.node.PacketOutgoingEnvironment;
import de.lukasbreuer.orion.operator.server.OperatorServer;
import de.lukasbreuer.orion.operator.server.node.NodeDisconnectHook;
import de.lukasbreuer.orion.operator.server.node.NodeHandshakeRequestHook;
import de.lukasbreuer.orion.operator.server.node.NodePongHook;
import de.lukasbreuer.signar.EventExecutor;
import de.lukasbreuer.signar.HookRegistry;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__({@Inject}))
public final class OperatorDistribution {
  private final Injector injector;
  private final OperatorDistributionConfiguration configuration;
  private final PacketRegistry packetRegistry;
  private final EventExecutor eventExecutor;
  private final HookRegistry hookRegistry;
  private final OperatorClientRegistry clientRegistry;
  private final PacketEventRepository packetEventRepository;
  private OperatorServer operatorServer;

  /**
   * Initializes node distribution (registers packets, hooks, events and
   * opens server)
   * @throws Exception
   */
  public void initialize() throws Exception {
    operatorServer = OperatorServer.create(packetRegistry, eventExecutor,
      clientRegistry, packetEventRepository,
      configuration.distributionPort());
    registerPackets();
    registerHooks();
    registerEvents();
    operatorServer.openAsync(() -> {});
  }

  private void registerPackets() throws Exception {
    packetRegistry.registerPacket(PacketIncomingHandshakeRequest.class);
    packetRegistry.registerPacket(PacketIncomingPong.class);
    packetRegistry.registerPacket(PacketIncomingDisconnect.class);
  }

  private void registerHooks() {
    hookRegistry.register(NodeHandshakeRequestHook.create(configuration,
      clientRegistry, this));
    hookRegistry.register(injector.getInstance(NodePongHook.class));
    hookRegistry.register(NodeDisconnectHook.create(clientRegistry, this));
  }

  private void registerEvents() {
    packetEventRepository.<OperatorClient, PacketIncomingPong>registerEvent(
      PacketIncomingPong.class, (client, packet) ->
        NodePongEvent.create(client, packet.value()));
    packetEventRepository.<OperatorClient, PacketIncomingDisconnect>registerEvent(
      PacketIncomingDisconnect.class, (client, packet) -> NodeDisconnectEvent.create(
        client, NodeDisconnectEvent.DisconnectReason.SHUTDOWN));
  }

  /**
   * Sends PacketOutgoingEnvironment to all registered clients
   */
  public void notifyEnvironment() {
    var clients = clientRegistry.findAllClients()
      .stream().filter(client -> client.state().isAuthorized()).toList();
    for (var client : clients) {
      var neighbors = clients.stream()
        .filter(neighbor -> neighbor != client)
        .map(OperatorClient::hostname)
        .toList();
      client.sendPacket(new PacketOutgoingEnvironment(client.hostname(), neighbors));
    }
  }

  /**
   * Sends an outgoing packet to all registered clients
   * @param packet The packet that will be sent
   */
  public void broadcastPacket(PacketOutgoing packet) {
    operatorServer.broadcastPacket(packet);
  }

  /**
   * Closes server
   */
  public void disconnect() {
    operatorServer.close();
  }
}
