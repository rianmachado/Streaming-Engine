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
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.jboss.logging.Logger;

import rian.clouddevelopment.constants.Global;
import rian.clouddevelopment.pojo.Device;
import rian.clouddevelopment.pojo.PlayerDeviceSummary;
import rian.clouddevelopment.pojo.PlayerSummary;
import io.quarkus.kafka.client.serialization.ObjectMapperSerde;

@ApplicationScoped
public class TopologyPlayerDevice {

	@Inject
	Logger logger;

	@Produces
	public Topology getPlayerDeviceJoinFilter() {

	    logger.info(" TopologyPlayerDevice SUCCESSFUL START.... ");

	    final StreamsBuilder builder = new StreamsBuilder();
	    final ObjectMapperSerde<PlayerSummary> playerSummarySerder = new ObjectMapperSerde<>(PlayerSummary.class);
	    final ObjectMapperSerde<Device> deviceSerder = new ObjectMapperSerde<>(Device.class);
	    final ObjectMapperSerde<PlayerDeviceSummary> playerDeviceSummarySerder = new ObjectMapperSerde<>(PlayerDeviceSummary.class);

	    // Define retenção de 6 horas
	    final long retentionMs = Duration.ofHours(6).toMillis();

	    // Criação da store com logging configurado
	    StoreBuilder<KeyValueStore<Long, PlayerDeviceSummary>> storeBuilder =
	        Stores.keyValueStoreBuilder(
	            Stores.persistentKeyValueStore("players-device-summary"),
	            Serdes.Long(),
	            playerDeviceSummarySerder
	        ).withLoggingEnabled(Map.of("retention.ms", String.valueOf(retentionMs)));

	    // Adiciona a store ao builder manualmente
	    builder.addStateStore(storeBuilder);

	    // Usa Materialized.with apenas para os serdes, sem usar .as()
	    final Materialized<Long, PlayerDeviceSummary, KeyValueStore<Bytes, byte[]>> materializedStore =
	        Materialized.<Long, PlayerDeviceSummary, KeyValueStore<Bytes, byte[]>>with(
	            Serdes.Long(),
	            playerDeviceSummarySerder
	        );

	    // Tabela materializada com nome e store já registrada
	    final KTable<Long, PlayerDeviceSummary> playersDeviceSummaryTable = builder.table(
	        Global.PLAYERS_DEVICE_SUMMARY,
	        Consumed.with(Serdes.Long(), playerDeviceSummarySerder),
	        materializedStore
	    );

	    final GlobalKTable<Long, PlayerSummary> playerSummaryTable = builder.globalTable(
	        Global.PLAYERS_SUMMARY,
	        Consumed.with(Serdes.Long(), playerSummarySerder)
	    );

	    final KStream<String, Device> deviceEvents = builder.stream(
	        Global.DEVICES,
	        Consumed.with(Serdes.String(), deviceSerder)
	    );

	    deviceEvents
	        .filter((ipAddress, event) -> event.getPlayerID() != 0)
	        .map((key, value) -> KeyValue.pair(value.getPlayerID(), value))
	        .join(
	            playerSummaryTable,
	            (playerSummaryId, devicePlayedId) -> playerSummaryId,
	            (device, playerSummary) -> new PlayerDeviceSummary(
	                playerSummary.getPlayerID(),
	                playerSummary.getDisplayID(),
	                playerSummary.getSnsName(),
	                playerSummary.getLastLogin(),
	                device.getSysos(),
	                device.isEmulador(),
	                device.getModel(),
	                device.getRegionName(),
	                device.getCityName()
	            )
	        )
	        .to(Global.PLAYERS_DEVICE_SUMMARY, Produced.with(Serdes.Long(), playerDeviceSummarySerder));

	    logger.info(" TopologyPlayerDevice SUCCESSFUL FINISH.... ");

	    return builder.build();
	}


	
}