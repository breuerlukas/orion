package de.lukasbreuer.orion.worker;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import de.lukasbreuer.orion.core.packet.PacketEventRepository;
import de.lukasbreuer.orion.core.packet.PacketRegistry;
import de.lukasbreuer.orion.worker.client.WorkerOperatorClient;
import de.lukasbreuer.orion.worker.event.node.NodeEnvironmentEvent;
import de.lukasbreuer.orion.worker.event.node.NodeHandshakeResponseEvent;
import de.lukasbreuer.orion.worker.event.node.NodePingEvent;
import de.lukasbreuer.orion.worker.packet.incoming.node.PacketIncomingEnvironment;
import de.lukasbreuer.orion.worker.packet.incoming.node.PacketIncomingHandshakeResponse;
import de.lukasbreuer.orion.worker.packet.incoming.node.PacketIncomingPing;
import de.lukasbreuer.orion.worker.packet.outgoing.node.PacketOutgoingDisconnect;
import de.lukasbreuer.orion.worker.packet.outgoing.node.PacketOutgoingHandshakeRequest;
import de.lukasbreuer.orion.worker.server.node.NodeDisconnectHook;
import de.lukasbreuer.orion.worker.server.node.NodeEnvironmentHook;
import de.lukasbreuer.orion.worker.server.node.NodeHandshakeResponseHook;
import de.lukasbreuer.orion.worker.server.node.NodePingHook;
import de.lukasbreuer.signar.HookRegistry;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__({@Inject}))
public final class WorkerDistribution {
  private final Injector injector;
  private final WorkerConfiguration configuration;
  private final PacketRegistry packetRegistry;
  private final HookRegistry hookRegistry;
  private final PacketEventRepository packetEventRepository;
  private final WorkerOperatorClient workerOperatorClient;

  /**
   * Initializes node distribution (registers packets, hooks, events and
   * opens server)
   * @throws Exception
   */
  public void initialize() throws Exception {
    registerPackets();
    registerHooks();
    registerEvents();
    workerOperatorClient.connectAsync(() -> workerOperatorClient.sendPacket(
      new PacketOutgoingHandshakeRequest(configuration.distributionKey())));
  }

  private void registerPackets() throws Exception {
    packetRegistry.registerPacket(PacketIncomingHandshakeResponse.class);
    packetRegistry.registerPacket(PacketIncomingPing.class);
    packetRegistry.registerPacket(PacketIncomingEnvironment.class);
  }

  private void registerHooks() {
    hookRegistry.register(injector.getInstance(NodeDisconnectHook.class));
    hookRegistry.register(injector.getInstance(NodeHandshakeResponseHook.class));
    hookRegistry.register(injector.getInstance(NodePingHook.class));
    hookRegistry.register(injector.getInstance(NodeEnvironmentHook.class));
  }

  private void registerEvents() {
    packetEventRepository.registerEvent(PacketIncomingHandshakeResponse.class,
      (client, packet) -> NodeHandshakeResponseEvent.create(packet.success()));
    packetEventRepository.<WorkerOperatorClient, PacketIncomingPing>registerEvent(
      PacketIncomingPing.class, (client, packet) ->
        NodePingEvent.create(client, packet.value()));
    packetEventRepository.registerEvent(PacketIncomingEnvironment.class,
      (client, packet) -> NodeEnvironmentEvent.create(packet.yourAddress(),
        packet.neighborAddresses()));
  }

  /**
   * Sends farewell greeting
   */
  public void disconnect() {
    workerOperatorClient.sendPacket(new PacketOutgoingDisconnect());
  }
}
