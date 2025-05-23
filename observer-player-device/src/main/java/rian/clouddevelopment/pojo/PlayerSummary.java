package rian.clouddevelopment.pojo;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;

@RegisterForReflection
@Getter
@Setter
public class PlayerSummary {

	private String playerID;
	private String displayID;
	private String snsName;
	private int deleted;
	private String updatetime;
	private String createtime;
	private String lastLogin;
	private int status;
	private int snsType;
	private String ipAddress;
	private int activated;
	private String countryCode;
	private String countryCodeIP;

	public PlayerSummary() {
	}

	public PlayerSummary(String playerID, String displayID, String snsName, int deleted, String updatetime,
			String createtime, String lastLogin, int status, int snsType, String ipAddress, int activated,
			String countryCode, String countryCodeIP) {
		super();
		this.playerID = playerID;
		this.displayID = displayID;
		this.snsName = snsName;
		this.deleted = deleted;
		this.updatetime = updatetime;
		this.createtime = createtime;
		this.lastLogin = lastLogin;
		this.status = status;
		this.snsType = snsType;
		this.ipAddress = ipAddress;
		this.activated = activated;
		this.countryCode = countryCode;
		this.countryCodeIP = countryCodeIP;
	}

}
