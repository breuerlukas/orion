package de.lukasbreuer.orion.worker.server.node;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.lukasbreuer.orion.worker.environment.WorkerEnvironment;
import de.lukasbreuer.orion.worker.event.node.NodeEnvironmentEvent;
import de.lukasbreuer.signar.EventHook;
import de.lukasbreuer.signar.Hook;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__({@Inject}))
public final class NodeEnvironmentHook implements Hook {
  private final WorkerEnvironment environment;

  @EventHook
  private void nodeEnvironment(NodeEnvironmentEvent event) {
    var previousState = environment.state();
    environment.update(event.yourAddress(), event.neighborAddresses());
    if (previousState.isWorker() && environment.state().isHead()) {
      System.out.println("This worker is now the operator head");
    } else if (previousState.isHead() && environment.state().isWorker()) {
      System.out.println("This worker is no longer the operator head");
    }
  }
}
