package dins.project.service;

public interface SparkService {

    void startTrafficControl() throws Exception;

    Long resetTrafficCounter(long newValue);

    Long addCounter(long value);
}