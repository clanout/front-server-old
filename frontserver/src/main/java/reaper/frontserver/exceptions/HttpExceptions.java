package reaper.frontserver.exceptions;

public class HttpExceptions
{
    public static class AuthenticationRequired extends Exception
    {}

    public static class AccessDenied extends Exception
    {}

    public static class ServerError extends Exception
    {}

    public static class NotFound extends Exception
    {}

    public static class BadRequest extends Exception
    {}
}
