import org.eclipse.jetty.server.HttpConnection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import static spark.Spark.*;
import com.google.gson.*;

public class Main
{
    private static Storage storage = new Storage();

    public ArrayList<Ledamot> readFromRiksdagenAPI()
    {
        HttpClient client = HttpClient.newHttpClient();
        ArrayList<Ledamot> ledamots = new ArrayList<>();

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

            JsonArray personArray = personlista.getAsJsonArray("person");



            for (int i = 0; i < personArray.size(); i++)
            {
                JsonObject person = (JsonObject) personArray.get(i);


                JsonPrimitive firstNamePrim = person.getAsJsonPrimitive("tilltalsnamn");
                JsonPrimitive lastNamePrim = person.getAsJsonPrimitive("efternamn");
                String name = firstNamePrim.getAsString() + " " + lastNamePrim.getAsString();

                JsonPrimitive partiPrim = person.getAsJsonPrimitive("parti");
                String parti = partiPrim.getAsString();

                JsonPrimitive bildPrim = person.getAsJsonPrimitive("bild_url_192");
                String bild = bildPrim.getAsString();


                Ledamot ledamot = new Ledamot(i, name, parti, bild);

                ledamots.add(ledamot);
            }

        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        return ledamots;
    }

    

    public static void main(String[] args)

    {
        Main prog = new Main();
        Gson gson = new Gson();

        get("/api/v1/ledamoter", ((request, response) ->
        {
            if(storage.getList() == null)
            {
                storage.addList(prog.readFromRiksdagenAPI());
            }

            ArrayList<Map> temp = new ArrayList<>();

            for (Ledamot ledamot : storage.getList())
            {
                temp.add(ledamot.getAsMap());
            }

            response.type("application/json");
            response.body(gson.toJson(temp));

            return response.body();
        }));

        get("/api/v1/ledamoter/:id", ((request, response) ->
        {
            if(storage.getList() == null)
            {
                storage.addList(prog.readFromRiksdagenAPI());
            }

            int chosenID = Integer.parseInt(request.params("id"));

            Ledamot ledamot = storage.getLedamotAt(chosenID);

            response.type("application/json");
            response.body(gson.toJson(ledamot));

            return response.body();
        }));

        get("/api/v1/ledamoter/parti/:parti", ((request, response) ->
        {
            if(storage.getList() == null)
            {
                storage.addList(prog.readFromRiksdagenAPI());
            }

            String chosenParty = request.params("parti");

            ArrayList<Map> tempList = new ArrayList<>();

            for (int i = 0; i < storage.getSize(); i++)
            {
                Ledamot tempLedamot = storage.getLedamotAt(i);

                if (tempLedamot.getParty().equals(chosenParty))
                {
                    tempList.add(tempLedamot.getAsMap());
                }
            }

            response.type("application/json");
            response.body(gson.toJson(tempList));

            return response.body();
        }));

    }
}
