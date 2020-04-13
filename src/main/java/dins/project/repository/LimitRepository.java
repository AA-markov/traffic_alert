package dins.project.repository;

import dins.project.model.Limit;
import dins.project.model.LimitType;

public interface LimitRepository {

    boolean addLimit(LimitType type, Long value);

    Limit getLatestValue(LimitType type);
}