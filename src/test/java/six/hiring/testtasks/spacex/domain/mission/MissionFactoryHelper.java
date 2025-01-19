package six.hiring.testtasks.spacex.domain.mission;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MissionFactoryHelper {

    private final MissionFactory missionFactory = new MissionFactory();
    private final String DEFAULT_NAME = "Mars";
    private final MissionConfig DEFAULT_CONFIG = MissionConfig.builder()
            .name(DEFAULT_NAME)
            .build();

    public Mission buildDefaultMission() {
        return buildMission(DEFAULT_CONFIG);
    }

    public Mission buildMission(MissionConfig missionConfig) {
        return buildMission(missionFactory, missionConfig);
    }

    public Mission buildMission(MissionFactory factory, MissionConfig missionConfig) {
        return factory.buildMission(missionConfig);
    }

}
