package de.lukasbreuer.orion.operator.server.node;

import de.lukasbreuer.orion.operator.OperatorDistribution;
import de.lukasbreuer.orion.operator.client.OperatorClientRegistry;
import de.lukasbreuer.orion.operator.event.node.NodeDisconnectEvent;
import de.lukasbreuer.signar.EventHook;
import de.lukasbreuer.signar.Hook;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class NodeDisconnectHook implements Hook {
  private final OperatorClientRegistry clientRegistry;
  private final OperatorDistribution distribution;

  @EventHook
  private void nodeDisconnect(NodeDisconnectEvent event) {
    var client = event.client();
    clientRegistry.unregisterClient(client);
    distribution.notifyEnvironment();
    var reason = event.reason();
    if (reason.isConnectionFailed()) {
      return;
    }
    if (reason.isShutdown()) {
      System.out.println("The node " + event.client().hostname() + " has disconnected");
    } else {
      System.err.println("The node " + event.client().hostname() + " has timed out");
    }
  }
}
