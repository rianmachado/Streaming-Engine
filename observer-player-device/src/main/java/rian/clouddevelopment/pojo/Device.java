package rian.clouddevelopment.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@RegisterForReflection
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Builder
public class Device {

	private String playerID;
	private String sysos;
	private boolean emulador;
	private boolean root;
	private String model;
	private String countryCode;
	private String countryName;
	private String regionName;
	private String cityName;
	private String latitude;
	private String longitude;
	private boolean isProxy;
	private String zipCode;
	private String timeZone;

	public Device() {
	}

	public Device(String playerID, String sysos, boolean emulador, String model) {
		this.playerID = playerID;
		this.sysos = sysos;
		this.emulador = emulador;
		this.model = model;
	}

	@Override
	public String toString() {
		return "Device [playerID=" + playerID + ", sysos=" + sysos + ", emulador=" + emulador + ", model=" + model
				+ "]";
	}

}