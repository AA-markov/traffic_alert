package dins.project.service;

public interface PacketService {

    void startTrafficControl() throws Exception;

    Long resetTrafficCounter(long newValue);

    Long addCounter(long value);
}