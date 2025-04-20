package group.suprema.pojos;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;

@RegisterForReflection
@Getter
@Setter
public class Data {
	private long playerID;
	private String displayID;
	private String snsName;
	private int deleted;
	private String updatetime;
	private String createtime;
	private String lastLogin;
	private int status;
	private int snsType;
	private String snsToken;
	private long coin;
	private long diamond;
	private long vipLevel;
	private String vipEndtime;
	private long billMoney;
	private long billCoin;
	private long billDiamond;
	private String billDate;
	private String iconID;
	private long myClubCount;
	private long joinClubCount;
	private long tutorialStep;
	private long gameCount;
	private long winCount;
	private String extra;
	private String ipAddress;
	private long activated;
	private String countryCode;
	private long festivalItem;
	private long banPlaying;
	private long certType;
	private long deepLinkPlayerID;
	private String countryCodeIP;
	private String onesignalID;
	private String pushToken;
	private String hardwareInfo;
	private String playerIntegrity;

}
