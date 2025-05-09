package rian.clouddevelopment.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import rian.clouddevelopment.pojos.GeoLocation;
import rian.clouddevelopment.rest.client.ExtensionsServiceApiLocation;

@RequestScoped
public class ServiceiPlataformIP2Location {

	@Inject
	Logger logger;

	@Inject
	@RestClient
	ExtensionsServiceApiLocation extensionsService;

	@ConfigProperty(name = "app.config.apikey.ip2location")
	private String apiKey;

	public GeoLocation searchLocationByIp(String ip) {
		GeoLocation geoLocation = new GeoLocation();
		try {
			geoLocation = extensionsService.searchLocationByIp(apiKey, ip);
		} catch (Exception e) {
			logger.error("We had problems integrating with the external Service: IP2LOCATION.IO "
					+ e.getLocalizedMessage());
		}
		return geoLocation;
	}

}
