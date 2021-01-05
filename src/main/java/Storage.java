import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class Storage
{
    private ArrayList<Ledamot> list;
    private LocalDate date;
    private LocalTime time;

    public Storage()
    {
    }

    public void addList(ArrayList<Ledamot> inList)
    {
        this.list = inList;
    }

    public ArrayList<Ledamot> getList()
    {
        return list;
    }

    public void setLastUpdated(LocalDate inDate, LocalTime inTime)
    {
        date = inDate;
        time = inTime;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public LocalTime getTime()
    {
        return time;
    }

    public Ledamot getLedamotAt(int index)
    {
        return list.get(index);
    }

    public Map getLedamotMapAt(int index)
    {
        return list.get(index).getAsMap();
    }

    public int getSize()
    {
        return  list.size();
    }

    public ArrayList<Map> getLedamoter()
    {
        ArrayList<Map> temp = new ArrayList<>();

        for (Ledamot ledamot : this.getList())
        {
            temp.add(ledamot.getAsMap());
        }

        return temp;
    }

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
