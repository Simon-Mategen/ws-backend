import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Map;

/**
 * Class containing the various functions used when responding to a call
 * @author Simon M�tegen, Hanna Ringkvist, Sonja Peric
 * @version 1
 */
public class Functions
{
    private Storage storage;

    /**
     * @param inStorage The storage object used to store ledamoter
     */
    public Functions(Storage inStorage)
    {
        this.storage = inStorage;
    }

    /**
     * Calls Twitters API and creates Tweet objects of the first three tweets
     * @param name Name of the selected ledamot
     * @return A list of maps of the tweets
     */
    public ArrayList<Map> readTweetsFromAPI(String name)
    {
        HttpClient client = HttpClient.newHttpClient();
        ArrayList<Map> tweetArray = new ArrayList<>();

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

            tweetArray.add(createTweetObject(tweet1).getAsMap());
            tweetArray.add(createTweetObject(tweet2).getAsMap());
            tweetArray.add(createTweetObject(tweet3).getAsMap());
        }
        catch (Exception e)
        {
            System.out.println("Twitter read " + e.getMessage());
        }

        return tweetArray;
    }

    /**
     * Creates a Tweet object from the information received by the Twitter APi
     * @param object A Json object containing the information from twitters API
     * @return A Tweet object
     */
    private Tweet createTweetObject(JsonObject object)
    {
        //Texten
        JsonPrimitive jText = object.getAsJsonPrimitive("text");
        String text = jText.getAsString();
        //Datumet
        JsonPrimitive jDate = object.getAsJsonPrimitive("created_at");
        String date = jDate.getAsString();
        //Anv�ndare
        JsonObject jUser = object.getAsJsonObject("user");
        JsonPrimitive jScreenName = jUser.getAsJsonPrimitive("screen_name");
        String screenName = jScreenName.getAsString();
        //URL
        JsonPrimitive jID = object.getAsJsonPrimitive("id");
        String id = jID.getAsString();
        String url = "https://twitter.com/" + screenName + "/status/" + id;

        return new Tweet(text, screenName, url, date);
    }

    /**
     * Returns a list of Ledamot objects created from the information received by the Swedish Riksdagen API
     * @return A list of ledamots
     */
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

    /**
     * Checks if the ledamots in storage should be uppdated. If storage is empty or it has been more then 12 hours from
     * the last update the storage is updated.
     */
    public void checkStorage()
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
                if ((LocalTime.now().until(storage.getTime(), ChronoUnit.HOURS) >= 12))
                {
                    System.out.println("Test");
                    updateStorage();
                }
            }
        }
    }

    /**
     * Creates a new list of ledamots from the Swedish Riksdagen API and replaces the ledamots in storage with the new
     * one.
     */
    public void updateStorage()
    {
        storage.addList(readFromRiksdagenAPI());
        storage.setLastUpdated(LocalDate.now(), LocalTime.now());
    }



}
