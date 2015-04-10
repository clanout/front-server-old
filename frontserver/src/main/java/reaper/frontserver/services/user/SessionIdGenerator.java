package reaper.frontserver.services.user;

import java.util.UUID;

public class SessionIdGenerator
{
    public static String get()
    {
        return UUID.randomUUID().toString();
    }
}
