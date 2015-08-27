package reaper.frontserver.exceptions;

public class HttpExceptions
{
    public static class AuthenticationRequired extends Exception
    {
        public AuthenticationRequired()
        {
            super();
        }

        public AuthenticationRequired(String message)
        {
            super(message);
        }
    }

    public static class ServerError extends Exception
    {
        public ServerError()
        {
            super();
        }

        public ServerError(String message)
        {
            super(message);
        }
    }

    public static class NotFound extends Exception
    {
        public NotFound()
        {
            super();
        }

        public NotFound(String message)
        {
            super(message);
        }
    }

    public static class BadRequest extends Exception
    {
        public BadRequest()
        {
            super();
        }

        public BadRequest(String message)
        {
            super(message);
        }
    }
}
