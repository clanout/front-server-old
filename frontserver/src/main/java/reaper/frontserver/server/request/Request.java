package reaper.frontserver.server.request;

import reaper.frontserver.services.http.json.GsonProvider;

import java.util.HashMap;
import java.util.Map;

public class Request
{
    private static final String URI_KEY = "_URI";
    private static final String USER_KEY = "_ME";
    private static final String SESSION_KEY = "_SESSIONID";

    private Map<String, String> data;

    public Request(String uri, Map<String, String> data)
    {
        this.data = data;

        if (data == null)
        {
            this.data = new HashMap<>();
        }

        this.data.put(URI_KEY, uri);
    }

    public String getUri()
    {
        return data.get(URI_KEY);
    }

    public String getData(String key)
    {
        return data.get(key);
    }

    public String getSessionId()
    {
        return data.remove(SESSION_KEY);
    }

    public void setUser(String userId)
    {
        data.put(USER_KEY, userId);
    }

    @Override
    public String toString()
    {
        return GsonProvider.getGson().toJson(data);
    }
}
