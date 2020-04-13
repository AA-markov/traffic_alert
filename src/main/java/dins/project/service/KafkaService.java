package dins.project.service;

import dins.project.model.AlertMessage;

public interface KafkaService {

    void sendToKafka(AlertMessage message);

    void sendNumbers(long number);
}