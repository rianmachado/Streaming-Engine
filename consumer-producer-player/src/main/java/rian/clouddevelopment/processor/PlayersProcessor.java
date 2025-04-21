package rian.clouddevelopment.processor;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.kafka.Record;
import rian.clouddevelopment.pojos.PlayerData;
import rian.clouddevelopment.pojos.PlayerSummary;

@ApplicationScoped
public class PlayersProcessor {

	@Inject
	private Logger logger;

	@Incoming("players-broadcast")
	@Outgoing("players-summary")
	public Multi<Record<Long, PlayerSummary>> generate(PlayerData playerData) {

		logger.info("Start producer PlayerSummary.....");

		try {
			PlayerSummary playerSummary = new PlayerSummary(playerData.getData().getPlayerID(),
					playerData.getData().getDisplayID(), playerData.getData().getSnsName(),
					playerData.getData().getDeleted(), playerData.getData().getUpdatetime(),
					playerData.getData().getCreatetime(), playerData.getData().getLastLogin(),
					playerData.getData().getStatus(), playerData.getData().getSnsType(),
					playerData.getData().getIpAddress(), playerData.getData().getDeleted(),
					playerData.getData().getCountryCode(), playerData.getData().getCountryCodeIP());
			return Multi.createFrom()
					.item(Record.of(playerSummary.getPlayerID(), playerSummary));
		} catch (Exception e) {
			logger.error("DADOS INVALIDOS NO TOPICO PLAYER: >>> " + e.getLocalizedMessage());
			return Multi.createFrom()
					.item(Record.of(0l, (new PlayerSummary(0l, e.getLocalizedMessage()))));
		}

	}

}
