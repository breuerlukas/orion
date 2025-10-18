package de.lukasbreuer.orion.operator.event.node;

import de.lukasbreuer.orion.operator.client.OperatorClient;
import de.lukasbreuer.signar.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(staticName = "create")
public final class NodePongEvent extends Event {
  private final OperatorClient client;
  private final int value;
}
