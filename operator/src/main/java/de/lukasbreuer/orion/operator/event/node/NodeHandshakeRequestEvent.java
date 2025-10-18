package de.lukasbreuer.orion.operator.event.node;

import de.lukasbreuer.signar.Event;
import io.netty.channel.Channel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(staticName = "create")
public final class NodeHandshakeRequestEvent extends Event {
  private final Channel channel;
  private final String key;
}
