package reaper.frontserver.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import reaper.frontserver.exceptions.HttpExceptions;
import reaper.frontserver.server.request.Request;
import reaper.frontserver.server.request.RequestFactory;
import reaper.frontserver.services.auth.AuthService;
import reaper.frontserver.services.feedback.FeedbackService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/v1.0/feedback")
public class FeedbackServer
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

            int type = -1;
            String comment;
            try
            {
                type = Integer.parseInt(request.getData("type"));
                comment = request.getData("comment");

                if (type == -1 || comment == null)
                {
                    throw new NullPointerException("rating/feedback is null");
                }
            }
            catch (Exception e)
            {
                throw new HttpExceptions.BadRequest(e.getMessage());
            }

            FeedbackService feedbackService = new FeedbackService();
            feedbackService.add(userId, type, comment);

            LOG.info("[RESPONSE] SUCCESS\n");
            return Response.ok().build();
        }
        catch (HttpExceptions.BadRequest e)
        {
            LOG.error("[BAD REQUEST] " + e.getMessage() + "\n");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        catch (HttpExceptions.ServerError e)
        {
            LOG.error("[INTERNAL SERVER ERROR] " + e.getMessage() + "\n");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        catch (HttpExceptions.NotFound e)
        {
            LOG.error("[NOT FOUND] " + e.getMessage() + "\n");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        catch (HttpExceptions.AuthenticationRequired e)
        {
            LOG.error("[AUTHENTICATION REQUIRED] " + e.getMessage() + "\n");
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
}