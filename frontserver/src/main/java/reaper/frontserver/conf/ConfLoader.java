package reaper.frontserver.conf;

import reaper.frontserver.conf.implementation.ApacheConf;

public class ConfLoader
{
    public static Conf getConf(ConfResource confResource)
    {
        return new ApacheConf(confResource.getResource());
    }
}
