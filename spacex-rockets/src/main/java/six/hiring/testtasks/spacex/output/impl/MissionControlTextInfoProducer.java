package six.hiring.testtasks.spacex.output.impl;

import lombok.Builder;
import lombok.Setter;
import six.hiring.testtasks.spacex.MissionControl;
import six.hiring.testtasks.spacex.domain.mission.Mission;
import six.hiring.testtasks.spacex.domain.rocket.Rocket;
import six.hiring.testtasks.spacex.output.MissionControlInfoProducer;

import java.util.*;

@Builder
public class MissionControlTextInfoProducer implements MissionControlInfoProducer<List<String>> {

    @Setter
    @Builder.Default
    private Comparator<Mission> missionsComparator = new DefaultMissionComparator();

    @Setter
    @Builder.Default
    private Comparator<Rocket> rocketComparator = new DefaultRocketComparator();

    @Override
    public List<String> apply(MissionControl missionControl) {
        return missionControl.getMissions().stream()
                .sorted(missionsComparator)
                .map(m -> {
                    List<String> missionDescription = new ArrayList<>();
                    Set<Rocket> rockets = m.getRockets();
                    missionDescription.add(
                            String.format("%s - %s - Dragons: %d", m.getName(), m.getStatus().getName(), rockets.size()));
                    rockets.stream()
                            .sorted(rocketComparator)
                            .forEach(r -> missionDescription.add(String.format("\t%s - %s", r.getName(), r.getStatus().getName())
                    ));
                    return missionDescription;
                })
                .flatMap(Collection::stream)
            .toList();
    }

}
