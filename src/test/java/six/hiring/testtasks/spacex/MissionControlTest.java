package six.hiring.testtasks.spacex;

import org.junit.jupiter.api.*;
import six.hiring.testtasks.spacex.domain.mission.Mission;
import six.hiring.testtasks.spacex.domain.mission.MissionConfig;
import six.hiring.testtasks.spacex.domain.mission.MissionFactoryHelper;
import six.hiring.testtasks.spacex.domain.mission.MissionStatus;
import six.hiring.testtasks.spacex.domain.rocket.Rocket;
import six.hiring.testtasks.spacex.domain.rocket.RocketConfig;
import six.hiring.testtasks.spacex.domain.rocket.RocketFactoryHelper;
import six.hiring.testtasks.spacex.domain.rocket.RocketStatus;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MissionControlTest {

    private MissionControl missionControl;

    @BeforeEach
    public void init() {
        missionControl = new MissionControl();
    }

    @AfterEach
    public void destroy() {}

    @Test
    @Order(1)
    public void createsRocketWithCorrectInitialState() {
        Rocket rocket = missionControl.createRocket(RocketFactoryHelper.getDefaultConfig());
        assertNotNull(rocket);
        assertEquals(RocketStatus.ON_GROUND, rocket.getStatus());
        assertEquals(Set.of(rocket), missionControl.getRockets());
    }

    @Test
    @Order(2)
    public void createsMissionWithCorrectInitialState() {
        Mission mission = missionControl.createMission(MissionFactoryHelper.getDefaultConfig());
        assertNotNull(mission);
        assertEquals(MissionStatus.SCHEDULED, mission.getStatus());
        assertEquals(Set.of(mission), missionControl.getMissions());
    }

    @Test
    @Order(3)
    public void assignsRocketToMission() {
        Rocket rocket = missionControl.createRocket(RocketFactoryHelper.getDefaultConfig());
        Mission mission = missionControl.createMission(MissionFactoryHelper.getDefaultConfig());

        missionControl.assignRocketToMission(rocket, mission);

        assertNotNull(rocket.getMission());
        assertEquals(mission, rocket.getMission());
        assertEquals(Set.of(rocket), mission.getRockets());

        assertEquals(MissionStatus.IN_PROGRESS, mission.getStatus());
    }

    @Test
    public void declinesUnknownRocket() {
        Rocket rocket = RocketFactoryHelper.buildDefaultRocket();
        Mission mission = missionControl.createMission(MissionFactoryHelper.getDefaultConfig());

        Exception e = assertThrows(IllegalArgumentException.class, () -> missionControl.assignRocketToMission(rocket, mission));
        assertEquals(String.format("Unknown rocket %s", rocket.getName()), e.getMessage());
    }

    @Test
    public void declinesUnknownMission() {
        Rocket rocket = missionControl.createRocket(RocketFactoryHelper.getDefaultConfig());
        Mission mission = MissionFactoryHelper.buildDefaultMission();

        Exception e = assertThrows(IllegalArgumentException.class, () -> missionControl.assignRocketToMission(rocket, mission));
        assertEquals(String.format("Unknown mission %s", mission.getName()), e.getMessage());
    }

    @Test
    public void declinesRocketAssignedToMissionTwice() {
        Mission mission = missionControl.createMission(MissionFactoryHelper.getDefaultConfig());
        Rocket rocket = missionControl.createRocket(RocketFactoryHelper.getDefaultConfig());

        missionControl.assignRocketToMission(rocket, mission);
        Exception e = assertThrows(IllegalArgumentException.class, () -> missionControl.assignRocketToMission(rocket, mission));
        assertEquals(String.format("Rocket %s already assigned to mission %s", rocket.getName(), mission.getName()), e.getMessage());
    }

    @Test
    public void declinesRocketWhenMissionEnded() {
        Mission mission = missionControl.createMission(MissionFactoryHelper.getDefaultConfig());
        Rocket rocket = missionControl.createRocket(RocketFactoryHelper.getDefaultConfig());

        missionControl.changeMissionStatus(mission, MissionStatus.ENDED);
        Exception e = assertThrows(IllegalStateException.class, () -> missionControl.assignRocketToMission(rocket, mission));
        assertEquals("Mission has ended and new rockets cannot be assigned", e.getMessage());
    }

    @Test
    public void assignsRocketCollection() {
        Mission mission = missionControl.createMission(MissionFactoryHelper.getDefaultConfig());
        Rocket rocket1 = missionControl.createRocket(RocketConfig.builder().name("rocket 1").build());
        Rocket rocket2 = missionControl.createRocket(RocketConfig.builder().name("rocket 2").build());

        Set<Rocket> rockets = Set.of(rocket1, rocket2);
        mission.assignRockets(rockets);

        assertEquals(rockets, missionControl.getRockets());
        assertEquals(rockets.size(), missionControl.getRockets().size());
    }

    @Test
    public void updatesStatusFromScheduledToPendingWhenRepairedRocketIsAssigned() {
        Mission mission = missionControl.createMission(MissionFactoryHelper.getDefaultConfig());
        Rocket rocket = missionControl.createRocket(RocketFactoryHelper.getDefaultConfig());
        assertEquals(MissionStatus.SCHEDULED, mission.getStatus());

        missionControl.changeRocketStatus(rocket, RocketStatus.IN_REPAIR);
        missionControl.assignRocketToMission(rocket, mission);
        assertEquals(MissionStatus.PENDING, mission.getStatus());
    }

    @Test
    public void updatesStatusFromScheduledToInProgressWhenOneRocketIsAssigned() {
        Mission mission = missionControl.createMission(MissionFactoryHelper.getDefaultConfig());
        Rocket rocket = missionControl.createRocket(RocketFactoryHelper.getDefaultConfig());
        assertEquals(MissionStatus.SCHEDULED, mission.getStatus());

        missionControl.assignRocketToMission(rocket, mission);
        assertEquals(MissionStatus.IN_PROGRESS, mission.getStatus());
        assertEquals(RocketStatus.IN_SPACE, rocket.getStatus());
    }

    @Test
    public void updatesStatusFromScheduledToInProgressWhenMultipleRocketsAreAssigned() {
        Mission mission = missionControl.createMission(MissionFactoryHelper.getDefaultConfig());
        Rocket rocket1 = missionControl.createRocket(RocketConfig.builder().name("rocket 1").build());
        Rocket rocket2 = missionControl.createRocket(RocketConfig.builder().name("rocket 2").build());
        assertEquals(MissionStatus.SCHEDULED, mission.getStatus());

        Set<Rocket> rockets = Set.of(rocket1, rocket2);

        missionControl.assignRocketsToMission(rockets, mission);
        assertEquals(MissionStatus.IN_PROGRESS, mission.getStatus());
        rockets.forEach(r -> assertEquals(RocketStatus.IN_SPACE, r.getStatus()));
    }

    @Test
    public void disallowesRepairingRocketWhenInSpace() {
        Mission mission = missionControl.createMission(MissionFactoryHelper.getDefaultConfig());
        Rocket rocket = missionControl.createRocket(RocketFactoryHelper.getDefaultConfig());
        assertEquals(MissionStatus.SCHEDULED, mission.getStatus());

        missionControl.assignRocketToMission(rocket, mission);
        assertEquals(MissionStatus.IN_PROGRESS, mission.getStatus());

        Exception e = assertThrows(IllegalStateException.class, () -> missionControl.changeRocketStatus(rocket, RocketStatus.IN_REPAIR));
        assertEquals("Mission failed: one or more rockets are broken while in space", e.getMessage());
    }

    @Test
    public void updatesRocketStatusesWhenMissionEnded() {
        Mission mission = missionControl.createMission(MissionFactoryHelper.getDefaultConfig());
        Rocket rocket1 = missionControl.createRocket(RocketConfig.builder().name("rocket 1").build());
        Rocket rocket2 = missionControl.createRocket(RocketConfig.builder().name("rocket 2").build());
        assertEquals(MissionStatus.SCHEDULED, mission.getStatus());

        Set<Rocket> rockets = Set.of(rocket1, rocket2);
        missionControl.assignRocketsToMission(rockets, mission);
        assertEquals(MissionStatus.IN_PROGRESS, mission.getStatus());

        missionControl.changeMissionStatus(mission, MissionStatus.ENDED);
        assertEquals(MissionStatus.ENDED, mission.getStatus());
        rockets.forEach(r -> {
            assertEquals(RocketStatus.ON_GROUND, r.getStatus());
            assertNull(r.getMission());
        });
    }

    @Test
    public void declinesChangingRocketStatusToInSpace() {
        Mission mission = missionControl.createMission(MissionFactoryHelper.getDefaultConfig());
        Rocket rocket = missionControl.createRocket(RocketFactoryHelper.getDefaultConfig());
        missionControl.changeRocketStatus(rocket, RocketStatus.IN_REPAIR);

        missionControl.assignRocketToMission(rocket, mission);

        Exception e = assertThrows(
                IllegalArgumentException.class,
                () -> missionControl.changeRocketStatus(rocket, RocketStatus.IN_SPACE));
        assertEquals("Rocket cannot go to space independently from the mission start", e.getMessage());
    }

    @Test
    public void missionLifecycleTest() {
        Mission mission = missionControl.createMission(MissionFactoryHelper.getDefaultConfig());
        Rocket rocket1 = missionControl.createRocket(RocketConfig.builder().name("rocket 1").build());
        Rocket rocket2 = missionControl.createRocket(RocketConfig.builder().name("rocket 2").build());
        missionControl.changeRocketStatus(rocket1, RocketStatus.IN_REPAIR);

        assertEquals(MissionStatus.SCHEDULED, mission.getStatus());
        assertEquals(RocketStatus.IN_REPAIR, rocket1.getStatus());
        assertEquals(RocketStatus.ON_GROUND, rocket2.getStatus());

        //  start mission with a rocket in repairing state
        missionControl.assignRocketToMission(rocket1, mission);
        assertEquals(MissionStatus.PENDING, mission.getStatus());

        missionControl.assignRocketToMission(rocket2, mission);
        assertEquals(MissionStatus.PENDING, mission.getStatus());

        //  repair the rocket, which should trigger the mission launch
        missionControl.changeRocketStatus(rocket1, RocketStatus.ON_GROUND);
        assertEquals(MissionStatus.IN_PROGRESS, mission.getStatus());
        mission.getRockets().forEach(r -> assertEquals(RocketStatus.IN_SPACE, r.getStatus()));

        //  end the mission
        missionControl.changeMissionStatus(mission, MissionStatus.ENDED);
        assertEquals(MissionStatus.ENDED, mission.getStatus());
        mission.getRockets().forEach(r -> {
            assertEquals(RocketStatus.ON_GROUND, r.getStatus());
            assertNull(r.getMission());
        });
    }
}
