package six.hiring.testtasks.spacex.domain.rocket;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        String rocketName = "Rocket 1";
        RocketConfig customConfig = RocketConfig.builder()
                .name(rocketName)
                .build();
        Rocket rocket = RocketFactoryHelper.buildRocket(rocketFactory, customConfig);

        assertNotNull(rocket);
        assertEquals(rocketName, rocket.getName());
        assertEquals(RocketStatus.ON_GROUND, rocket.getStatus());
        assertNull(rocket.getMission());
    }
}
