import java.util.ArrayList;

public class AutoSolver extends Thread
{
    static ArrayList<Cell[][]> steps ;
    static ArrayList<Integer> Curr;
	Game parent;

	public AutoSolver(Board b,Game parent)
	{
        steps = new ArrayList<Cell[][]>();
        Curr = new ArrayList<Integer>();
		findSolution(0, 0, b.board);
		this.parent = parent;
	}

    public synchronized void run()
	{	
		int i = 0,j = 0;
		while(i < steps.size())
		{
            parent.gameBoard.board = steps.get(i);
            parent.gameBoard.Currx = Curr.get(j);
            j++;
            parent.gameBoard.Curry = Curr.get(j);
			parent.repaint();
            i++;
            j++;
			try { sleep(300);}	
			catch (InterruptedException e) { e.printStackTrace();}		
		}	
	}
	//the thread to print all the steps

	public static Cell[][] findSolution(int x,int y,Cell[][] board)
	{
        steps.add(board);
        Curr.add(x); Curr.add(y);

        if(Board.BoardSolved(board)) 
        {
            return board;
        }
        //Stop condition

        if(Board.DeadEnd(board))
        {
            return null;
        }
        //Dead End

		if(board[x][y].isPlaced)
        {
            if(y == 8)
            {
                return findSolution(x + 1, 0, board);
            }
            return findSolution(x, y + 1, board);
        }
        //modification to allow the function to Solve incomplete boards

        Cell[][] aux = new Cell[9][9];
        Board.initBoard(aux);
        Board.initBoard(aux);
        Board.CopyBoard(board, aux);
        //create a new Board for recursion

        int num = Board.RandomInlay(x, y, board);
        if(num == -1)
        {
            return null;
        }
        //also Dead End option

        aux[x][y].Fill(num);
        //in lay random number in the new board

        if(y == 8)
        {
            aux = findSolution(x + 1, 0, aux);
            if(aux == null)
            {
                board[x][y].Posibilities[num - 1] = false;
                return findSolution(x, y,board);
            }
            else board = aux;
        }
        else
        {
            aux = findSolution(x, y + 1, aux);
            if(aux == null)
            {
                board[x][y].Posibilities[num - 1] = false;
                return findSolution(x, y, board);
            }
            else board = aux;
        }
        //DeadEnd - backtracking

        return board;
    }
	//modification to the original newBoard function 
}