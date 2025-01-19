package six.hiring.testtasks.spacex.output.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import six.hiring.testtasks.spacex.MissionControl;
import six.hiring.testtasks.spacex.domain.mission.Mission;
import six.hiring.testtasks.spacex.domain.mission.MissionConfig;
import six.hiring.testtasks.spacex.domain.mission.MissionStatus;
import six.hiring.testtasks.spacex.domain.rocket.Rocket;
import six.hiring.testtasks.spacex.domain.rocket.RocketConfig;
import six.hiring.testtasks.spacex.domain.rocket.RocketStatus;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MissionControlInfoProducerTest {

    private MissionControl missionControl;
    private MissionControlTextInfoProducer infoProducer;

    @BeforeEach
    public void init() {
        missionControl = new MissionControl();
        infoProducer = MissionControlTextInfoProducer.builder().build();
    }

    @AfterEach
    public void destroy() {

    }

    private Mission createMissionWithName(String name) {
        return missionControl.createMission(MissionConfig.builder()
                .name(name)
                .build());
    }

    private Rocket createRocketWithName(String name) {
        return missionControl.createRocket(RocketConfig.builder()
                .name(name)
                .build());
    }

    @Test
    public void returnsCorrectlyOrderedSummary() {
        Mission mars = createMissionWithName("Mars");
        Mission luna1 = createMissionWithName("Luna1");
        Mission luna2 = createMissionWithName("Luna2");
        Mission transit = createMissionWithName("Transit");
        Mission doubleLanding = createMissionWithName("Double Landing");
        Mission verticalLanding = createMissionWithName("Vertical Landing");

        Rocket dragon1 = createRocketWithName("Dragon 1");
        Rocket dragon2 = createRocketWithName("Dragon 2");
        Rocket redDragon = createRocketWithName("Red Dragon");
        Rocket dragonXL = createRocketWithName("Dragon XL");
        Rocket falconHeavy = createRocketWithName("Falcon Heavy");

        missionControl.changeRocketStatus(dragon1, RocketStatus.IN_REPAIR);
        missionControl.assignRocketsToMission(Set.of(dragon1, dragon2), luna1);
        missionControl.assignRocketsToMission(Set.of(redDragon, dragonXL, falconHeavy), transit);
        missionControl.changeMissionStatus(transit, MissionStatus.IN_PROGRESS);
        missionControl.changeMissionStatus(doubleLanding, MissionStatus.ENDED);
        missionControl.changeMissionStatus(verticalLanding, MissionStatus.ENDED);

        List<String> summary = infoProducer.apply(missionControl);
        List<String> expectedSummary = List.of(
                "Transit - In progress - Dragons: 3",
                        "\tDragon XL - In space",
                        "\tFalcon Heavy - In space",
                        "\tRed Dragon - In space",
                        "Luna1 - Pending - Dragons: 2",
                        "\tDragon 2 - On ground",
                        "\tDragon 1 - In repair",
                        "Vertical Landing - Ended - Dragons: 0",
                        "Mars - Scheduled - Dragons: 0",
                        "Luna2 - Scheduled - Dragons: 0",
                        "Double Landing - Ended - Dragons: 0"
        );
        assertEquals(expectedSummary.size(), summary.size());
        for (int i = 0; i < expectedSummary.size(); i++) {
            assertEquals(expectedSummary.get(i), summary.get(i));
        }
    }
}
