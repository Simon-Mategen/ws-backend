import java.util.HashMap;
import java.util.Map;

/**
 * Class representing the information to be saved about each tweet read from the Twitter API.
 * @author Simon Måtegen, Hanna Ringkvist, Sonja Peric
 * @version 1
 */
public class Tweet
{
    private String text;
    private String author;
    private String url;
    private String date;

    /**
     * Constructor for Tweet
     * @param inText Text of the tweet
     * @param inAuthor Author of the tweet
     * @param inURL Url to the tweet on twitters website
     * @param intDate Date of when the tweet was posted
     */
    public Tweet(String inText, String inAuthor, String inURL, String intDate )
    {
        this.text = inText;
        this.author = inAuthor;
        this.url = inURL;
        this.date = intDate;
    }

    /**
     * Representing the data of a tweet as a map for returning as JSON
     * @return The information about a tweet as a map with keys corresponding to the API specification
     */
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
