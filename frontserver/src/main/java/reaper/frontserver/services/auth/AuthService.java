package reaper.frontserver.services.auth;

import com.google.gson.annotations.SerializedName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import reaper.frontserver.db.DataSource;
import reaper.frontserver.exceptions.HttpExceptions;
import reaper.frontserver.server.AppServer;
import reaper.frontserver.server.request.Request;
import reaper.frontserver.services.http.json.GsonProvider;
import reaper.frontserver.services.user.SessionIdGenerator;
import reaper.frontserver.services.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class AuthService
{
    private static Logger LOG = LogManager.getRootLogger();

    private static final String USER_DETAILS_URI = "me";

    private static final String SQL_USER_FROM_SESSIONID = "SELECT user_id FROM sessions WHERE session_id = ?";
    private static final String SQL_CREATE_SESSION = "INSERT INTO sessions (session_id, user_id) VALUES(?,?)";

    public String getActiveUser(String sessionId)
    {
        try
        {
            String userId = null;

            Connection connection = DataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_USER_FROM_SESSIONID);
            statement.setString(1, sessionId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                userId = resultSet.getString(1);
                break;
            }
            resultSet.close();
            statement.close();
            connection.close();

            return userId;
        }
        catch (Exception e)
        {
            LOG.error("unable to fetch active user", e);
            return null;
        }
    }

    public String login(String userId)
    {
        try
        {
            String sessionId = SessionIdGenerator.get();

            Connection connection = DataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_CREATE_SESSION);
            statement.setString(1, sessionId);
            statement.setString(2, userId);

            statement.executeUpdate();
            statement.close();
            connection.close();

            return sessionId;
        }
        catch (Exception e)
        {
            LOG.error("unable to login user", e);
            return null;
        }
    }

    public Map<String, String> buildAuthResponse(String userId, String sessionId, boolean isNewUser) throws HttpExceptions.BadRequest, HttpExceptions.ServerError, HttpExceptions.NotFound
    {
        AppServer appServer = AppServer.getInstance();

        Request request = new Request(USER_DETAILS_URI, null);
        request.setUser(userId);

        String response = appServer.dispatch(request);
        User user = GsonProvider.getGson().fromJson(response, ActiveUser.class).getUser();

        Map<String, String> authResponse = new HashMap<>();
        authResponse.put("session_id", sessionId);
        authResponse.put("is_new_user", String.valueOf(isNewUser));
        authResponse.put("user_id", user.getId());
        authResponse.put("first_name", user.getFirstname());
        authResponse.put("last_name", user.getLastname());
        authResponse.put("email", user.getUsername());
        authResponse.put("gender", user.getGender());
        authResponse.put("phone", user.getPhone());

        return authResponse;
    }

    private static class ActiveUser
    {
        @SerializedName("active_user")
        User user;

        public User getUser()
        {
            return user;
        }
    }
}
