package six.hiring.testtasks.spacex.domain.rocket;

import lombok.Getter;
import lombok.NonNull;
import six.hiring.testtasks.spacex.domain.mission.Mission;

@Getter
public class Rocket {

    private final String name;
    private RocketStatus status = RocketStatus.ON_GROUND;
    private Mission mission;

    public Rocket(String name) {
        this.name = name;
    }

    public void setMission(@NonNull Mission mission) {
        this.mission = mission;
        mission.updateStatus();
    }

    public void resetMission() {
        this.mission = null;
        setStatus(RocketStatus.ON_GROUND);
    }

    /**
     * Set mission status, being aware of the source of change to avoid an infinite loop
     */
    public void setStatus(RocketStatus targetStatus, Mission initiator) {
        if (RocketStatus.IN_SPACE.equals(targetStatus) && initiator == null) {
            throw new IllegalArgumentException("Rocket cannot go to space independently from the mission start");
        }

        this.status = targetStatus;
        //  if mission is not the source of change then notify it about the update
        if (this.mission != null && initiator == null) {
            this.mission.updateStatus();
        }
    }

    public void setStatus(RocketStatus targetStatus) {
        setStatus(targetStatus, null);
    }

}
