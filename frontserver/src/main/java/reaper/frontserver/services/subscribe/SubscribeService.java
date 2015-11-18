package reaper.frontserver.services.subscribe;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import reaper.frontserver.db.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class SubscribeService
{
    private static Logger LOG = LogManager.getRootLogger();

    private static final String SQL_INSERT = "INSERT INTO subscriptions VALUES (?,?)";

    public void add(String email)
    {
        try
        {
            Connection connection = DataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT);
            statement.setString(1, email);
            statement.setTimestamp(2, Timestamp.from(OffsetDateTime.now().atZoneSameInstant(ZoneOffset.UTC).toInstant()));
            statement.executeUpdate();
            statement.close();
            connection.close();
        }
        catch (Exception e)
        {
            LOG.error("unable to add subscription", e);
        }
    }

}
