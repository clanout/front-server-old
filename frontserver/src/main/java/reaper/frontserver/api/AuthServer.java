package reaper.frontserver.api;

import com.google.gson.Gson;
import reaper.frontserver.exceptions.HttpExceptions;
import reaper.frontserver.server.AppServer;
import reaper.frontserver.server.request.Request;
import reaper.frontserver.server.request.RequestFactory;
import reaper.frontserver.services.auth.AuthService;
import reaper.frontserver.services.facebook.FacebookService;
import reaper.frontserver.services.user.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.HashMap;
import java.util.Map;

@Path("/v1.0/auth")
public class AuthServer
{
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response jsonPost(@Context UriInfo uriInfo, String postDataJson)
    {
        System.out.println("authentication");

        try
        {
            Request request = RequestFactory.create(uriInfo, postDataJson);

            String accessToken = request.getData("access_token");
            if (accessToken == null)
            {
                throw new HttpExceptions.BadRequest();
            }

            UserService userService = new UserService();
            AuthService authService = new AuthService();
            FacebookService facebookService = new FacebookService(accessToken);

            String facebookId = facebookService.getFacebookId(accessToken);
            if (facebookId == null)
            {
                throw new HttpExceptions.ServerError();
            }

            String userId = userService.getFromFacebookId(facebookId);
            if (userId == null)
            {
                userId = userService.register(facebookService.getFacebookData());
                if (userId == null)
                {
                    throw new HttpExceptions.ServerError();
                }
                if (!userService.createUser(userId, facebookId))
                {
                    throw new HttpExceptions.ServerError();
                }

                String sessionId = authService.login(userId);

                if (sessionId == null)
                {
                    throw new HttpExceptions.ServerError();
                }

                Map<String, String> response = new HashMap<>();
                response.put("_SESSIONID", sessionId);

                String responseJson = (new Gson()).toJson(response);

                return Response.ok(responseJson, MediaType.APPLICATION_JSON_TYPE).build();
            }
            else
            {
                String sessionId = authService.login(userId);

                if (sessionId == null)
                {
                    throw new HttpExceptions.ServerError();
                }

                Map<String, String> response = new HashMap<>();
                response.put("_SESSIONID", sessionId);

                String responseJson = (new Gson()).toJson(response);

                return Response.ok(responseJson, MediaType.APPLICATION_JSON_TYPE).build();
            }
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
    }

    @Path("validate")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateSession(@Context UriInfo uriInfo, String postDataJson)
    {
        System.out.println("validation");
        try
        {
            Request request = RequestFactory.create(uriInfo, postDataJson);

            String sessionId = request.getSessionId();
            System.out.println(sessionId);

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

            System.out.println("valid");

            return Response.ok().build();
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
