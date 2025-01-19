package six.hiring.testtasks.spacex.domain.rocket;

import lombok.Getter;

@Getter
public enum RocketStatus {
    ON_GROUND(10, "On ground"),
    IN_SPACE(20, "In space"),
    IN_REPAIR(30, "In repair");

    private final int statusCode;
    private final String name;

    RocketStatus(int statusCode, String name) {
        this.statusCode = statusCode;
        this.name = name;
    }
}
