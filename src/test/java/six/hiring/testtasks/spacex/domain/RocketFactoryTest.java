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

    @BeforeEach
    public void init() {
        rocketFactory = new RocketFactory();
    }

    @AfterEach
    public void destroy() {

    }

    @Test
    public void createsRocketWithValidInitialState() {
        final String rocketName = "Falcon Heavy";

        RocketConfig rocketConfig = RocketConfig.builder()
                .name(rocketName)
            .build();
        Rocket rocket = rocketFactory.buildRocket(rocketConfig);

        assertNotNull(rocket);
        assertEquals(rocketName, rocket.getName());
        assertEquals(RocketStatus.ON_GROUND, rocket.getStatus());
        assertNull(rocket.getMission());
    }
}
