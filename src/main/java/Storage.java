import java.util.ArrayList;
import java.util.Date;

public class Storage
{
    private ArrayList<Ledamot> list;
    private Date lastUpdated;

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
    
}
