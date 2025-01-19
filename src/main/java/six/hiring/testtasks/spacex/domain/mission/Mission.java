package six.hiring.testtasks.spacex.domain.mission;

import lombok.Getter;
import six.hiring.testtasks.spacex.domain.rocket.Rocket;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class Mission {

    private final Set<Rocket> rockets = new TreeSet<>(Comparator.comparing(Rocket::getStatus));
    @Getter
    private final String name;
    @Getter
    private MissionStatus status = MissionStatus.SCHEDULED;

    protected Mission(String name) {
        this.name = name;
    }

    public Set<Rocket> getRockets() {
        return Set.copyOf(rockets);
    }
}
