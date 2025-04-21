package rian.clouddevelopment.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;

@RegisterForReflection
@Getter
@Setter
public class Metadata {

	private String timestamp;

	private String operation;

	@JsonProperty("record-type")
	private String recordType;

	@JsonProperty("partition-key-type")
	private String partitionKeyType;

	@JsonProperty("schema-name")
	private String schemaName;

	@JsonProperty("table-name")
	private String tableName;

	@JsonProperty("transaction-id")
	private long transactionId;

}
