package reaper.frontserver.server.request;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import reaper.frontserver.exceptions.HttpExceptions;

import javax.ws.rs.core.UriInfo;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.Map;

public class RequestFactory
{
    private static final int VERSION_PREFIX_LENGTH = 5;

    public static Request create(UriInfo uriInfo, String postDataJson) throws HttpExceptions.BadRequest, HttpExceptions.ServerError, HttpExceptions.NotFound
    {
        return new Request(getUri(uriInfo), getData(postDataJson));
    }

    private static String getUri(UriInfo uriInfo) throws HttpExceptions.NotFound, HttpExceptions.BadRequest, HttpExceptions.ServerError
    {
        try
        {
            URI uri = new URI(uriInfo.getPath());
            String uriStr = uri.normalize().toString();
            String apiUri = uriStr.substring(VERSION_PREFIX_LENGTH);
            if (apiUri.isEmpty())
            {
                throw new HttpExceptions.NotFound();
            }
            return apiUri;
        }
        catch (Exception e)
        {
            throw new HttpExceptions.ServerError();
        }
    }

    private static Map<String, String> getData(String postDataJson) throws HttpExceptions.BadRequest
    {
        if (postDataJson != null)
        {
            try
            {
                Type type = new TypeToken<Map<String, String>>()
                {
                }.getType();
                Map<String, String> data = (new Gson()).fromJson(postDataJson, type);
                return data;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                throw new HttpExceptions.BadRequest();
            }
        }
        else
        {
            throw new HttpExceptions.BadRequest();
        }
    }
}
