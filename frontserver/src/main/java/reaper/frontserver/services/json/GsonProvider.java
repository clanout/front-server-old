package reaper.frontserver.services.json;

import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonProvider
{
    private static Gson gson = Converters.registerAll(new GsonBuilder()).create();

    public static Gson getGson()
    {
        return gson;
    }
}
