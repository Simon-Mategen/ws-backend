import java.util.HashMap;
import java.util.Map;

/**
 * Class representing the information to be saved about each ledamot read from Riksdagens API.
 * @author Simon Måtegen, Hanna Ringkvist, Sonja Peric
 * @version 1
 */
public class Ledamot
{
    private int id;
    private String name;
    private String party;
    private String pictureURL;
    private String sourceID;

    /**
     * Constructor for ledamot
     * @param inId Id from the order that they are read from the API response
     * @param inName Name of the ledamot
     * @param inParty The political party of the ledamot
     * @param inPictureURL URL link to the picture of the ledamot
     * @param inSourceID The Ledomots sourceID from the API response
     */
    public Ledamot(int inId, String inName, String inParty, String inPictureURL, String inSourceID)
    {
        this.id = inId;
        this.name = inName;
        this.party = inParty;
        this.pictureURL = inPictureURL;
        this.sourceID = inSourceID;
    }

    /**
     * Representing the data of the ladamot as a map for returning as JSON
     * @return The information about a ledamot as a map with keys corresponding to the API specification
     */
    public Map getAsMap()
    {
        Map map = new HashMap();
        map.put("id", this.id);
        map.put("namn", this.name);
        map.put("parti", this.party);
        map.put("bild", this.pictureURL);

        return map;
    }

    /**
     * Used to get the party of the ledamot
     * @return The political part of the ledamot
     */
    public String getParty()
    {
        return party;
    }

    /**
     * Used to get the sourceID for a ledamot
     * @return The sourceID for the ledamot
     */
    public String getSourceID()
    {
        return sourceID;
    }
}
