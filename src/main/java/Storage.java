import java.util.ArrayList;

public class Storage
{
    private ArrayList<Ledamot> list;

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
