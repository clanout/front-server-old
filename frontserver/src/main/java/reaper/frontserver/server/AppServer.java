package reaper.frontserver.server;

import reaper.frontserver.conf.ConfLoader;
import reaper.frontserver.conf.ConfResource;
import reaper.frontserver.exceptions.HttpExceptions;
import reaper.frontserver.server.request.Request;
import reaper.frontserver.services.http.HttpService;

public class AppServer
{
    private static AppServer appServer;
    private static final String APP_SERVER_ROOT = ConfLoader.getConf(ConfResource.SERVER).get("appserver.url") + "/";

    private HttpService httpService;

    public static AppServer getInstance()
    {
        if (appServer == null)
        {
            return new AppServer();
        }
        else
        {
            return appServer;
        }
    }

    private AppServer()
    {
        httpService = new HttpService();
    }

    public String dispatch(Request request) throws HttpExceptions.NotFound, HttpExceptions.ServerError
    {
        return httpService.post(buildUrl(request.getUri()), request.toString());
    }

    private static String buildUrl(String uri)
    {
        return APP_SERVER_ROOT + uri;
    }

}
