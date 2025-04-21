package rian.clouddevelopment.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;

@RegisterForReflection
@Getter
@Setter
public class DeviceCount {

	private String sysos;
	private long count;

	public DeviceCount aggregate(String sysos) {
		this.sysos = sysos;
		this.count++;
		return this;
	}


}