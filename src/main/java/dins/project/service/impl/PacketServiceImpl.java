package dins.project.service.impl;

import dins.project.service.KafkaService;
import dins.project.service.PacketService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.spark.api.java.JavaSparkContext;
import org.pcap4j.core.*;
import org.pcap4j.packet.Packet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:application.yml")
@Setter
public class PacketServiceImpl implements PacketService {

    private AtomicLong counter = new AtomicLong(0);

    @Value("${spark.packet.snapshot_bytes}")
    private int snapshotLengthBytes;
    @Value("${spark.packet.timeout}")
    private int readTimeoutMillis;
    @Value("${spark.ip}")
    private String setIp;

    @Override
    public void startTrafficControl() throws Exception {
        PcapHandle handle = deviceOpen("Ethernet");

        /**
         * Unfortunately, I have not succeed neither in capturing packets by Spark
         * nor in serializing Longs to kafka (and back).
         * Therefore, I left Spark-directed listener commented.
         * Now application works well, but it is not Spark-driven though.
         */
//        PacketListener listener = packet -> kafkaService.sendNumbers(packet.getRawData().length);

        PacketListener listener = packet -> counter.addAndGet(packet.getRawData().length);

        tryIpFilter(handle).loop(-1, listener);
        handle.close();
    }

    @Override
    public Long resetTrafficCounter(long newValue) {
        return counter.getAndSet(newValue);
    }

    @Override
    public Long addCounter(long value) {
        return counter.addAndGet(value);
    }

    private PcapHandle deviceOpen(String description) throws Exception {
        List<PcapNetworkInterface> devices = Pcaps.findAllDevs();
        PcapNetworkInterface device = devices.stream().filter(e -> e.getDescription().contains(description)).findFirst()
                .orElseThrow(RuntimeException::new);
        return device.openLive(snapshotLengthBytes,
                PcapNetworkInterface.PromiscuousMode.NONPROMISCUOUS, readTimeoutMillis);
    }

    private PcapHandle tryIpFilter(PcapHandle handle) throws Exception {
        if (setIp.contains("default")) return handle;
        String filter = String.format("ip host %s", setIp);
        handle.setFilter(filter, BpfProgram.BpfCompileMode.OPTIMIZE);
        return handle;
    }
}