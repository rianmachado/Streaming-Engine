package rian.clouddevelopment.pojo;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;

@RegisterForReflection
@Getter
@Setter
public class PlayerIpAddress {

	private String playerID;

	public PlayerIpAddress(String playerID) {
		this.playerID = playerID;
	}

	public PlayerIpAddress() {
	}

	@Override
	public String toString() {
		return "PlayerIpAddress [playerID=" + playerID + "]";
	}

}