package reaper.frontserver;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;


@Path("{uri: .*}")
public class FrontGateway
{
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String regularGet(@Context UriInfo uriInfo)
    {
        return "HelloWorld GET";
    }
}