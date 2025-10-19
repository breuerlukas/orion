package de.lukasbreuer.orion.operator;

import com.google.inject.AbstractModule;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class OperatorInjectionModule extends AbstractModule {
  @Override
  protected void configure() {
    install(OperatorDistributionInjectionModule.create());
  }
}
