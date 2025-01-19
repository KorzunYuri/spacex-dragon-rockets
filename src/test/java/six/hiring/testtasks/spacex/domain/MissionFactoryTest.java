package six.hiring.testtasks.spacex.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import six.hiring.testtasks.spacex.domain.mission.Mission;
import six.hiring.testtasks.spacex.domain.mission.MissionConfig;
import six.hiring.testtasks.spacex.domain.mission.MissionFactory;
import six.hiring.testtasks.spacex.domain.mission.MissionStatus;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MissionFactoryTest {

    private MissionFactory missionFactory;
    private final String DEFAULT_NAME = "Mars";
    private final MissionConfig DEFAULT_CONFIG = MissionConfig.builder()
            .name(DEFAULT_NAME)
        .build();

    @BeforeEach
    public void init() {
        missionFactory = new MissionFactory();
    }

    @AfterEach
    public void destroy() {

    }

    private Mission createMission(MissionConfig config) {
        return missionFactory.buildMission(config);
    }

    private Mission createDefaultMission() {
        return createMission(DEFAULT_CONFIG);
    }

    @Test
    public void createsMissionWithValidInitialState() {
        Mission mission = createDefaultMission();

        assertNotNull(mission);
        assertEquals(DEFAULT_NAME, mission.getName());
        assertEquals(MissionStatus.SCHEDULED, mission.getStatus());
        assertEquals(Collections.emptySet(), mission.getRockets());
    }

    @Test
    public void createsMissionWithCustomConfig() {
        final String missionName = "Mars";
        MissionConfig missionConfig = MissionConfig.builder().name(missionName).build();
        Mission mission = missionFactory.buildMission(missionConfig);

        assertNotNull(mission);
        assertEquals(missionName, mission.getName());
    }

}
