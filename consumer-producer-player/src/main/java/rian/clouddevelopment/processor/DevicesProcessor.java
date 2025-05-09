package rian.clouddevelopment.processor;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.kafka.Record;
import rian.clouddevelopment.enums.TypeAlert;
import rian.clouddevelopment.pojos.Device;
import rian.clouddevelopment.pojos.GeoLocation;
import rian.clouddevelopment.pojos.PlayerData;
import rian.clouddevelopment.service.ServiceBuildMessage;
import rian.clouddevelopment.service.ServiceWhatsApp;
import rian.clouddevelopment.service.ServiceiPlataformIP2Location;

@ApplicationScoped
public class DevicesProcessor {

	@Inject
	private Logger logger;

	@Inject
	private ServiceiPlataformIP2Location serviceiPlataformIP2Location;

	@Inject
	private ServiceWhatsApp serviceWhatsApp;

	@Inject
	private ServiceBuildMessage serviceBuildMessage;

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

		StringBuilder builder = new StringBuilder();
		builder.append("\n");
		builder.append("Player ID: ");
		builder.append(device.getPlayerID());
		builder.append("\n");
		builder.append("Login: ");
		builder.append(playerData.getData().getSnsName());
		builder.append("\n");
		builder.append("IP: ");
		builder.append(playerData.getData().getIpAddress());
		String msg = builder.toString();
		logger.info("DEVICE DESCRIPTION: " + msg);

		if (device.isEmulador()) {
			String msgEmmlator = serviceBuildMessage.getAlert(TypeAlert.EMMULATOR);
			msgEmmlator = msgEmmlator + msg;
			serviceWhatsApp.alertDevice(msgEmmlator);
			
		}
		
		if (device.isRoot()) {
			String msgRoot = serviceBuildMessage.getAlert(TypeAlert.ROOT);
			msgRoot = msgRoot + msg;
			serviceWhatsApp.alertDevice(msgRoot);
		}

		return Multi.createFrom()
				.item(Record.of(playerData.getData().getIpAddress(), device));

	}

}
