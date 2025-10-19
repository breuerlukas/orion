package de.lukasbreuer.orion.operator;

import de.lukasbreuer.orion.core.configuration.Configuration;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.json.JSONObject;

@Getter
@Accessors(fluent = true)
public final class OperatorDistributionConfiguration extends Configuration {
  private static final String CONFIGURATION_PATH = "/configurations/operator/distribution/distribution.json";

  public static OperatorDistributionConfiguration createAndLoad() throws Exception {
    var configuration = new OperatorDistributionConfiguration(CONFIGURATION_PATH);
    configuration.load();
    return configuration;
  }

  private int distributionPort;
  private String distributionKey;

  private OperatorDistributionConfiguration(String path) {
    super(path);
  }

  @Override
  protected void deserialize(JSONObject json) {
    distributionPort = json.getInt("distributionPort");
    distributionKey = json.getString("distributionKey");
  }
}
