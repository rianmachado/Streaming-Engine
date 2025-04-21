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
import rian.clouddevelopment.pojo.PlayerDeviceSummary;

@ApplicationScoped
public class InteractiveQueries {

	@Inject
	Logger logger;

	@Inject
	KafkaStreams streams;


	public List<PlayerDeviceSummary> getPlayerDeviceSummary() {
		List<PlayerDeviceSummary> allPlayerDeviceSummary = new ArrayList<>();
		KeyValueIterator<Long, PlayerDeviceSummary> list = getPlayerDeviceSummaryStreams().all();
		if (list != null) {
			while (list.hasNext()) {
				allPlayerDeviceSummary.add(list.next().value);
			}
		}
		return allPlayerDeviceSummary;
	}

	/*
	private ReadOnlyKeyValueStore<Long, PlayerDeviceSummary> getPlayerDeviceSummaryStreams() {
		while (true) {
			try {
				return streams.store(
						fromNameAndType(Global.PLAYERS_DEVICE_SUMMARY, QueryableStoreTypes.keyValueStore()));
			} catch (InvalidStateStoreException e) {
				logger.error(e.getLocalizedMessage());
			}
		}
	}
	*/
	
	private ReadOnlyKeyValueStore<Long, PlayerDeviceSummary> getPlayerDeviceSummaryStreams() {
	    for (int i = 0; i < 10; i++) { // Tenta por 10 vezes
	        try {
	            return streams.store(
	                fromNameAndType(Global.PLAYERS_DEVICE_SUMMARY, QueryableStoreTypes.keyValueStore()));
	        } catch (InvalidStateStoreException e) {
	            logger.warn("State store ainda não está pronto, tentando novamente...");
	            try {
	                Thread.sleep(2000); // Espera 2 segundo antes de tentar de novo
	            } catch (InterruptedException ignored) {
	            	logger.error(ignored.getLocalizedMessage());
	            }
	        }
	    }
	    throw new RuntimeException("State store não disponível após várias tentativas.");
	}

	

}