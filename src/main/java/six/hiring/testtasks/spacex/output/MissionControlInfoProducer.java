package six.hiring.testtasks.spacex.output;

import six.hiring.testtasks.spacex.MissionControl;

import java.util.function.Function;

public interface MissionControlInfoProducer<OutputType> extends Function<MissionControl, OutputType> {
}
