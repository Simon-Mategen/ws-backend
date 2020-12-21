import java.util.HashMap;
import java.util.Map;

public class Tweet
{
    private String text;
    private String author;
    private String url;
    private String date;

    public Tweet(String inText, String inAuthor, String inURL, String intDate )
    {
        this.text = inText;
        this.author = inAuthor;
        this.url = inURL;
        this.date = intDate;
    }

    public Map getAsMap()
    {
        Map map = new HashMap();
        map.put("text", this.text);
        map.put("författare", this.author);
        map.put("url", this.url);
        map.put("datum", this.date);

        return map;
    }
}
