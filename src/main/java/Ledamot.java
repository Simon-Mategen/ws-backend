import java.util.HashMap;
import java.util.Map;

public class Ledamot
{
    private int id;
    private String name;
    private String party;
    private String pictureURL;
    private String sourceID;

    public Ledamot(int inId, String inName, String inParty, String inPictureURL, String inSourceID)
    {
        this.id = inId;
        this.name = inName;
        this.party = inParty;
        this.pictureURL = inPictureURL;
        this.sourceID = inSourceID;
    }

    public Map getAsMap()
    {
        Map map = new HashMap();
        map.put("id", this.id);
        map.put("namn", this.name);
        map.put("parti", this.party);
        map.put("bild", this.pictureURL);

        return map;
    }

    public String getParty()
    {
        return party;
    }

    public String getSourceID()
    {
        return sourceID;
    }
}
