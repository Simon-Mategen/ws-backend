import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

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

    public int getSize()
    {
        return  list.size();
    }

}
