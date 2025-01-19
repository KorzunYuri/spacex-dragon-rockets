package six.hiring.testtasks.spacex.domain.mission;

import lombok.Getter;
import six.hiring.testtasks.spacex.domain.rocket.Rocket;
import six.hiring.testtasks.spacex.domain.rocket.RocketStatus;

import java.util.*;

public class Mission {

    private final Set<Rocket> rockets = new HashSet<>();

    @Getter
    private final String name;
    @Getter
    private MissionStatus status = MissionStatus.SCHEDULED;

    protected Mission(String name) {
        this.name = name;
    }

    public void assignRocket(Rocket newRocket) {
        assignRockets(Set.of(newRocket));
    }

    public void assignRockets(Collection<Rocket> newRockets) {
        if (this.status == MissionStatus.ENDED) {
            throw new IllegalStateException("Mission has ended and new rockets cannot be assigned");
        }
        newRockets.forEach(this::validateNewRocket);

        this.rockets.addAll(newRockets);
        newRockets.forEach(rocket -> rocket.setMission(this));
        updateStatus();
    }

    public Set<Rocket> getRockets() {
        return Set.copyOf(rockets);
    }

    private void validateNewRocket(Rocket rocket) throws IllegalArgumentException {
        if (rocket.getMission() != null) {
            throw new IllegalArgumentException(
                    String.format("Rocket %s already assigned to mission %s", rocket.getName(), rocket.getMission().getName()));
        }
        if (rockets.contains(rocket)) {
            //  if we reach here then there is a flaw in the logic - the rocket should have current mission as its mission
            throw new IllegalArgumentException(
                    String.format("Rocket %s already assigned to mission %s", rocket.getName(), this.getName()));
        }
    }

    public void setStatus(MissionStatus targetStatus) {
        if (this.status == targetStatus) return;

        //  apply status if possible
        validateNewStatus(targetStatus);
        this.status = targetStatus;

        // notify dependent objects
        if (this.status == MissionStatus.ENDED) {
            this.rockets.forEach(Rocket::resetMission);
        } else if (this.status == MissionStatus.IN_PROGRESS) {
            this.rockets.forEach(r -> r.setStatus(RocketStatus.IN_SPACE, this));
        }
    }

    /**
     * Recalculates mission status based on rockets' statuses and other info after external changes.
     * Normally shouldn't be called by the client.
     */
    public void updateStatus() {
        MissionStatus targetStatus;
        if (rockets.isEmpty()) {
            targetStatus = MissionStatus.SCHEDULED;
        } else if (rockets.stream().anyMatch(rocket -> rocket.getStatus().equals(RocketStatus.IN_REPAIR))) {
            targetStatus = MissionStatus.PENDING;
        } else {
            targetStatus = MissionStatus.IN_PROGRESS;
        }

        setStatus(targetStatus);
    }

    /**
     * Checks whether transition to a new status is valid
     * @throws IllegalArgumentException if transition is impossible (e.g. we want to repair a rocket when in space)
     */
    private void validateNewStatus(MissionStatus targetStatus) throws IllegalStateException {
        if (status == MissionStatus.IN_PROGRESS && targetStatus == MissionStatus.PENDING) {
            throw new IllegalStateException("Mission failed: one or more rockets are broken while in space");
        }
    }
}
