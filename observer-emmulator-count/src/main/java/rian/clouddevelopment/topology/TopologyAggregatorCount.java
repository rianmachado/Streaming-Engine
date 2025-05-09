package rian.clouddevelopment.topology;

import java.time.Duration;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;
import org.jboss.logging.Logger;

import io.quarkus.kafka.client.serialization.ObjectMapperSerde;
import rian.clouddevelopment.constants.Global;
import rian.clouddevelopment.model.DeviceCount;
import rian.clouddevelopment.pojo.Device;
import rian.clouddevelopment.pojo.PlayerSummary;

@ApplicationScoped
public class TopologyAggregatorCount {

	@Inject
	Logger logger;

	
	@Produces
	public Topology topologyAggregatorCount() {
	    final long retentionMs = Duration.ofHours(6).toMillis();

	    StreamsBuilder builder = new StreamsBuilder();

	    final ObjectMapperSerde<PlayerSummary> playerSerder = new ObjectMapperSerde<>(PlayerSummary.class);
	    final ObjectMapperSerde<Device> deviceSerder = new ObjectMapperSerde<>(Device.class);
	    final ObjectMapperSerde<DeviceCount> deviceCountSerder = new ObjectMapperSerde<>(DeviceCount.class);

	    final GlobalKTable<Long, PlayerSummary> playerSummaryTable = builder.globalTable(
	        Global.PLAYERS_SUMMARY,
	        Consumed.with(Serdes.Long(), playerSerder)
	    );

	    final KStream<String, Device> deviceEvents = builder.stream(
	        Global.DEVICES,
	        Consumed.with(Serdes.String(), deviceSerder)
	    );

	    deviceEvents
	        .filter((key, event) -> event.isEmulador())
	        .map((key, value) -> KeyValue.pair(value.getPlayerID(), value))
	        .join(
	            playerSummaryTable,
	            (playerId, devicePlayerId) -> playerId,
	            (device, player) -> device
	        )
	        .groupBy(
	            (playerId, device) -> device.getSysos(),
	            Grouped.with(Serdes.String(), deviceSerder)
	        )
	        .aggregate(
	            DeviceCount::new,
	            (sysos, device, deviceCount) -> deviceCount.aggregate(sysos),
	            Materialized.<String, DeviceCount, KeyValueStore<Bytes, byte[]>>as(Global.SUMMARY_COUNT_EMULATED_DEVICES_PLAYERS)
	                .withKeySerde(Serdes.String())
	                .withValueSerde(deviceCountSerder)
	                .withLoggingEnabled(Map.of("retention.ms", String.valueOf(retentionMs)))
	        )
	        .toStream()
	        .to(Global.SUMMARY_COUNT_EMULATED_DEVICES_PLAYERS, Produced.with(Serdes.String(), deviceCountSerder));

	    return builder.build();
	}


}
