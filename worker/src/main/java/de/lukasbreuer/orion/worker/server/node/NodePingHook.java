package de.lukasbreuer.orion.worker.server.node;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.lukasbreuer.orion.worker.event.node.NodePingEvent;
import de.lukasbreuer.orion.worker.packet.outgoing.node.PacketOutgoingPong;
import de.lukasbreuer.signar.EventHook;
import de.lukasbreuer.signar.Hook;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__({@Inject}))
public final class NodePingHook implements Hook {
  @EventHook
  private void nodePing(NodePingEvent event) {
    event.client().sendPacket(new PacketOutgoingPong(event.value()));
  }
}
