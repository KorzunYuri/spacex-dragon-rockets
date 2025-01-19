package six.hiring.testtasks.spacex;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import six.hiring.testtasks.spacex.domain.mission.Mission;
import six.hiring.testtasks.spacex.domain.mission.MissionConfig;
import six.hiring.testtasks.spacex.domain.mission.MissionStatus;
import six.hiring.testtasks.spacex.domain.rocket.Rocket;
import six.hiring.testtasks.spacex.domain.rocket.RocketConfig;
import six.hiring.testtasks.spacex.domain.rocket.RocketStatus;

import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MissionControlReactiveTest {

    private static final String DEFAULT_ROCKET_NAME = "Rocket 1";
    private static final String DEFAULT_MISSION_NAME = "Mission 1";
    private MissionControlReactive missionControl;

    @BeforeEach
    public void init() {
        missionControl = new MissionControlReactive();
    }

    @AfterEach
    public void destroy() {}

    private RocketConfig defaultRocketConfig() {
        return RocketConfig.builder().name(DEFAULT_ROCKET_NAME).build();
    }

    private MissionConfig defaultMissionConfig() {
        return MissionConfig.builder().name(DEFAULT_MISSION_NAME).build();
    }

    @Test
    @Order(1)
    public void createsRocketWithCorrectInitialState() {
        Mono<Rocket> rocket = missionControl.createRocket(defaultRocketConfig());
        StepVerifier.create(rocket)
                .assertNext(r -> {
                    assertNotNull(r);
                    assertEquals(DEFAULT_ROCKET_NAME, r.getName());
                    assertEquals(RocketStatus.ON_GROUND, r.getStatus());
                })
            .verifyComplete();

        StepVerifier.create(missionControl.getRockets())
                .expectNext(rocket.block())
            .verifyComplete();
    }

    @Test
    @Order(2)
    public void createsMissionWithCorrectInitialState() {
        Mono<Mission> mission = missionControl.createMission(defaultMissionConfig());
        StepVerifier.create(mission)
                .assertNext(m -> {
                    assertNotNull(m);
                    assertEquals(DEFAULT_MISSION_NAME, m.getName());
                    assertEquals(MissionStatus.SCHEDULED, m.getStatus());
                })
            .verifyComplete();

        StepVerifier.create(missionControl.getMissions())
                .expectNext(mission.block())
            .verifyComplete();
    }

}
