package six.hiring.testtasks.spacex;

import six.hiring.testtasks.spacex.domain.mission.Mission;
import six.hiring.testtasks.spacex.domain.mission.MissionConfig;
import six.hiring.testtasks.spacex.domain.mission.MissionFactory;
import six.hiring.testtasks.spacex.domain.mission.MissionStatus;
import six.hiring.testtasks.spacex.domain.rocket.Rocket;
import six.hiring.testtasks.spacex.domain.rocket.RocketConfig;
import six.hiring.testtasks.spacex.domain.rocket.RocketFactory;
import six.hiring.testtasks.spacex.domain.rocket.RocketStatus;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Serves as a facade for managing missions and rockets.
 */
public class MissionControl {

    private final Set<Mission> missions = new HashSet<>();
    private final Set<Rocket> rockets = new HashSet<>();

    private final MissionFactory missionFactory = new MissionFactory();
    private final RocketFactory rocketFactory = new RocketFactory();

    public Set<Rocket> getRockets() {
        return Set.copyOf(rockets);
    }

    public Set<Mission> getMissions() {
        return Set.copyOf(missions);
    }

    public Rocket createRocket(RocketConfig rocketConfig) {
        Rocket rocket = rocketFactory.buildRocket(rocketConfig);
        rockets.add(rocket);
        return rocket;
    }

    public Mission createMission(MissionConfig missionConfig) {
        Mission mission = missionFactory.buildMission(missionConfig);
        missions.add(mission);
        return mission;
    }

    public void assignRocketsToMission(Collection<Rocket> rockets, Mission mission) {
        validateMission(mission);
        rockets.forEach(this::validateRocket);
        mission.assignRockets(rockets);
    }

    public void assignRocketToMission(Rocket rocket, Mission mission) {
        assignRocketsToMission(Set.of(rocket), mission);
    }

    public void changeMissionStatus(Mission mission, MissionStatus status) {
        mission.setStatus(status);
    }

    public void changeRocketStatus(Rocket rocket, RocketStatus status) {
        rocket.setStatus(status);
    }

    private void validateMission(Mission mission) throws IllegalArgumentException {
        if (!missions.contains(mission)) {
            throw new IllegalArgumentException(String.format("Unknown mission %s", mission.getName()));
        }
    }

    private void validateRocket(Rocket rocket) throws IllegalArgumentException {
        if (!rockets.contains(rocket)) {
            throw new IllegalArgumentException(String.format("Unknown rocket %s", rocket.getName()));
        }
    }
}
