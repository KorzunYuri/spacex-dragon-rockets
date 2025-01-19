package six.hiring.testtasks.spacex.domain.mission;

import lombok.Builder;
import lombok.Getter;

/**
 * Contains all the necessary params for configuring a new {@link Mission}
 */
@Builder
@Getter
public class MissionConfig {
    private final String name;
}
