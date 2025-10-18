package de.lukasbreuer.orion.worker.event.node;

import de.lukasbreuer.orion.worker.client.WorkerOperatorClient;
import de.lukasbreuer.signar.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(staticName = "create")
public final class NodePingEvent extends Event {
  private final WorkerOperatorClient client;
  private final int value;
}