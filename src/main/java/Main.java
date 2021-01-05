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

                JsonPrimitive sourceIDPrim = person.getAsJsonPrimitive("sourceid");
                String sourceID = sourceIDPrim.getAsString();


                Ledamot ledamot = new Ledamot(i, name, parti, bild, sourceID);

                ledamots.add(ledamot);
            }

        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        return ledamots;
    }

    public ArrayList<String> getAvailablePartys()
    {
        ArrayList<String> partys = new ArrayList<>();

        for (int i = 0; i < storage.getSize(); i++)
        {
            Ledamot tempLedamot = storage.getLedamotAt(i);

            if (!partys.contains(tempLedamot.getParty()) && !tempLedamot.getParty().equals("-"))
            {
                partys.add(tempLedamot.getParty());
            }
        }

        return partys;
    }

    public ArrayList<Tweet> readTweetsFromAPI(String name)
    {
        HttpClient client = HttpClient.newHttpClient();
        ArrayList<Tweet> tweets = new ArrayList<>();

        try
        {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://api.twitter.com/1.1/search/tweets.json?q=" + name))
                    .header("Authorization", "Bearer AAAAAAAAAAAAAAAAAAAAAKvUKQEAAAAAb3IZRryD44EeZps4n0fPZ3DI7qc%3DFo02KaSe9BOQ8gVA0bTwPkFFA5PNy3PIeW29mvxFKFRAJrCPHc")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String textResponse = response.body();

            JsonParser parser = new JsonParser();
            JsonObject object = parser.parse(textResponse).getAsJsonObject();

            JsonArray array = object.getAsJsonArray("statuses");

            JsonObject tweet1 = array.get(0).getAsJsonObject();
            JsonObject tweet2 = array.get(1).getAsJsonObject();
            JsonObject tweet3 = array.get(2).getAsJsonObject();


            tweets.add(createTweet(tweet1));
            tweets.add(createTweet(tweet2));
            tweets.add(createTweet(tweet3));
        }
        catch (Exception e)
        {
            System.out.println("Twitter read" + e.getMessage());
        }

        return tweets;
    }

    private Tweet createTweet(JsonObject object)
    {
        //Texten
        JsonPrimitive jText = object.getAsJsonPrimitive("text");
        String text = jText.getAsString();
        //Datumet
        JsonPrimitive jDate = object.getAsJsonPrimitive("created_at");
        String date = jDate.getAsString();
        //Användare
        JsonObject jUser = object.getAsJsonObject("user");
        JsonPrimitive jScreenName = jUser.getAsJsonPrimitive("screen_name");
        String screenName = jScreenName.getAsString();
        //URL
        JsonPrimitive jID = object.getAsJsonPrimitive("id");
        String id = jID.getAsString();
        String url = "https://twitter.com/" + screenName + "/status/" + id;

        Tweet tweet = new Tweet(text, screenName, url, date);

        return tweet;
    }

    private void checkStorage()
    {
        if(storage.getList() == null)
        {
            updateStorage();
        }
        else
        {
            if (LocalDate.now().isAfter(storage.getDate()))
            {
                updateStorage();
            }
            else
            {
                if ((LocalTime.now().until(storage.getTime(), ChronoUnit.HOURS) >= 24))
                {
                    updateStorage();
                }
            }
        }
    }

    private void updateStorage()
    {
        storage.addList(readFromRiksdagenAPI());
        storage.setLastUpdated(LocalDate.now(), LocalTime.now());
    }

    public static void main(String[] args)

    {
        Main prog = new Main();
        Gson gson = new Gson();

        options("/*",
                (request, response) -> {

                    String accessControlRequestHeaders = request
                            .headers("Access-Control-Request-Headers");
                    if (accessControlRequestHeaders != null) {
                        response.header("Access-Control-Allow-Headers",
                                accessControlRequestHeaders);
                    }

                    String accessControlRequestMethod = request
                            .headers("Access-Control-Request-Method");
                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods",
                                accessControlRequestMethod);
                    }

                    return "OK";
                });

        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

        get("/api/v1/ledamoter", ((request, response) ->
        {

            prog.checkStorage();

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
            prog.checkStorage();

            int chosenID = Integer.parseInt(request.params("id"));

            Ledamot ledamot = storage.getLedamotAt(chosenID);

            response.type("application/json");
            response.body(gson.toJson(ledamot));

            return response.body();
        }));

        get("/api/v1/ledamoter/parti/:parti", ((request, response) ->
        {
            prog.checkStorage();

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

        get("/api/v1/partier", ((request, response) ->
        {
            prog.checkStorage();

            ArrayList<String> tempList = prog.getAvailablePartys();

            response.type("application/json");
            response.body(gson.toJson(tempList));

            return response.body();
        }));

        get("/api/v1/link/:id", ((request, response) ->
        {
            prog.checkStorage();

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

            ArrayList<Tweet> temp = new ArrayList<>();

            temp = prog.readTweetsFromAPI(nameToSearch);

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
            return response.body();
        }));

    }
}
