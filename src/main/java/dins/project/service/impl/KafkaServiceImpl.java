package dins.project.service.impl;

import dins.project.model.AlertMessage;
import dins.project.service.KafkaService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KafkaServiceImpl implements KafkaService {

    private static final String ALERT_TOPIC = "alerts";
    private static final String MESSAGE_TOPIC = "numbers";
    private final KafkaTemplate kafkaAlertTemplate;
    private final KafkaTemplate kafkaNumberTemplate;
    private final KafkaListenerContainerFactory listener;

    @Override
    public void sendToKafka(AlertMessage message) {
        kafkaAlertTemplate.send(ALERT_TOPIC, message);
    }

    @Override
    public void sendNumbers(long number) {
        kafkaNumberTemplate.send(MESSAGE_TOPIC, number);
    }
}