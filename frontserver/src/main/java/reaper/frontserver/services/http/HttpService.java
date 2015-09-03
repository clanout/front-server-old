package reaper.frontserver.services.http;

import com.squareup.okhttp.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import reaper.frontserver.exceptions.HttpExceptions;

import java.io.IOException;

public class HttpService
{
    private static OkHttpClient httpClient = new OkHttpClient();

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public String post(String url, String jsonData) throws HttpExceptions.ServerError, HttpExceptions.NotFound, HttpExceptions.BadRequest
    {
        try
        {
            RequestBody body = RequestBody.create(JSON, jsonData);

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            Response response = httpClient.newCall(request).execute();
            int responseCode = response.code();
            if (responseCode == 200)
            {
                return response.body().string();
            }
            else if (responseCode == 404)
            {
                throw new HttpExceptions.NotFound();
            }
            else if (responseCode == 400)
            {
                throw new HttpExceptions.BadRequest();
            }
            else
            {
                throw new HttpExceptions.ServerError();
            }
        }
        catch (Exception e)
        {
            throw new HttpExceptions.ServerError(e.getMessage());
        }
    }

    public String get(String url) throws HttpExceptions.ServerError, HttpExceptions.NotFound, HttpExceptions.BadRequest
    {
        Response response;

        try
        {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            response = httpClient.newCall(request).execute();
        }
        catch (Exception e)
        {
            throw new HttpExceptions.ServerError(e.getMessage());
        }

        int responseCode = response.code();
        if (responseCode == 200)
        {
            try
            {
                return response.body().string();
            }
            catch (IOException e)
            {
                throw new HttpExceptions.ServerError("Unable to process response body");
            }
        }
        else if (responseCode == 404)
        {
            throw new HttpExceptions.NotFound();
        }
        else if (responseCode == 400)
        {
            throw new HttpExceptions.BadRequest();
        }
        else
        {
            throw new HttpExceptions.ServerError();
        }
    }
}
