package dins.project.aspect;

import dins.project.repository.LimitRepository;
import dins.project.service.LimitService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class LimitsRefreshedAspect {

    private final LimitRepository limitRepository;
    private final LimitService limitService;

    @Pointcut("execution(* dins.project.repository.LimitRepository.addLimit(..))")
    private void limitRefesh() {
    }

    @After("limitRefesh()")
    private void updateLimits() {
        limitService.updateValues();
    }
}
