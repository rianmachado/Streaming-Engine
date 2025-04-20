package group.suprema.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import group.suprema.pojos.PlayerData;
import group.suprema.service.ServiceiPlataformIP2Location;

public class Main {

	public static void main(String[] args) {
		/*
		try	{
			ServiceiPlataformIP2Location service = new ServiceiPlataformIP2Location();
			PlayerData json = service.converteJsonToPlayer("src/main/resources/input-player.json");
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonNode = mapper.readTree(json.getData().getHardwareInfo());
			System.out.println("Sistema Operacional: " + jsonNode.get("sysos"));
			System.out.println("Emulator: " + jsonNode.get("Emulator"));
			System.out.println("OS Version: " + jsonNode.get("OS Version"));
			System.out.println("Model: " + jsonNode.get("Model"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		*/
	}

}
