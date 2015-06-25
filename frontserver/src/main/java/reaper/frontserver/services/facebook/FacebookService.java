package reaper.frontserver.services.facebook;

import com.google.gson.reflect.TypeToken;
import reaper.frontserver.services.http.HttpService;
import reaper.frontserver.services.json.GsonProvider;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class FacebookService
{
    private static final String URL_OAUTH = "https://graph.facebook.com/v2.3/me?access_token=";

    private Map<String, String> facebookData;
    private String accessToken;

    public FacebookService(String accessToken)
    {
        this.accessToken = accessToken;
    }

    private boolean isValid()
    {
        if (facebookData == null)
        {
            return false;
        }

        if (facebookData.get("id") == null)
        {
            return false;
        }

        return true;
    }

    private void fetchData()
    {
        HttpService httpService = new HttpService();

        String facebookResponse = null;
        try
        {
            facebookResponse = httpService.get(URL_OAUTH + accessToken);
        }
        catch (Exception e)
        {
        }

        try
        {
            Type type = new TypeToken<Map<String, String>>()
            {
            }.getType();
            facebookData = GsonProvider.getGson().fromJson(facebookResponse, type);
        }
        catch (Exception e)
        {
            facebookData = new HashMap<>();
        }

    }


    public String getUserId(String accessToken)
    {
        if (!isValid())
        {
            fetchData();
        }

        return facebookData.get("id");
    }

    public Map<String, String> getFacebookData()
    {
        if (!isValid())
        {
            fetchData();
        }

        return facebookData;
    }
}
