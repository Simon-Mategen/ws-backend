import java.util.HashMap;
import java.util.Map;

public class Ledamot
{
    private int id;
    private String name;
    private String party;
    private String pictureURL;

    public Ledamot(int inId, String inName, String inParty, String inPictureURL )
    {
        this.id = inId;
        this.name = inName;
        this.party = inParty;
        this.pictureURL = inPictureURL;
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
}
