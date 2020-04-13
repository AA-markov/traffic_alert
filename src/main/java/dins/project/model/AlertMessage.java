package dins.project.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Accessors(chain = true)
public class AlertMessage {
    private String message;
    private boolean isAbove;
    private long trafficValue;
    private Instant time;
}