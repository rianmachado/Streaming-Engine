package rian.clouddevelopment.rest.client;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import rian.clouddevelopment.pojos.GeoLocation;

@ApplicationScoped
@RegisterRestClient()
@Path("/")
public interface ExtensionsServiceApiLocation {

	@GET
	GeoLocation searchLocationByIp(@QueryParam(value = "key") String key, @QueryParam(value = "ip") String ip);
	
}