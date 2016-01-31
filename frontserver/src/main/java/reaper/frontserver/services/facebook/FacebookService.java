package reaper.frontserver.services.facebook;

import com.google.gson.annotations.SerializedName;
import reaper.frontserver.exceptions.HttpExceptions;
import reaper.frontserver.services.http.HttpService;
import reaper.frontserver.services.http.json.GsonProvider;

public class FacebookService
{
    private static final String USER_DATA_REQUEST_URL = "https://graph.facebook.com/v2.5/me" +
            "?fields=id,first_name,last_name,email,gender" +
            "&access_token=";


    private HttpService httpService;

    public FacebookService()
    {
        httpService = new HttpService();
    }

    public UserData getFacebookData(String accessToken)
    {
        try
        {
            String responseJson = httpService.get(USER_DATA_REQUEST_URL + accessToken);
            UserData userData = GsonProvider.getGson().fromJson(responseJson, UserData.class);
            return userData;
        }
        catch (HttpExceptions.ServerError | HttpExceptions.NotFound | HttpExceptions.BadRequest serverError)
        {
            return null;
        }
    }

    public static class UserData
    {
        @SerializedName("id")
        public String id;

        @SerializedName("first_name")
        public String firstname;

        @SerializedName("last_name")
        public String lastname;

        @SerializedName("email")
        public String email;

        @SerializedName("gender")
        public String gender;
    }
}
