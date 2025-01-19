package six.hiring.testtasks.spacex.output.impl;

import six.hiring.testtasks.spacex.domain.rocket.Rocket;

import java.util.Comparator;

/**
 * Rocket comparator for ordering rockets in descending size and ascending alphabetical order
 */
public class DefaultRocketComparator implements Comparator<Rocket> {
    @Override
    public int compare(Rocket r1, Rocket r2) {
        int statusComparison = Integer.compare(
                r1.getStatus().getStatusCode(),
                r2.getStatus().getStatusCode());
        if (statusComparison != 0) {
            return statusComparison;
        }
        //  asc alphabetical order
        return r1.getName().compareTo(r2.getName());
    }
}
