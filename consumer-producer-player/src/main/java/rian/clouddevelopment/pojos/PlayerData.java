package rian.clouddevelopment.pojos;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;

@RegisterForReflection
@Getter
@Setter
public class PlayerData {
	private Data data;
	private Metadata metadata;

	public PlayerData() {
	}

}
