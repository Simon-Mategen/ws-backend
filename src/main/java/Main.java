import org.eclipse.jetty.server.HttpConnection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import static spark.Spark.*;
import com.google.gson.*;

public class Main
{
    public static void main(String[] args)
    {
        Gson gson = new Gson();
        Storage storage = new Storage();
        Functions func = new Functions(storage);

        get("/api/v1/ledamoter", ((request, response) ->
        {
            func.checkStorage();

            ArrayList<Map> temp = storage.getLedamoter();

            response.type("application/json");
            response.body(gson.toJson(temp));

            return response.body();
        }));

        get("/api/v1/ledamoter/:id", ((request, response) ->
        {
            func.checkStorage();

            int chosenID = Integer.parseInt(request.params("id"));

            Map ledamot = storage.getLedamotMapAt(chosenID);

            response.type("application/json");
            response.body(gson.toJson(ledamot));

            return response.body();
        }));

        get("/api/v1/ledamoter/parti/:parti", ((request, response) ->
        {
            func.checkStorage();

            String chosenParty = request.params("parti");

            ArrayList<Map> tempList = storage.getLedamoterByParty(chosenParty);

            response.type("application/json");
            response.body(gson.toJson(tempList));

            return response.body();
        }));

        get("/api/v1/partier", ((request, response) ->
        {
            func.checkStorage();

            ArrayList<String> tempList = storage.getAvailablePartys();

            response.type("application/json");
            response.body(gson.toJson(tempList));

            return response.body();
        }));

        get("/api/v1/link/:id", ((request, response) ->
        {
            func.checkStorage();

            int choosenID = Integer.parseInt(request.params("id"));

            Ledamot tempLedamot = storage.getLedamotAt(choosenID);

            String link = "https://www.riksdagen.se/sv/ledamoter-partier/ledamot/filler-text_" + tempLedamot.getSourceID();

            response.type("application/json");
            response.body(gson.toJson(link));

            return response.body();
        }));

        get("/api/v1/tweet/:namn", ((request, response) ->
        {
            String nameToSearch = request.params("namn");

            ArrayList<Map> temp;

            temp = func.readTweetsFromAPI(nameToSearch);

            if (temp.size() == 0)
            {
                response.status(404);
                response.body("404: Tweets not found");
            }
            else
            {
                response.type("application/json");
                response.body(gson.toJson(temp));
            }

            response.type("application/json");
            response.body(gson.toJson(temp));

            return response.body();
        }));

    }
}
