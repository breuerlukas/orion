package de.lukasbreuer.orion.operator.channel;

import de.lukasbreuer.orion.operator.client.OperatorClientRegistry;
import de.lukasbreuer.orion.core.packet.PacketDecoder;
import de.lukasbreuer.orion.core.packet.PacketEncoder;
import de.lukasbreuer.orion.core.packet.PacketEventRepository;
import de.lukasbreuer.orion.core.packet.PacketRegistry;
import de.lukasbreuer.signar.EventExecutor;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class ChannelEquipment extends ChannelInitializer<Channel> {
  private final PacketRegistry packetRegistry;
  private final EventExecutor eventExecutor;
  private final OperatorClientRegistry distributionClientRegistry;
  private final PacketEventRepository packetEventRepository;

  @Override
  protected void initChannel(Channel channel) {
    equipChannel(channel);
  }

  private void equipChannel(Channel channel) {
    channel.pipeline().addLast("channel-encoder", PacketEncoder.create());
    channel.pipeline().addLast("channel-decoder", PacketDecoder.create(
      packetRegistry));
    channel.pipeline().addLast("chanel-inbox", ChannelInbox.create(eventExecutor,
      distributionClientRegistry, packetEventRepository));
  }
}