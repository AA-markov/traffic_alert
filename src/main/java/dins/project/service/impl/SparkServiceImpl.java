package dins.project.service.impl;

import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import dins.project.service.PacketService;
import dins.project.service.SparkService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.springframework.stereotype.Component;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SparkServiceImpl implements SparkService {

    private final SparkConf sparkConf;
    private final PacketService packetService;

    public void readFromKafka() throws InterruptedException {
        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("bootstrap.servers", "localhost:9092");
        kafkaParams.put("key.deserializer", StringDeserializer.class);
        kafkaParams.put("value.deserializer", LongDeserializer.class);
        kafkaParams.put("group.id", "group");
        kafkaParams.put("auto.offset.reset", "latest");
        kafkaParams.put("enable.auto.commit", false);

        Collection<String> topics = Arrays.asList("numbers");

        JavaStreamingContext streamingContext = new JavaStreamingContext(sparkConf, Durations.seconds(1));

        JavaInputDStream<ConsumerRecord<String, String>> messages =
                KafkaUtils.createDirectStream(streamingContext, LocationStrategies.PreferConsistent(), ConsumerStrategies.Subscribe(topics, kafkaParams));

        JavaPairDStream<String, String> results = messages.mapToPair(record -> new Tuple2<>(record.key(), record.value()));

        JavaDStream<String> lines = results.map(Tuple2::_2);

        lines.foreachRDD(
                javaRdd -> {
                    javaRdd.foreach(number -> {
                        /**
                         * For some reason serialization doesn't work, so then values cannot be read.
                         */
                        long value = Long.parseLong(number);
                        System.out.println("Value: " + packetService.addCounter(value));
                    });
                });
        streamingContext.start();
        streamingContext.awaitTermination();
    }
}
