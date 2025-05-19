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
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;
import org.jboss.logging.Logger;

import io.quarkus.kafka.client.serialization.ObjectMapperSerde;
import rian.clouddevelopment.constants.Global;
import rian.clouddevelopment.pojo.Device;
import rian.clouddevelopment.pojo.PlayerDeviceSummary;
import rian.clouddevelopment.pojo.PlayerSummary;

@ApplicationScoped
public class TopologyPlayerDevice {

	@Inject
	Logger logger;

	@Produces
	public Topology getPlayerDeviceJoinFilter() {

	    logger.info("TopologyPlayerDevice SUCCESSFUL START....");

	    final StreamsBuilder builder = new StreamsBuilder();
	    final ObjectMapperSerde<PlayerSummary> playerSummarySerder = new ObjectMapperSerde<>(PlayerSummary.class);
	    final ObjectMapperSerde<Device> deviceSerder = new ObjectMapperSerde<>(Device.class);
	    final ObjectMapperSerde<PlayerDeviceSummary> playerDeviceSummarySerder = new ObjectMapperSerde<>(PlayerDeviceSummary.class);

	    final long retentionMs = Duration.ofHours(6).toMillis();

	    // KTable materializada com chave String
	    final Materialized<String, PlayerDeviceSummary, KeyValueStore<Bytes, byte[]>> materializedStore =
	        Materialized.<String, PlayerDeviceSummary, KeyValueStore<Bytes, byte[]>>as(Global.PLAYERS_DEVICE_SUMMARY)
	            .withKeySerde(Serdes.String())
	            .withValueSerde(playerDeviceSummarySerder)
	            .withLoggingEnabled(Map.of("retention.ms", String.valueOf(retentionMs)));

	    final KTable<String, PlayerDeviceSummary> playersDeviceSummaryTable = builder.table(
	        Global.PLAYERS_DEVICE_SUMMARY,
	        Consumed.with(Serdes.String(), playerDeviceSummarySerder),
	        materializedStore
	    );

	    final GlobalKTable<String, PlayerSummary> playerSummaryTable = builder.globalTable(
	        Global.PLAYERS_SUMMARY,
	        Consumed.with(Serdes.String(), playerSummarySerder)
	    );

	    final KStream<String, Device> deviceEvents = builder.stream(
	        Global.DEVICES,
	        Consumed.with(Serdes.String(), deviceSerder)
	    );

	    deviceEvents
	        .map((key, value) -> KeyValue.pair(value.getPlayerID(), value))
	        .join(
	            playerSummaryTable,
	            (playerId, device) -> playerId,
	            (device, playerSummary) -> new PlayerDeviceSummary(
	                String.valueOf(playerSummary.getPlayerID()),
	                playerSummary.getDisplayID(),
	                playerSummary.getSnsName(),
	                playerSummary.getLastLogin(),
	                device.getSysos(),
	                device.isEmulador(),
	                device.isRoot(),
	                device.getModel(),
	                device.getRegionName(),
	                device.getCityName()
	            )
	        )
	        .to(Global.PLAYERS_DEVICE_SUMMARY, Produced.with(Serdes.String(), playerDeviceSummarySerder));

	    logger.info("TopologyPlayerDevice SUCCESSFUL FINISH....");

	    return builder.build();
	}


}