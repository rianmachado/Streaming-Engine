package rian.clouddevelopment.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;

@RegisterForReflection
@Getter
@Setter
public class GeoLocation {

	public GeoLocation() {
	}

	@JsonProperty("country_code")
	private String countryCode;

	@JsonProperty("country_name")
	private String countryName;

	@JsonProperty("region_name")
	private String regionName;

	@JsonProperty("city_name")
	private String cityName;

	@JsonProperty("latitude")
	private String latitude;

	@JsonProperty("longitude")
	private String longitude;

	@JsonProperty("is_proxy")
	private boolean isProxy;

	@JsonProperty("zip_code")
	private String zipCode;

	@JsonProperty("time_zone")
	private String timeZone;

}
