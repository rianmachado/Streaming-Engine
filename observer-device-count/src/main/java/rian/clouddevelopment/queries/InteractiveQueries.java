package rian.clouddevelopment.queries;

import static org.apache.kafka.streams.StoreQueryParameters.fromNameAndType;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.errors.InvalidStateStoreException;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.jboss.logging.Logger;

import rian.clouddevelopment.constants.Global;
import rian.clouddevelopment.model.DeviceCount;

@ApplicationScoped
public class InteractiveQueries {

	@Inject
	Logger logger;

	@Inject
	KafkaStreams streams;

	public List<DeviceCount> getDeviceCount() {
		List<DeviceCount> allPlayerDeviceSummaryCount = new ArrayList<>();
		KeyValueIterator<String, DeviceCount> list = getDeviceCountStore().all();
		if (list != null) {
			while (list.hasNext()) {
				allPlayerDeviceSummaryCount.add(list.next().value);
			}
		}
		return allPlayerDeviceSummaryCount;
	}

	private ReadOnlyKeyValueStore<String, DeviceCount> getDeviceCountStore() {
		while (true) {
			try {
				return streams.store(fromNameAndType(Global.PLAYERS_DEVICE_NAO_EMULADOR_SUMMARY_COUNT,
						QueryableStoreTypes.keyValueStore()));

			} catch (InvalidStateStoreException e) {
				logger.error(e.getLocalizedMessage());
			}
		}
	}

}