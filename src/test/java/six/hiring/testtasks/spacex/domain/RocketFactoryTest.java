package six.hiring.testtasks.spacex.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import six.hiring.testtasks.spacex.domain.rocket.Rocket;
import six.hiring.testtasks.spacex.domain.rocket.RocketConfig;
import six.hiring.testtasks.spacex.domain.rocket.RocketFactory;
import six.hiring.testtasks.spacex.domain.rocket.RocketStatus;

import static org.junit.jupiter.api.Assertions.*;

public class RocketFactoryTest {

    private RocketFactory rocketFactory;
    private final String DEFAULT_NAME = "Falcon Heavy";
    private final RocketConfig DEFAULT_CONFIG = RocketConfig.builder()
            .name(DEFAULT_NAME)
        .build();

    @BeforeEach
    public void init() {
        rocketFactory = new RocketFactory();
    }

    @AfterEach
    public void destroy() {

    }

    private Rocket createRocket(RocketConfig rocketConfig) {
        return rocketFactory.buildRocket(rocketConfig);
    }

    private Rocket createDefaultRocket() {
        return createRocket(DEFAULT_CONFIG);
    }

    @Test
    public void createsRocketWithValidInitialState() {
        Rocket rocket = createDefaultRocket();

        assertNotNull(rocket);
        assertEquals(DEFAULT_NAME, rocket.getName());
        assertEquals(RocketStatus.ON_GROUND, rocket.getStatus());
        assertNull(rocket.getMission());
    }

    @Test
    public void createsRocketWithCustomConfig() {
        String rocketName = "Rocket 1";
        RocketConfig customConfig = RocketConfig.builder()
                .name(rocketName)
            .build();
        Rocket rocket = createRocket(customConfig);

        assertNotNull(rocket);
        assertEquals(rocketName, rocket.getName());
    }
}
