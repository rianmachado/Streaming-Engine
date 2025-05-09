package rian.clouddevelopment.rest.client;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import rian.clouddevelopment.pojos.MessageWhats;
import rian.clouddevelopment.util.ApplicationProperties;

@ApplicationScoped
@RegisterRestClient()
@Path("/api/wtpQueue")
public interface ExtensionsServiceApiWhats {
    @Path("/send")
    @ClientHeaderParam(name = "x-api-key", value = "{getApiKey}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @POST
    Response sendWhatsApp(MessageWhats messageWhats );
    
    static String getApiKey() {
        return ApplicationProperties.getApiKey();
    }
    
}