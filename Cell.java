import java.util.Arrays;

public class Cell 
{
    boolean [] posibilities; // if the cell is empty, what numbers could be in that cell
    boolean isPlaced; // is this cell empty?
    int num; // the number the cell contains
    
    // coordinates
    int x;
    int y;
    
    // constructor, empty Cell
    public Cell(int x, int y){
        
        num = 0; 
        posibilities = new boolean[9];
        Arrays.fill(posibilities, true);
        isPlaced = false; 
        
        this.x = x;
        this.y = y;
    }

    // inserts a value inside the cell
    public void fill(int num){

        this.num = num;
        isPlaced = true;
    }
    
    // deletes the value inside the cell and makes it empty again
    public void hole(){

        num = 0;
        isPlaced = false;
    }
    
    // is num possible in this cell?
    public boolean isPossible(int num){
        
        return posibilities[num - 1];
    }
    
    // returns true if no number is possible in this cell
    public boolean impossible(){

        for(int i = 0 ; i < 9 ; i++)
        {
            if(posibilities[i])
            {
                return false;
            }
        }
        return true;
    }
    
}