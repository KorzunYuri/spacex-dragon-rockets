package six.hiring.testtasks.spacex.domain.mission;

public class MissionFactory {

    public Mission buildMission(MissionConfig missionConfig) {
        Mission mission = new Mission(missionConfig.getName());
        return mission;
    }
}
