package reaper.frontserver.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/v1.0/notification/offline")
public class NotificationServer
{
    private static Logger LOG = LogManager.getRootLogger();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response jsonPost(@QueryParam("to") String to, @QueryParam("from") String from, String postDataJson)
    {
        LOG.info("[FROM] " + from);
        LOG.info("[TO] " + to);
        return Response.ok().build();
    }
}
