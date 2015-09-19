package reaper.frontserver.api;

import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import reaper.frontserver.exceptions.HttpExceptions;
import reaper.frontserver.server.request.Request;
import reaper.frontserver.server.request.RequestFactory;
import reaper.frontserver.services.auth.AuthService;
import reaper.frontserver.services.http.json.GsonProvider;
import reaper.frontserver.services.user.UserService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/v1.0/auth")
public class AuthServer
{
    private static Logger LOG = LogManager.getRootLogger();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response jsonPost(@Context UriInfo uriInfo, String postDataJson)
    {
        LOG.info("[REQUEST] " + uriInfo.getPath() + " -> " + postDataJson);

        try
        {
            Request request = RequestFactory.create(uriInfo, postDataJson);

            String userId = request.getData("id");
            String firstname = request.getData("first_name");
            String lastname = request.getData("last_name");
            String gender = request.getData("gender");
            String email = request.getData("email");
            String friends = request.getData("friend_list");

            if (userId == null || firstname == null || lastname == null || gender == null || email == null || friends == null)
            {
                throw new HttpExceptions.BadRequest("user_data is null");
            }

            UserService userService = new UserService();
            AuthService authService = new AuthService();

            if (!userService.isRegistered(userId))
            {
                LOG.info("[RESPONSE] New User -> " + userId + " : " + firstname + " " + lastname);

                userId = userService.register(userId, firstname, lastname, gender, email, friends);
                if (userId == null)
                {
                    throw new HttpExceptions.ServerError("unable to register user with appserver");
                }
                if (!userService.createUser(userId))
                {
                    throw new HttpExceptions.ServerError("unable to create user");
                }
            }
            else
            {
                LOG.info("[RESPONSE] Recurring User ->" + userId + " : " + firstname + " " + lastname);
            }

            String sessionId = authService.login(userId);
            if (sessionId == null)
            {
                throw new HttpExceptions.ServerError("unable to create new session for user = " + userId);
            }

            Map<String, String> response = new HashMap<>();
            response.put("_SESSIONID", sessionId);

            LOG.info("[RESPONSE] SUCCESS");

            String responseJson = GsonProvider.getGson().toJson(response);
            return Response.ok(responseJson, MediaType.APPLICATION_JSON_TYPE).build();
        }
        catch (HttpExceptions.BadRequest e)
        {
            LOG.error("[BAD REQUEST] " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        catch (HttpExceptions.ServerError e)
        {
            LOG.error("[INTERNAL SERVER ERROR] " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        catch (HttpExceptions.NotFound e)
        {
            LOG.error("[NOT FOUND] " + e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Path("validate")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateSession(@Context UriInfo uriInfo, String postDataJson)
    {
        LOG.info("[REQUEST] " + uriInfo.getPath() + " -> " + postDataJson);

        try
        {
            Request request = RequestFactory.create(uriInfo, postDataJson);

            String sessionId = request.getSessionId();
            if (sessionId == null)
            {
                throw new HttpExceptions.BadRequest("session_id is null/empty");
            }

            AuthService authService = new AuthService();
            String userId = authService.getActiveUser(sessionId);
            if (userId == null)
            {
                throw new HttpExceptions.AuthenticationRequired("invalid session");
            }

            LOG.info("[RESPONSE] SUCCESS");

            return Response.ok().build();
        }
        catch (HttpExceptions.BadRequest e)
        {
            LOG.error("[BAD REQUEST] " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        catch (HttpExceptions.ServerError e)
        {
            LOG.error("[INTERNAL SERVER ERROR] " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        catch (HttpExceptions.NotFound e)
        {
            LOG.error("[NOT FOUND] " + e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        catch (HttpExceptions.AuthenticationRequired e)
        {
            LOG.error("[AUTHENTICATION REQUIRED] " + e.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
}
