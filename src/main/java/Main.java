import org.eclipse.jetty.server.HttpConnection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static spark.Spark.*;
import com.google.gson.*;

public class Main
{

    public void readFromRiksdagenAPI()
    {
        HttpClient client = HttpClient.newHttpClient();

        try
        {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://data.riksdagen.se/personlista/?iid=&fnamn=&enamn=&f_ar=&kn=&parti=&valkrets=&rdlstatus=&org=&utformat=json&sort=sorteringsnamn&sortorder=asc&termlista="))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String textResponse = response.body();

            JsonParser parser = new JsonParser();
            JsonObject object = parser.parse(textResponse).getAsJsonObject();

            JsonObject personlista = object.getAsJsonObject("personlista");

            JsonArray person = personlista.getAsJsonArray("person");

            for (int i = 0; i < person.size(); i++)
            {
                JsonObject temp = (JsonObject) person.get(i);

                System.out.println(temp.get("sorteringsnamn") + "\n" + temp.get("bild_url_192"));
            }

        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args)
    {
/*        get("/api/v1/ledamoter", ((request, response) ->
        {


            return response.body();
        }));*/

        Main prog = new Main();
        prog.readFromRiksdagenAPI();
    }


}
