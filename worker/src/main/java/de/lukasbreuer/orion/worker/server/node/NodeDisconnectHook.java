package de.lukasbreuer.orion.worker.server.node;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.lukasbreuer.orion.worker.WorkerConfiguration;
import de.lukasbreuer.orion.worker.client.WorkerOperatorClient;
import de.lukasbreuer.orion.worker.event.node.NodeDisconnectEvent;
import de.lukasbreuer.orion.worker.packet.outgoing.node.PacketOutgoingHandshakeRequest;
import de.lukasbreuer.signar.EventHook;
import de.lukasbreuer.signar.Hook;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__({@Inject}))
public final class NodeDisconnectHook implements Hook {
  private final WorkerConfiguration configuration;
  private final WorkerOperatorClient workerOperatorClient;

  @EventHook
  private void nodeDisconnect(NodeDisconnectEvent event) {
    var reason = event.reason();
    if (reason.isConnectionFailed()) {
      System.err.println("The connection to the operator failed");
    } else if (reason.isTimeOut()) {
      System.err.println("The connection to the operator timed out");
    }
    workerOperatorClient.connectAsync(() -> workerOperatorClient.sendPacket(
      new PacketOutgoingHandshakeRequest(configuration.distributionKey())));
  }
}
