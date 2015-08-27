package reaper.frontserver.services.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import reaper.frontserver.db.DataSource;
import reaper.frontserver.server.AppServer;
import reaper.frontserver.server.request.Request;
import reaper.frontserver.services.http.json.BasicJsonParser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class UserService
{
    private static Logger LOG = LogManager.getRootLogger();

    private static final String REGISTRATION_URI = "register";

    private static final String SQL_CREATE_USER = "INSERT INTO users (user_id) VALUES(?)";
    private static final String SQL_READ_USER = "SELECT user_id FROM users WHERE user_id = ?";

    public boolean isRegistered(String userId)
    {
        boolean isRegistered = false;

        try
        {
            Connection connection = DataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_READ_USER);
            statement.setString(1, userId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
            {
                isRegistered = true;
                break;
            }

            resultSet.close();
            statement.close();
            connection.close();
        }
        catch (Exception e)
        {
            LOG.error("error while checking user registration status", e);
        }

        return isRegistered;
    }

    public boolean createUser(String userId)
    {
        try
        {
            Connection connection = DataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_CREATE_USER);
            statement.setString(1, userId);

            statement.executeUpdate();

            statement.close();
            connection.close();

            return true;
        }
        catch (Exception e)
        {
            LOG.error("error while creating user", e);
            return false;
        }
    }

    public String register(String id, String firstname, String lastname, String gender, String email)
    {
        Map<String, String> userData = new HashMap<>();
        userData.put("id", id);
        userData.put("first_name", firstname);
        userData.put("last_name", lastname);
        userData.put("gender", gender);
        userData.put("email", email);

        Request request = new Request(REGISTRATION_URI, userData);
        AppServer appServer = AppServer.getInstance();
        try
        {
            String response = appServer.dispatch(request);
            String userId = BasicJsonParser.getValue(response, "user_id");
            return userId;
        }
        catch (Exception e)
        {
            LOG.error("error while registering user with appserver", e);
            return null;
        }
    }
}
