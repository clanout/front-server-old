package reaper.frontserver.services.http;

import com.squareup.okhttp.*;
import reaper.frontserver.exceptions.HttpExceptions;

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
        try
        {
            Request request = new Request.Builder()
                    .url(url)
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
        catch (HttpExceptions.NotFound | HttpExceptions.BadRequest | HttpExceptions.ServerError e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new HttpExceptions.ServerError(e.getMessage());
        }
    }
}
