package group.suprema.service;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import group.suprema.pojos.GeoLocation;

@ApplicationScoped
@RegisterRestClient()
@Path("/")
public interface ExtensionsService {

	@GET
	GeoLocation searchLocationByIp(@QueryParam(value = "key") String key, @QueryParam(value = "ip") String ip);
}