package de.lukasbreuer.orion.worker;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import de.lukasbreuer.orion.core.packet.PacketEventRepository;
import de.lukasbreuer.orion.core.packet.PacketRegistry;
import de.lukasbreuer.orion.worker.client.WorkerOperatorClient;
import de.lukasbreuer.signar.EventExecutor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class WorkerInjectionModule extends AbstractModule {
  @Provides
  @Singleton
  WorkerConfiguration provideDistributionConfiguration() throws Exception {
    return WorkerConfiguration.createAndLoad();
  }

  @Provides
  @Singleton
  WorkerOperatorClient provideWorkerOperatorClient(
    WorkerConfiguration configuration, PacketRegistry packetRegistry,
    EventExecutor eventExecutor, PacketEventRepository packetEventRepository
  ) {
    return WorkerOperatorClient.create(configuration, packetRegistry,
      eventExecutor, packetEventRepository, configuration.operatorHostname(),
      configuration.operatorDistributionPort());
  }
}
