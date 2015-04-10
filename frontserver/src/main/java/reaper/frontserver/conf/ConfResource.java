package reaper.frontserver.conf;

import java.net.URL;

public enum ConfResource
{
    SERVER("server.conf"),
    API("api.conf"),
    DB("db.conf");

    private String FILE;

    private ConfResource(String filename)
    {
        this.FILE = filename;
    }

    public URL getResource()
    {
        return ConfResource.class.getResource("/conf/" + FILE);
    }
}
