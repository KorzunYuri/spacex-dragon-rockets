package six.hiring.testtasks.spacex.domain.mission;

import lombok.Getter;

@Getter
public enum MissionStatus {
    SCHEDULED(10, "Scheduled"),
    PENDING(20, "Pending"),
    IN_PROGRESS(30, "In progress"),
    ENDED(40, "Ended");

    private final int statusCode;
    private final String name;

    MissionStatus(int statusCode, String name) {
        this.statusCode = statusCode;
        this.name = name;
    }
}
