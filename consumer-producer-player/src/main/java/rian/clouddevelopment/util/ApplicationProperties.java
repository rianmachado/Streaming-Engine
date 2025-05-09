package rian.clouddevelopment.util;
import org.eclipse.microprofile.config.ConfigProvider;


public class ApplicationProperties {
    public static String getApiKey() {
        return ConfigProvider.getConfig().getValue("app.config.apikey.whatsapp", String.class);
    }
}
