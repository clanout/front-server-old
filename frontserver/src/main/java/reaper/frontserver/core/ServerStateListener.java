package reaper.frontserver.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import reaper.frontserver.db.DataSource;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ServerStateListener implements ServletContextListener
{
    private static Logger LOG = LogManager.getRootLogger();

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent)
    {
        LOG.info("[[ FRONT SERVER STARTED ]]");

        try
        {
            LOG.info("[ Initializing DB Connection Pool ]");
            DataSource.init();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent)
    {
        LOG.info("[ Closing DB Connection Pool ]");
        DataSource.close();

        LOG.info("[[ FRONT SERVER STOPPED ]]");
    }
}
