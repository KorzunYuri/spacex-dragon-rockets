package six.hiring.testtasks.spacex;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import six.hiring.testtasks.spacex.domain.mission.Mission;
import six.hiring.testtasks.spacex.domain.mission.MissionConfig;
import six.hiring.testtasks.spacex.domain.mission.MissionFactory;
import six.hiring.testtasks.spacex.domain.mission.MissionStatus;
import six.hiring.testtasks.spacex.domain.rocket.Rocket;
import six.hiring.testtasks.spacex.domain.rocket.RocketConfig;
import six.hiring.testtasks.spacex.domain.rocket.RocketFactory;
import six.hiring.testtasks.spacex.domain.rocket.RocketStatus;

import java.util.HashSet;
import java.util.Set;

public class MissionControlReactive {

    private final Set<Mission> missions = new HashSet<>();
    private final Set<Rocket> rockets = new HashSet<>();

    private final MissionFactory missionFactory = new MissionFactory();
    private final RocketFactory rocketFactory = new RocketFactory();

    public Flux<Rocket> getRockets() {
        return Flux.fromIterable(rockets);
    }

    public Flux<Mission> getMissions() {
        return Flux.fromIterable(missions);
    }

    public Mono<Rocket> createRocket(RocketConfig rocketConfig) {
        Rocket rocket = rocketFactory.buildRocket(rocketConfig);
        rockets.add(rocket);
        return Mono.just(rocket);
    }

    public Mono<Mission> createMission(MissionConfig missionConfig) {
        Mission mission = missionFactory.buildMission(missionConfig);
        missions.add(mission);
        return Mono.just(mission);
    }

    public Mono<Void> assignRocketsToMission(Flux<Rocket> rockets, Mono<Mission> mission) {
        return validateMission(mission) // Проверяем миссию
                .flatMapMany(validMission -> rockets
                        .doOnNext(this::validateRocket) // Проверяем ракеты
                        .collectList() // Собираем список ракет
                        .doOnNext(validMission::assignRockets)) // Назначаем их миссии
                .then();
    }

    public Mono<Void> assignRocketToMission(Mono<Rocket> rocket, Mono<Mission> mission) {
        return assignRocketsToMission(Flux.from(rocket), mission);
    }

    public Mono<Mission> changeMissionStatus(Mono<Mission> mission, MissionStatus status) {
        return mission.doOnNext(m -> m.setStatus(status));
    }

    public Mono<Rocket> changeRocketStatus(Mono<Rocket> rocket, RocketStatus status) {
        return rocket.doOnNext(r -> r.setStatus(status));
    }

    private Mono<Mission> validateMission(Mono<Mission> mission) {
        return mission.flatMap(m -> {
            if (!missions.contains(m)) {
                return Mono.error(new IllegalArgumentException(
                        String.format("Unknown mission %s", m.getName())));
            }
            return Mono.just(m);
        });
    }

    /**
     * For reactive approach to become applicable here, Mission should handle Rockets in reactive way,
     * so using the same domain model by both synchronous and reactive approach is limited.
     */
    private void validateRocket(Rocket rocket) throws IllegalArgumentException {
        if (!rockets.contains(rocket)) {
            throw new IllegalArgumentException(String.format("Unknown rocket %s", rocket.getName()));
        }
    }
}
