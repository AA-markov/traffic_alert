package dins.project.service.impl;

import dins.project.model.AlertMessage;
import dins.project.service.KafkaService;
import dins.project.service.LimitService;
import dins.project.service.SparkService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class ScheduleServiceImpl {

    private final KafkaService kafkaService;
    private final LimitService limitService;
    private final SparkService sparkService;

    @Scheduled(fixedRate = 5 * 1000)
    public void executeCounterCheck() {
        long value = sparkService.resetTrafficCounter(0L);
        AlertMessage message = new AlertMessage().setTime(Instant.now()).setTrafficValue(value);
        if (value > limitService.getMax()) {
            message.setAbove(true)
                    .setMessage("Traffic is over limit!");
            kafkaService.sendToKafka(message);
        } else if (value < limitService.getMin()) {
            message.setAbove(false)
                    .setMessage("Traffic is below limit!");
            kafkaService.sendToKafka(message);
        }
    }

    @Scheduled(fixedRate = 20 * 60 * 1000)
    public void executeLimitUpdate() {
        limitService.updateValues();
    }

}
