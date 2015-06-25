package reaper.frontserver.services.auth;

import reaper.frontserver.db.DataSource;
import reaper.frontserver.services.user.SessionIdGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AuthService
{
    private static final String SQL_USER_FROM_SESSIONID = "SELECT user_id FROM sessions WHERE session_id = ?";
    private static final String SQL_CREATE_SESSION = "INSERT INTO sessions (session_id, user_id) VALUES(?,?)";

    public String getActiveUser(String sessionId)
    {
        return "9320369679";
//        try
//        {
//            String userId = null;
//
//            Connection connection = DataSource.getConnection();
//            PreparedStatement statement = connection.prepareStatement(SQL_USER_FROM_SESSIONID);
//            statement.setString(1, sessionId);
//
//            ResultSet resultSet = statement.executeQuery();
//
//            while (resultSet.next())
//            {
//                userId = resultSet.getString(1);
//                break;
//            }
//            resultSet.close();
//
//            return userId;
//        }
//        catch (Exception e)
//        {
//            return null;
//        }
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

            return sessionId;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
