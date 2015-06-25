package reaper.frontserver.services.user;

import reaper.frontserver.db.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

public class UserService
{
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
            e.printStackTrace();
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
            e.printStackTrace();
            return false;
        }
    }

    public String register(Map<String, String> userData)
    {
        System.out.println(userData);
        return userData.get("id");

//        Request request = new Request(REGISTRATION_URI, userData);
//        AppServer appServer = AppServer.getInstance();
//        try
//        {
//            String response = appServer.dispatch(request);
//            String userId = BasicJsonParser.getValue(response, "user_id");
//            return userId;
//        }
//        catch (Exception e)
//        {
//            return null;
//        }
    }
}
