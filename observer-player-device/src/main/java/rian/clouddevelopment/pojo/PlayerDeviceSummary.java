package rian.clouddevelopment.pojo;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;

@RegisterForReflection
@Getter
@Setter
public class PlayerDeviceSummary {

	private long playerID;
	private String displayID;
	private String snsName;
	private String lastLogin;
	private String sysos;
	private boolean emulador;
	private boolean root;
	private String model;
	private String regionName;
	private String cityName;

	public PlayerDeviceSummary() {
	}

	public PlayerDeviceSummary(long playerID, String displayID, String snsName, String lastLogin, String sysos,
			boolean emulador, Boolean root, String model, String regionName, String cityName) {
		super();
		this.playerID = playerID;
		this.displayID = displayID;
		this.snsName = snsName;
		this.lastLogin = lastLogin;
		this.sysos = sysos;
		this.emulador = emulador;
		this.root = root;
		this.model = model;
		this.regionName = regionName;
		this.cityName = cityName;
	}

}
