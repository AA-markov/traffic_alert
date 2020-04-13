package dins.project.service.impl;

import dins.project.model.Limit;
import dins.project.model.LimitType;
import dins.project.repository.LimitRepository;
import dins.project.service.LimitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LimitServiceImpl implements LimitService {

    private Long minLimit = 0L;
    private Long maxLimit = Long.MAX_VALUE;

    private final LimitRepository limitRepository;

    @Override
    public boolean addLimit(LimitType type, Long value) {
        if (isValueAcceptable(type, value)) {
            return limitRepository.addLimit(type, value);
        }
        return false;
    }

    @Override
    public void updateValues() {
        minLimit = getLatest(LimitType.MIN);
        maxLimit = getLatest(LimitType.MAX);
    }

    @Override
    public Limit getLimit(LimitType type) {
        return limitRepository.getLatestValue(type);
    }

    private Long getLatest(LimitType type) {
        return limitRepository.getLatestValue(type).getValue();
    }

    private boolean isValueAcceptable(LimitType type, Long value) {
        boolean isAcceptable = false;
        if (type == LimitType.MIN) {
            isAcceptable = value > 0 && value < maxLimit;
        }
        else isAcceptable = value > 0 && value > minLimit;
        return isAcceptable;
    }

    @Override
    public long getMax() {
        return maxLimit;
    }

    @Override
    public long getMin() {
        return minLimit;
    }
}