package rian.clouddevelopment.controller;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import rian.clouddevelopment.model.DeviceCount;
import rian.clouddevelopment.queries.InteractiveQueries;
import io.smallrye.mutiny.Uni;

@Path("/player")
public class PlayerCountResource {

	@Inject
	InteractiveQueries interactiveQueries;


	@GET
	@Path("/device-count")
	public Uni<List<DeviceCount>> playerDeviceSummary() {
		List<DeviceCount> allPlayerDeviceSummary = interactiveQueries.getDeviceCount();
		return Uni.createFrom().item(allPlayerDeviceSummary);
	}

}