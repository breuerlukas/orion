package de.lukasbreuer.orion.worker.server.node;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.lukasbreuer.orion.worker.event.node.NodeHandshakeResponseEvent;
import de.lukasbreuer.signar.EventHook;
import de.lukasbreuer.signar.Hook;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__({@Inject}))
public final class NodeHandshakeResponseHook implements Hook {
  @EventHook
  private void nodeHandshakeResponse(NodeHandshakeResponseEvent event) {
    System.out.println("Successfully connected to operator");
  }
}
