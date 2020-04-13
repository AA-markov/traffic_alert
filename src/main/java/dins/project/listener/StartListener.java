package dins.project.listener;

import dins.project.service.LimitService;
import dins.project.service.SparkService;
import dins.project.service.impl.Spark;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StartListener implements ApplicationListener<ApplicationReadyEvent> {

    private final SparkService sparkService;
    private final Spark spark;
    private final LimitService limitService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        limitService.updateValues();

        try {
            sparkService.startTrafficControl();
//            spark.execute();
        } catch (Exception e) {
            log.error("The problem occurred: {}", e.getMessage());
        }
    }
}