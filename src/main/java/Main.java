import java.util.ArrayList;
import java.util.Map;
import static spark.Spark.*;
import com.google.gson.*;

/**
 * Main class. Used to start the program. Handles all the calls to the webservice.
 * @author Simon Måtegen, Hanna Ringkvist, Sonja Peric
 * @version 1
 */
public class Main
{
    public static void main(String[] args)
    {
        Gson gson = new Gson();
        Storage storage = new Storage();
        Functions func = new Functions(storage);

        //Necessary if this code and the website is run on the same machine. Otherwise it gets stopped by CORS.
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

        //Endpoint to get all of the ledamots. Gets returned as Json.
        get("/api/v1/ledamoter", ((request, response) ->
        {
            func.checkStorage();

            ArrayList<Map> tempList = storage.getLedamoter();

            if (tempList == null)
            {
                response.status(404);
                response.body("404: No ledamots found");
            }
            else
            {
                response.type("application/json");
                response.body(gson.toJson(tempList));
            }

            response.type("application/json");
            response.body(gson.toJson(tempList));

            return response.body();
        }));

        //Endpoint to get a specific ledamot given an id. Gets returned as Json.
        get("/api/v1/ledamoter/:id", ((request, response) ->
        {
            func.checkStorage();

            int chosenID = Integer.parseInt(request.params("id"));

            Map ledamot = storage.getLedamotMapAt(chosenID);

            if (ledamot == null)
            {
                response.status(404);
                response.body("404: No ledamot found with given id");
            }
            else
            {
                response.type("application/json");
                response.body(gson.toJson(ledamot));
            }

            return response.body();
        }));

        //Endpoint to get all of the ledamots in a specific party. Gets returned as Json.
        get("/api/v1/ledamoter/parti/:parti", ((request, response) ->
        {
            func.checkStorage();

            String chosenParty = request.params("parti");

            ArrayList<Map> tempList = storage.getLedamoterByParty(chosenParty);

            if (tempList == null)
            {
                response.status(404);
                response.body("404: No ledamots in the given party");
            }
            else
            {
                response.type("application/json");
                response.body(gson.toJson(tempList));
            }

            return response.body();
        }));

        //Endpoint to get all of the political parties currently in storage. Gets returned as Json.
        get("/api/v1/partier", ((request, response) ->
        {
            func.checkStorage();

            ArrayList<String> tempList = storage.getAvailablePartys();

            if (tempList == null)
            {
                response.status(404);
                response.body("404: No parties found");
            }
            else
            {
                response.type("application/json");
                response.body(gson.toJson(tempList));
            }

            return response.body();
        }));

        //Endpoint to get the link to the Swedish Riksdagen website with more information about a specific ledamot. Gets returned as Json.
        get("/api/v1/link/:id", ((request, response) ->
        {
            func.checkStorage();

            int choosenID = Integer.parseInt(request.params("id"));

            Ledamot tempLedamot = storage.getLedamotAt(choosenID);

            if (tempLedamot == null)
            {
                response.status(404);
                response.body("404: Ledamot with given id not found");
            }
            else
            {
                String link = "https://www.riksdagen.se/sv/ledamoter-partier/ledamot/filler-text_" + tempLedamot.getSourceID();

                response.type("application/json");
                response.body(gson.toJson(link));
            }

            return response.body();
        }));

        //Endpoint to get the three latest tweets for a given ledamot. Gets returned as Json.
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

            return response.body();
        }));

    }
}
