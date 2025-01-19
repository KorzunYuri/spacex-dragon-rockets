package six.hiring.testtasks.spacex.domain.rocket;

public class RocketFactory {

    public Rocket buildRocket(RocketConfig config) {
        Rocket rocket = new Rocket(config.getName());
        return rocket;
    }

}
