package rian.clouddevelopment.processor;
import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import io.smallrye.reactive.messaging.annotations.Broadcast;
import rian.clouddevelopment.pojos.PlayerData;

@ApplicationScoped
public class ProcessorBroadcast {

    @Incoming("players")
    @Outgoing("players-broadcast")
    @Broadcast
    public PlayerData broadcastPlayers(PlayerData data) {
        return data;
    }
}