package de.lukasbreuer.orion.worker.event.node;

import de.lukasbreuer.signar.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(staticName = "create")
public final class NodeHandshakeResponseEvent extends Event {
  private final boolean success;
}
