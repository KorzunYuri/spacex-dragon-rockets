package six.hiring.testtasks.spacex.domain.mission;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MissionFactoryHelper {

    public final String DEFAULT_NAME = "Mars";
    private final MissionFactory missionFactory = new MissionFactory();
    private final MissionConfig DEFAULT_CONFIG = MissionConfig.builder()
            .name(DEFAULT_NAME)
            .build();

    public Mission buildDefaultMission() {
        return buildMission(DEFAULT_CONFIG);
    }

    public MissionConfig configWithName(String missionName) {
        return MissionConfig.builder()
                .name(missionName)
            .build();
    }

    public Mission buildMission(String missionName) {
        return buildMission(missionFactory, configWithName(missionName));
    }

    public Mission buildMission(MissionConfig missionConfig) {
        return buildMission(missionFactory, missionConfig);
    }

    public Mission buildMission(MissionFactory factory, MissionConfig missionConfig) {
        return factory.buildMission(missionConfig);
    }

    public MissionConfig getDefaultConfig() {
        return DEFAULT_CONFIG;
    }
}
