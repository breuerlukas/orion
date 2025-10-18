package de.lukasbreuer.orion.operator.server.node;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.lukasbreuer.orion.operator.event.node.NodePongEvent;
import de.lukasbreuer.signar.EventHook;
import de.lukasbreuer.signar.Hook;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__({@Inject}))
public final class NodePongHook implements Hook {
  private final NodePingCache pingCache;

  @EventHook
  private void nodePong(NodePongEvent event) {
    var client = event.client();
    if (!pingCache.pingExists(client)) {
      return;
    }
    if (pingCache.findPingValue(client) == event.value()) {
      pingCache.removeNodePing(client);
    }
  }
}
