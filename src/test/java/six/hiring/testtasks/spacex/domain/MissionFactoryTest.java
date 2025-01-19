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

    @BeforeEach
    public void init() {
        missionFactory = new MissionFactory();
    }

    @AfterEach
    public void destroy() {

    }

    @Test
    public void createsMissionWithValidInitialState() {
        final String missionName = "Mars";

        MissionConfig missionConfig = MissionConfig.builder().name(missionName).build();
        Mission mission = missionFactory.buildMission(missionConfig);

        assertNotNull(mission);
        assertEquals(missionName, mission.getName());
        assertEquals(MissionStatus.SCHEDULED, mission.getStatus());
        assertEquals(Collections.emptySet(), mission.getRockets());
    }

}
