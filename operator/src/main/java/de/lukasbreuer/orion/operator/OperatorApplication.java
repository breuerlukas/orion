package de.lukasbreuer.orion.operator;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.lukasbreuer.orion.operator.server.node.NodePingSchedule;

public class OperatorApplication {
  public static void main(String[] args) throws Exception {
    System.setProperty("jdk.httpclient.allowRestrictedHeaders",
      "host,connection,content-length");
    var injector = Guice.createInjector(OperatorInjectionModule.create());
    System.out.println("Initializing Operator");
    setupDistribution(injector);
    System.out.println("Successfully booted Operator");
  }

  private static void setupDistribution(Injector injector) throws Exception {
    var distribution = injector.getInstance(OperatorDistribution.class);
    distribution.initialize();
    var nodePingScheduler = injector.getInstance(NodePingSchedule.class);
    nodePingScheduler.start();
  }
}
