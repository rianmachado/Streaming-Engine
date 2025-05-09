package rian.clouddevelopment.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import rian.clouddevelopment.enums.TypeAlert;

@RequestScoped
public class ServiceBuildMessage {

	@Inject
	Logger logger;

	private static final String TITLE = new String(Character.toChars(0373250)) + " Suprema Poker\n";

	public String getAlert(TypeAlert typeAlert) {
		StringBuilder builder = new StringBuilder(TITLE);
		if (TypeAlert.EMMULATOR.equals(typeAlert)) {
			builder.append("DISPOSITIVO EMULADO");
		} else if (TypeAlert.ROOT.equals(typeAlert)) {
			builder.append("DISPOSITIVO ROOT");
		}
		return builder.toString();
	}

}
