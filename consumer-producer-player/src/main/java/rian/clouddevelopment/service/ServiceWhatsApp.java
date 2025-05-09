package rian.clouddevelopment.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import rian.clouddevelopment.pojos.MessageWhats;
import rian.clouddevelopment.rest.client.ExtensionsServiceApiWhats;

@RequestScoped
public class ServiceWhatsApp {

	@Inject
	Logger logger;

	@Inject
	@RestClient
	ExtensionsServiceApiWhats extensionsService;
	
	@ConfigProperty(name = "app.config.number.whatsapp")
	private String number;

	public void alertDevice(String msg) {
		
		MessageWhats body = MessageWhats.builder().message(msg).phone(number).build();
		try {
			Response response = extensionsService.sendWhatsApp(body);
			logger.info("MENSAGEM ENVIADA PARA WHATS COM SUCESSO!");
			logger.info("Status: " + response.getStatus());
			
		} catch (Exception e) {
			logger.error("We had problems integrating with the external Service: POKERBYTE.COM.BR/API/WTPQUEUE "
					+ e.getLocalizedMessage());
		}
		
	}

}
