package dins.project.service;

import dins.project.model.Limit;
import dins.project.model.LimitType;

public interface LimitService {

    boolean addLimit(LimitType type, Long value);

    void updateValues();

    Limit getLimit(LimitType type);

    long getMax();

    long getMin();
}