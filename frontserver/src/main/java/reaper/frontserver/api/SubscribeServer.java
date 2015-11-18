package reaper.frontserver.api;

import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import reaper.frontserver.exceptions.HttpExceptions;
import reaper.frontserver.server.request.Request;
import reaper.frontserver.server.request.RequestFactory;
import reaper.frontserver.services.auth.AuthService;
import reaper.frontserver.services.feedback.FeedbackService;
import reaper.frontserver.services.http.json.GsonProvider;
import reaper.frontserver.services.subscribe.SubscribeService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.lang.reflect.Type;
import java.util.Map;

@Path("/v1.0/subscribe")
public class SubscribeServer
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
            Type type = new TypeToken<Map<String, String>>()
            {
            }.getType();
            Map<String, String> data = GsonProvider.getGson().fromJson(postDataJson, type);
            if(data == null)
            {
                throw new HttpExceptions.BadRequest();
            }

            String email = data.get("email");
            if (email == null || email.isEmpty())
            {
                throw new HttpExceptions.BadRequest();
            }

            SubscribeService subscribeService = new SubscribeService();
            subscribeService.add(email);

            return Response.ok().build();
        }
        catch (HttpExceptions.BadRequest e)
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}