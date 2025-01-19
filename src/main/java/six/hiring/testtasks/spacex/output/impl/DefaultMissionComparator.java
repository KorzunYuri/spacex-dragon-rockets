package six.hiring.testtasks.spacex.output.impl;

import six.hiring.testtasks.spacex.domain.mission.Mission;

import java.util.Comparator;

/**
 * Mission comparator for ordering missions in descending size and alphabetical order
 */
public class DefaultMissionComparator implements Comparator<Mission> {

    @Override
    public int compare(Mission m1, Mission m2) {
        // desc size order
        int statusComparison = Integer.compare(m2.getRockets().size(), m1.getRockets().size());
        if (statusComparison != 0) {
            return statusComparison;
        }
        //  desc alphabetical order
        return m2.getName().compareTo(m1.getName());
    }
}