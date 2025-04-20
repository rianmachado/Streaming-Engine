package group.suprema.processor;
import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import group.suprema.pojos.PlayerData;
import io.smallrye.reactive.messaging.annotations.Broadcast;

@ApplicationScoped
public class ProcessorBroadcast {

    @Incoming("players")
    @Outgoing("players-broadcast")
    @Broadcast
    public PlayerData broadcastPlayers(PlayerData data) {
        return data;
    }
}