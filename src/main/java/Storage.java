import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;

/**
 * Class for storing ledamots for caching
 * @author Simon Måtegen, Hanna Ringkvist, Sonja Peric
 * @version 1
 */
public class Storage
{
    private ArrayList<Ledamot> list;
    private LocalDate date;
    private LocalTime time;

    /**
     * Adds a list of ledamots to be stored
     * @param inList List of ledamots object
     */
    public void addList(ArrayList<Ledamot> inList)
    {
        this.list = inList;
    }

    /**
     * Get a list of the currently stored ledamots
     * @return List of ledamots object
     */
    public ArrayList<Ledamot> getList()
    {
        return list;
    }

    /**
     * Set when the the list of ledamots was last updated
     * @param inDate Localdate object
     * @param inTime Localtime object
     */
    public void setLastUpdated(LocalDate inDate, LocalTime inTime)
    {
        date = inDate;
        time = inTime;
    }

    /**
     * Gets the date of when the list of ledamots was last updated
     * @return Stored date
     */
    public LocalDate getDate()
    {
        return date;
    }

    /**
     * Gets the time of when the list of ledamots was last updated
     * @return Stored date
     */
    public LocalTime getTime()
    {
        return time;
    }

    /**
     * Gets the specific ledamot object at the specified index
     * @param index Id of the ledamot
     * @return Ledamot object
     */
    public Ledamot getLedamotAt(int index)
    {
        return list.get(index);
    }

    /**
     * Gets the map of the information of a specific ledamot at the specified index
     * @param index Id of the ledamot
     * @return Map of ledamot object
     */
    public Map getLedamotMapAt(int index)
    {
        return list.get(index).getAsMap();
    }

    /**
     * Gets the number of stored objects
     * @return Number of stored objects
     */
    public int getSize()
    {
        return  list.size();
    }

    /**
     * Returns a list of maps of information on all ledamots
     * @return A list of maps
     */
    public ArrayList<Map> getLedamoter()
    {
        ArrayList<Map> temp = new ArrayList<>();

        for (Ledamot ledamot : this.getList())
        {
            temp.add(ledamot.getAsMap());
        }

        return temp;
    }

    /**
     * Returns a list of maps of information on all ledamots in the chosen party
     * @param party Political party
     * @return A list of maps
     */
    public ArrayList<Map> getLedamoterByParty(String party)
    {
        ArrayList<Map> tempList = new ArrayList<>();

        for (int i = 0; i < this.getSize(); i++)
        {
            Ledamot tempLedamot = this.getLedamotAt(i);

            if (tempLedamot.getParty().equals(party))
            {
                tempList.add(tempLedamot.getAsMap());
            }
        }

        return tempList;
    }

    /**
     * Returns a list of the parties of the ledamots in the list
     * @return List of partys
     */
    public ArrayList<String> getAvailablePartys()
    {
        ArrayList<String> partys = new ArrayList<>();

        for (int i = 0; i < this.getSize(); i++)
        {
            Ledamot tempLedamot = this.getLedamotAt(i);

            if (!partys.contains(tempLedamot.getParty()) && !tempLedamot.getParty().equals("-"))
            {
                partys.add(tempLedamot.getParty());
            }
        }

        return partys;
    }


}
