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
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.jboss.logging.Logger;

import rian.clouddevelopment.constants.Global;
import rian.clouddevelopment.model.DeviceCount;
import rian.clouddevelopment.pojo.Device;
import rian.clouddevelopment.pojo.PlayerSummary;
import io.quarkus.kafka.client.serialization.ObjectMapperSerde;

@ApplicationScoped
public class TopologyAggregatorCount {

	@Inject
	Logger logger;

	
	@Produces
	public Topology topologyAggregatorCount() {
	    final long retentionMs = Duration.ofHours(6).toMillis(); // Ex: 6 horas de retenção

	    KeyValueBytesStoreSupplier storeSupplier = Stores.persistentKeyValueStore(
	        Global.PLAYERS_DEVICE_NAO_EMULADOR_SUMMARY_COUNT
	    );

	    // Criação da store com logging configurado
	    StoreBuilder<KeyValueStore<String, DeviceCount>> storeBuilder = Stores.keyValueStoreBuilder(
	            storeSupplier,
	            Serdes.String(),
	            new ObjectMapperSerde<>(DeviceCount.class)
	    ).withLoggingEnabled(Map.of("retention.ms", String.valueOf(retentionMs)));

	    StreamsBuilder builder = new StreamsBuilder();

	    // Registro manual da store no builder
	    builder.addStateStore(storeBuilder);

	    ObjectMapperSerde<PlayerSummary> playerSerder = new ObjectMapperSerde<>(PlayerSummary.class);
	    ObjectMapperSerde<Device> deviceSerder = new ObjectMapperSerde<>(Device.class);
	    ObjectMapperSerde<DeviceCount> deviceCountSerder = new ObjectMapperSerde<>(DeviceCount.class);

	    GlobalKTable<Long, PlayerSummary> playerSummaryTable = builder.globalTable(
	        Global.PLAYERS_SUMMARY,
	        Consumed.with(Serdes.Long(), playerSerder)
	    );

	    KStream<String, Device> deviceEvents = builder.stream(
	        Global.DEVICES,
	        Consumed.with(Serdes.String(), deviceSerder)
	    );

	    deviceEvents
	        .filter((key, event) -> event.isRoot())
	        .map((key, value) -> KeyValue.pair(value.getPlayerID(), value))
	        .join(
	            playerSummaryTable,
	            (playerId, devicePlayerId) -> playerId,
	            (device, player) -> device // Mantém o Device na agregação
	        )
	        .groupBy(
	            (playerId, device) -> device.getSysos(),
	            Grouped.with(Serdes.String(), deviceSerder)
	        )
	        .aggregate(
	            DeviceCount::new,
	            (sysos, device, deviceCount) -> deviceCount.aggregate(sysos),
	            Materialized.<String, DeviceCount, KeyValueStore<Bytes, byte[]>>with(
	                Serdes.String(),
	                deviceCountSerder
	            )
	        )
	        .toStream()
	        .to(Global.PLAYERS_DEVICE_NAO_EMULADOR_SUMMARY_COUNT, Produced.with(Serdes.String(), deviceCountSerder));

	    return builder.build();
	}

}
