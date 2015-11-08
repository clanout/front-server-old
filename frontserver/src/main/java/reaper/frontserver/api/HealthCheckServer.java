package reaper.frontserver.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/health_check")
public class HealthCheckServer
{
    @GET
    public Response healthCheck()
    {
        return Response.ok().build();
    }
}
