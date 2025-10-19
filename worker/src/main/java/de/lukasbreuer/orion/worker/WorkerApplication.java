package de.lukasbreuer.orion.worker;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class WorkerApplication {
  public static void main(String[] args) throws Exception {
    System.setProperty("jdk.httpclient.allowRestrictedHeaders",
      "host,connection,content-length");
    var injector = Guice.createInjector(WorkerInjectionModule.create());
    System.out.println("Initializing Worker");
    setupDistribution(injector);
    System.out.println("Successfully booted Worker");
  }

  private static void setupDistribution(Injector injector) throws Exception {
    var distribution = injector.getInstance(WorkerDistribution.class);
    distribution.initialize();
  }
}
