package group.suprema.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import group.suprema.pojos.GeoLocation;

@RequestScoped
public class ServiceiPlataformIP2Location {

	@Inject
	Logger logger;

	@Inject
	@RestClient
	ExtensionsService extensionsService;

	@ConfigProperty(name = "app.config.apikey.ip2location")
	private String apiKey;

	public GeoLocation searchLocationByIp(String ip) {
		GeoLocation geoLocation = new GeoLocation();
		try {
			geoLocation = extensionsService.searchLocationByIp(apiKey, ip);
		} catch (Exception e) {
			logger.error("We had problems integrating with the external Service: ip2location.io "
					+ e.getLocalizedMessage());
		}
		return geoLocation;
	}

}
