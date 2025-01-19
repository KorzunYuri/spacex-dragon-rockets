package six.hiring.testtasks.spacex.domain.rocket;

import lombok.Getter;
import six.hiring.testtasks.spacex.domain.mission.Mission;

@Getter
public class Rocket {

    private final String name;
    private RocketStatus status = RocketStatus.ON_GROUND;
    private Mission mission;

    public Rocket(String name) {
        this.name = name;
    }
}
