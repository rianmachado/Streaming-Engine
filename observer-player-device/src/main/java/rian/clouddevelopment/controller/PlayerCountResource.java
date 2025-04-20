package group.suprema.controller;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import group.suprema.pojo.PlayerDeviceSummary;
import group.suprema.queries.InteractiveQueries;
import io.smallrye.mutiny.Uni;

@Path("/player")
public class PlayerCountResource {

	@Inject
	InteractiveQueries interactiveQueries;


	@GET
	@Path("/device")
	public Uni<List<PlayerDeviceSummary>> playerDeviceSummary() {
		List<PlayerDeviceSummary> allPlayerDeviceSummary = interactiveQueries.getPlayerDeviceSummary();
		return Uni.createFrom().item(allPlayerDeviceSummary);
	}

}