package rian.clouddevelopment.pojos;

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

	private long playerID;
	private String sysos;
	private boolean emulador;
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
	private boolean root;
	

	public Device(long playerID, String sysos,boolean emulador, String model) {
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