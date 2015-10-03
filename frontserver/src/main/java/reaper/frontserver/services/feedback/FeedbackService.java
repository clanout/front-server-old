package reaper.frontserver.services.feedback;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import reaper.frontserver.db.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class FeedbackService
{
    private static Logger LOG = LogManager.getRootLogger();

    private static final String SQL_INSERT = "INSERT INTO user_feedback VALUES (?,?,?)";

    public void add(String userId, int type, String comment)
    {
        try
        {
            Connection connection = DataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT);
            statement.setString(1, userId);
            statement.setInt(2, type);
            statement.setString(3, comment);

            statement.executeUpdate();
            statement.close();
            connection.close();
        }
        catch (Exception e)
        {
            LOG.error("unable to add feedback", e);
        }
    }

}
