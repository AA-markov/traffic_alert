package dins.project.service.impl;

import dins.project.service.SparkService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.springframework.stereotype.Component;
import scala.Tuple2;

import java.util.*;

@Component
@RequiredArgsConstructor
public class Spark {

    private final SparkConf sparkConf;
    private final SparkService sparkService;

    public void execute() throws InterruptedException {
        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("bootstrap.servers", "localhost:9092");
        kafkaParams.put("group.id", "group");

        Collection<String> topics = Arrays.asList("numbers");

        JavaStreamingContext streamingContext = new JavaStreamingContext(sparkConf, Durations.seconds(1));

        JavaInputDStream<ConsumerRecord<String, String>> messages =
                KafkaUtils.createDirectStream(streamingContext, LocationStrategies.PreferConsistent(), ConsumerStrategies.<String, String> Subscribe(topics, kafkaParams));

        JavaPairDStream<String, String> results = messages.mapToPair(record -> new Tuple2<>(record.key(), record.value()));

        JavaDStream<String> lines = results.map(Tuple2::_2);

        lines.foreachRDD(
                javaRdd -> {
                    javaRdd.foreach(number -> {
                        long value = Long.parseLong(number);
                        System.out.println("Value: " + sparkService.addCounter(value));
                    });
                });
        streamingContext.start();
        streamingContext.awaitTermination();
    }
}
