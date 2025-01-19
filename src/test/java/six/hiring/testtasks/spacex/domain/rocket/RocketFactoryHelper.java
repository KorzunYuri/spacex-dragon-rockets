package six.hiring.testtasks.spacex.domain.rocket;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RocketFactoryHelper {

    public final String DEFAULT_NAME = "Falcon Heavy";
    private final RocketFactory rocketFactory = new RocketFactory();
    private final RocketConfig DEFAULT_CONFIG = RocketConfig.builder()
            .name(DEFAULT_NAME)
            .build();

    public Rocket buildDefaultRocket() {
        return buildRocket(DEFAULT_CONFIG);
    }

    public RocketConfig configWithName(String rocketName) {
        return RocketConfig.builder()
                .name(rocketName)
            .build();
    }

    public Rocket buildRocket(String rocketName) {
        return buildRocket(rocketFactory, configWithName(rocketName));
    }

    public Rocket buildRocket(RocketConfig rocketConfig) {
        return buildRocket(rocketFactory, rocketConfig);
    }

    public Rocket buildRocket(RocketFactory factory, RocketConfig rocketConfig) {
        return factory.buildRocket(rocketConfig);
    }

    public RocketConfig getDefaultConfig() {
        return DEFAULT_CONFIG;
    }
}
