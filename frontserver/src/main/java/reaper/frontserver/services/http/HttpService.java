package reaper.frontserver.services.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import reaper.frontserver.exceptions.HttpExceptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HttpService
{
    public String post(String url, String jsonData) throws HttpExceptions.ServerError, HttpExceptions.NotFound, HttpExceptions.BadRequest
    {
        HttpClient client = HttpClientBuilder.create().build();

        HttpPost post = new HttpPost(url);
        post.setEntity(new StringEntity(jsonData, ContentType.APPLICATION_JSON));

        HttpResponse response = null;
        try
        {
            response = client.execute(post);
        }
        catch (IOException e)
        {
            throw new HttpExceptions.ServerError();
        }

        int responseCode = response.getStatusLine().getStatusCode();

        if (responseCode == 200)
        {
            try
            {
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                StringBuffer apiResponse = new StringBuffer();
                String line = "";
                while ((line = rd.readLine()) != null)
                {
                    apiResponse.append(line);
                }

                return apiResponse.toString();
            }
            catch (Exception e)
            {
                throw new HttpExceptions.ServerError();
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

    public String get(String url) throws HttpExceptions.ServerError, HttpExceptions.NotFound, HttpExceptions.BadRequest
    {
        HttpClient client = HttpClientBuilder.create().build();

        HttpGet get = new HttpGet(url);

        HttpResponse response = null;
        try
        {
            response = client.execute(get);
        }
        catch (IOException e)
        {
            throw new HttpExceptions.ServerError();
        }

        int responseCode = response.getStatusLine().getStatusCode();

        if (responseCode == 200)
        {
            try
            {
                BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));

                StringBuffer apiResponse = new StringBuffer();
                String line = "";
                while ((line = rd.readLine()) != null)
                {
                    apiResponse.append(line);
                }

                return apiResponse.toString();
            }
            catch (Exception e)
            {
                throw new HttpExceptions.ServerError();
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
