package group.suprema.processor;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import group.suprema.pojos.Device;
import group.suprema.pojos.GeoLocation;
import group.suprema.pojos.PlayerData;
import group.suprema.service.ServiceiPlataformIP2Location;
import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.kafka.Record;

@ApplicationScoped
public class DevicesProcessor {

	@Inject
	private Logger logger;

	@Inject
	private ServiceiPlataformIP2Location serviceiPlataformIP2Location;

	@Incoming("players-broadcast")
    @Outgoing("devices")
	public Multi<Record<String, Device>> generate(PlayerData playerData) {

		logger.info("Start producer Devices.....");

		GeoLocation geolocatoin = serviceiPlataformIP2Location.searchLocationByIp(playerData.getData().getIpAddress());

		String sysos = "Not Found";
		boolean emulator = false;
		String model = "Not Found";
		boolean root = false;
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonNode = mapper.readTree(playerData.getData().getHardwareInfo());
			sysos = jsonNode.get("sysos").asText();
			emulator = jsonNode.get("Emulator").asBoolean();
			model = jsonNode.get("Model").asText();
			root = jsonNode.get("root").asBoolean();
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}

		Device device = Device.builder().playerID(playerData.getData().getPlayerID()).sysos(sysos).emulador(emulator)
				.model(model)
				.countryCode(geolocatoin.getCountryCode()).countryName(geolocatoin.getCountryName())
				.isProxy(geolocatoin.isProxy()).latitude(geolocatoin.getLatitude()).cityName(geolocatoin.getCityName())
				.longitude(geolocatoin.getLongitude()).regionName(geolocatoin.getRegionName())
				.timeZone(geolocatoin.getTimeZone()).zipCode(geolocatoin.getZipCode()).root(root).build();

		return Multi.createFrom()
				.item(Record.of(playerData.getData().getIpAddress(), device));

	}

}
