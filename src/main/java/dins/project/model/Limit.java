package dins.project.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Builder
@Accessors(chain = true)
public class Limit {
    private LimitType type;
    private Long value;
    private Instant effectiveDate;
}