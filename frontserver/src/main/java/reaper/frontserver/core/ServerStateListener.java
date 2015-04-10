package reaper.frontserver.core;

import reaper.frontserver.db.DataSource;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ServerStateListener implements ServletContextListener
{
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent)
    {
        System.out.println("Server Started");

        try
        {
            System.out.println("Initializing DB Connection Pool...");
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
        System.out.println("Closing DB Connection Pool...");
        DataSource.close();

        System.out.println("Server Stopped");
    }
}
