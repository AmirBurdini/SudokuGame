
public class Cell 
{
    int num;
    boolean [] Posibilities;
    boolean isPlaced;
    Board parent;

    public Cell(Board p)
    {
        num = 0;
        Posibilities = new boolean[] {true,true,true,true,true,true,true,true,true};
        isPlaced = false;
        parent = p;
    }


    public void Fill(int num)
    {
        this.num = num;
        isPlaced = true;
    }
    //inserts a value inside the cell
    
    public void Hole()
    {
        num = 0;
        isPlaced = false;
    }
    //deletes the value inside the cell
    
    public boolean isPossible(int num)
    {
        return Posibilities[num - 1];
    }
    //is num possible in this cell?
    
    public boolean impossible()
    {
        for(int i = 0 ; i < 9 ; i++)
        {
            if(Posibilities[i])
            {
                return false;
            }
        }
        return true;
    }
    //returns true if no number is possible in this cell

}