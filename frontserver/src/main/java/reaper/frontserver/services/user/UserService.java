package reaper.frontserver.services.user;

import reaper.frontserver.db.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.UUID;

public class UserService
{
    private static final String SQL_USER_FROM_FBID = "SELECT user_id FROM users WHERE facebook_id = ?";
    private static final String SQL_CREATE_USER = "INSERT INTO users (user_id, facebook_id) VALUES(?,?)";

    public String getFromFacebookId(String facebookId)
    {
        try
        {
            String userId = null;

            Connection connection = DataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_USER_FROM_FBID);
            statement.setString(1, facebookId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                userId = resultSet.getString(1);
                break;
            }
            resultSet.close();

            return userId;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public boolean createUser(String userId, String facebookId)
    {
        try
        {
            if (userId.isEmpty() || facebookId.isEmpty())
            {
                throw new Exception("Empty userId/facebookId");
            }

            Connection connection = DataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_CREATE_USER);
            statement.setString(1, userId);
            statement.setString(2, facebookId);

            statement.executeUpdate();

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
        return UUID.randomUUID().toString();
    }
}
