package rian.clouddevelopment.pojos;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@RegisterForReflection
@Getter
@Setter
@Builder
public class MessageWhats {
	private String phone;
	private String message;
}
