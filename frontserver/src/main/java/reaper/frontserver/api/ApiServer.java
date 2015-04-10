package reaper.frontserver.api;

import reaper.frontserver.exceptions.HttpExceptions;
import reaper.frontserver.server.AppServer;
import reaper.frontserver.server.request.Request;
import reaper.frontserver.server.request.RequestFactory;
import reaper.frontserver.services.auth.AuthService;
import reaper.frontserver.services.user.UserService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;


@Path("/v1.0/{uri: .*}")
public class ApiServer
{
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response jsonPost(@Context UriInfo uriInfo, String postDataJson)
    {
        System.out.println("request received");

        try
        {
            Request request = RequestFactory.create(uriInfo, postDataJson);

            String sessionId = request.getSessionId();
            if (sessionId == null)
            {
                throw new HttpExceptions.AuthenticationRequired();
            }

            AuthService authService = new AuthService();

            String userId = authService.getActiveUser(sessionId);
            if (userId == null)
            {
                throw new HttpExceptions.AuthenticationRequired();
            }

            request.setUser(userId);

            AppServer appServer = AppServer.getInstance();
            String response = appServer.dispatch(request);

            return Response.ok(response, MediaType.APPLICATION_JSON_TYPE).build();
        }
        catch (HttpExceptions.BadRequest e)
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        catch (HttpExceptions.ServerError e)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        catch (HttpExceptions.NotFound e)
        {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        catch (HttpExceptions.AuthenticationRequired authenticationRequired)
        {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
}