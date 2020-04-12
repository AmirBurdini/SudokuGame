import java.util.Random;
import javax.swing.*;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.*;
import java.awt.event.*;

public class Board extends JPanel implements KeyListener
{
    Cell[][] board,solution;
    static Game parent;
    int Currx,Curry;
    boolean check;

    public Board(Game g,int NumofHoles)
    {
        Currx = 0; Curry = 0;
        check = false;

        setBackground(Color.black);
        parent = g;

        BorderLayout map = new BorderLayout();
        this.setLayout(map);

        GridLayout grid = new GridLayout(9,9);
        this.setLayout(grid);

        board = new Cell[9][9];
        initBoard(board);
        
        solution = new Cell[9][9];
        initBoard(solution);

        board = newBoard(0, 0, board);
        CopyBoard(board, solution);
        //creates a solution

        board = CreateGame(NumofHoles, board);
        //poke holes in the solution to create game

        Print(board);
        repaint();
    }


    public static void initBoard(Cell[][] board)
    {
        for(int i = 0 ; i < 9 ; i++)
        {
            for(int j = 0 ; j < 9 ; j++)
            {
                board[i][j] = new Cell(parent.gameBoard);
            } 
        }
    }
    //initiate Board

    public static void Print(Cell[][] board)
    {
        System.out.println();
        for(int i = 0 ; i < 9 ; i++)
        {
            for(int j = 0 ; j < 9 ; j++)
            {
                System.out.print("[" + board[i][j].num + "]") ;
            }

            System.out.println() ;
        }
        
    }
    //prints the board

    public static boolean imPossible(Cell[][] board,int x,int y)
    {
        for(int i = 1 ; i < 10 ; i++)
        {
            if(isLegal(x, y, board, i))
            {
                return false;
            }
        }
        return true;
    }
    //returns true if any value valid in board[x][y]

    public static boolean DeadEnd(Cell[][] board)
    {
        for(int i = 1 ; i < 10 ; i++)
        {
            for(int x = 0 ; x < 9 ; x++)
            {
                for(int y = 0 ; y < 9 ; y++)
                {
                    if(isLegal(x, y, board,i))
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    //are there no more possibilities for board[x][y]?

    public static void CopyBoard(Cell[][] board,Cell[][] aux)
    {
        for(int i = 0 ; i < 9 ; i++)
        {
            for(int j = 0 ; j < 9 ; j++)
            {
                aux[i][j].num = board[i][j].num;
                aux[i][j].isPlaced = board[i][j].isPlaced;
                for(int k = 0; k < 9; k++)
                {
                    aux[i][j].Posibilities[k] = board[i][j].Posibilities[k] ;
                }
            }
        }
    }
    //auxilary function

    public static void Dismiss(int x,int y,int num,Cell[][] board)
    {
        for(int i = 0 ; i < 9 ; i++)
        {
            if(i != y)
            {
                board[x][i].Posibilities[num - 1] = false ;
            }
            if(i != x)
            {
                board[i][y].Posibilities[num - 1] = false ;
            }
            
            //dismiss rows and columns
            for(int j = 0 ; j < 9 ; j++)
            {
                if((i/3 == x/3 && j/3 == y/3) && (i != x && j != y))
                {
                    board[i][j].Posibilities[num - 1] = false;
                }
                //dismiss square
            }
        }
    }
    //dismiss Cells options
    
    public static boolean isLegal(int x,int y,Cell [][] board,int num)
    {
        for(int i = 0 ; i < 9 ; i++)
        {
            if(board[x][i].num == num) {return false;}
            if(board[i][y].num == num) {return false;}
            //rows and columns
            for(int j = 0 ; j < 9 ; j++)
            {
                if((i/3 == x/3 && j/3 == y/3) && (i != x && j != y))
                {
                    if(board[i][j].num == num) {return false;}
                }
                //square
            }
        }

        return board[x][y].isPossible(num);
    }
    //checks if there isnt the same number in row.column or square

    public static int RandomInlay(int x,int y,Cell[][] board)
    {
        Random r = new Random();
        int num = r.nextInt(9) + 1;

        if(imPossible(board, x, y))
        {
            return -1;
        }

        while(!isLegal(x, y, board, num))
        {
            num = r.nextInt(9) + 1;
        }

        return num;
    }
    //legal random number generator

    public static Cell[][] newBoard(int x,int y,Cell[][] board)
    {
        if(x == 9) 
        {
            return board;
        }
        //Stop condition

        if(DeadEnd(board))
        {
            return null;
        }
        //Dead End

        Cell[][] aux = new Cell[9][9];
        initBoard(aux);
        initBoard(aux);
        CopyBoard(board, aux);
        //create a new Board for recursion

        int num = RandomInlay(x, y, board);
        if(num == -1)
        {
            return null;
        }
        //also Dead End option

        aux[x][y].Fill(num);
        //in lay random number in the new board

        if(y == 8)
        {
            aux = newBoard(x + 1, 0, aux);
            if(aux == null)
            {
                board[x][y].Posibilities[num - 1] = false;
                return newBoard(x, y,board);
            }
            else board = aux;
        }
        else
        {
            aux = newBoard(x, y + 1, aux);
            if(aux == null)
            {
                board[x][y].Posibilities[num - 1] = false;
                return newBoard(x, y, board);
            }
            else board = aux;
        }
        //DeadEnd - backtracking

        return board;
    }
    //recursive function to create a random board

    public static int findAllSolutions(int x,int y,Cell[][] board)
    {
        if(BoardSolved(board)) 
        {
            return 1;
        }
        //Stop condition

        if(DeadEnd(board))
        {
            return 0;
        }
        //Dead End

        if(board[x][y].isPlaced)
        {
            if(y == 8)
            {
                return findAllSolutions(x + 1, 0, board);
            }
            return findAllSolutions(x, y + 1, board);
        }
        //modification to allow the function to Solve incomplete boards

        Cell[][] aux = new Cell[9][9];
        initBoard(aux);
        CopyBoard(board, aux);
        //create a new Board for recursion

        int res = 0; 
        //number of solutions

        for(int num = 1 ; num < 10 ; num++)
        {
            if(isLegal(x, y, aux, num))
            {
                aux[x][y].Fill(num);

                if(y == 8)
                {
                    res += findAllSolutions(x + 1, 0, aux);
                }
                else res += findAllSolutions(x, y + 1, aux);
            }
            
            
        }
        //try all possible numbers in board[x][y]

        return res;
    }
    //recursive function to find every legal solution

    public static boolean BoardSolved(Cell[][] board)
    {
        for(int i = 0 ; i < 9 ; i++)
        {
            for(int j = 0 ; j < 9 ; j++)
            {
                if(board[i][j].num == 0)
                {
                    return false;
                }
            } 
        }

        return true;
    }
    //checks if the board is already solved

    public static Cell[][] CreateGame(int NumofHoles,Cell[][] board)
    {
        if(NumofHoles == 0)
        {
            return board;
        }
        //finished game board

        Cell[][] aux = new Cell[9][9];
        Board.initBoard(aux);
        Board.CopyBoard(board, aux);

        int x = RandomNum();
        int y = RandomNum();

        aux[x][y].Hole();

        if(Board.findAllSolutions(0, 0, aux) != 1)
        {
            return CreateGame(NumofHoles, board);
        }
        return CreateGame(NumofHoles - 1, aux);
    }
    //poke holes inside the solution board
    
    public static int RandomNum()
    {
        Random r = new Random();
        int num = r.nextInt(9);

        return num;
    }
    //random number generator

    public synchronized void paintComponent(Graphics g)
	{
		super.paintComponent(g);
        drawBoard(g);
        drawLines(g);
        drawCurrent(g);
	}
    //draws the entire panel
    
    public synchronized void drawBoard(Graphics g)
    {
        for(int i = 0 ; i < 9 ; i++)
        {
            for(int j = 0 ; j < 9 ; j++)
            {
                drawCell(g,i,j);
            }
        }
    }
    //draws the cells

    public void drawCell(Graphics g,int i,int j)
    {
        g.setColor(Color.darkGray);
        g.fillOval(i * 60 ,j * 60 ,50,50);

        if(board[j][i].num != 0)
        {
            g.setColor(Color.WHITE);
            g.drawString(board[j][i].num + "",i * 60 + 24,j * 60 + 30);
        }

        if((check)&&(!board[j][i].isPlaced))
        {
            if(board[j][i].num == solution[j][i].num)
            {
                g.setColor(Color.green);
            }
            else if(board[j][i].num != 0) 
            {
                g.setColor(Color.red);
            }
            
            g.drawString(board[j][i].num + "",i * 60 + 24,j * 60 + 30);
        }
    }
    //draws a cell

    public void drawLines(Graphics g)
    {
        g.setColor(Color.DARK_GRAY);

        for(int i = 0 ; i < 3 ; i++)
        {
            g.drawLine(i * 60 * 3 - 5, 0, i * 60 * 3 - 5, 530);
            g.drawLine(0, i * 60 * 3 - 5, 530,i * 60 * 3 - 5);
        }
    }
    //draws the lines between the cells

    public void drawCurrent(Graphics g)
    {
        g.setColor(Color.yellow);
        g.drawRect(Curry * 60 - 5, Currx * 60 - 5, 60, 60);
    }
    //draw a square around the current cell

    public void keyPressed(KeyEvent e) 
    {
        int i = e.getKeyCode();
        
        if((i == KeyEvent.VK_1)&&(!board[Currx][Curry].isPlaced))
        {
            board[Currx][Curry].num = 1;
        }

        if((i == KeyEvent.VK_2)&&(!board[Currx][Curry].isPlaced))
        {
            board[Currx][Curry].num = 2;
        }

        if((i == KeyEvent.VK_3)&&(!board[Currx][Curry].isPlaced))
        {
            board[Currx][Curry].num = 3;
        }

        if((i == KeyEvent.VK_4)&&(!board[Currx][Curry].isPlaced))
        {
            board[Currx][Curry].num = 4;
        }

        if((i == KeyEvent.VK_5)&&(!board[Currx][Curry].isPlaced))
        {
            board[Currx][Curry].num = 5;
        }

        if((i == KeyEvent.VK_6)&&(!board[Currx][Curry].isPlaced))
        {
            board[Currx][Curry].num = 6;
        }

        if((i == KeyEvent.VK_7)&&(!board[Currx][Curry].isPlaced))
        {
            board[Currx][Curry].num = 7;
        }

        if((i == KeyEvent.VK_8)&&(!board[Currx][Curry].isPlaced))
        {
            board[Currx][Curry].num = 8;
        }

        if((i == KeyEvent.VK_9)&&(!board[Currx][Curry].isPlaced))
        {
            board[Currx][Curry].num = 9;
        }

        if(i == KeyEvent.VK_UP)
        {
            if(Curry == 0)
            {
                Curry = 8;
            } 
            else Curry--;
        }

        if(i == KeyEvent.VK_DOWN)
        {
            if(Curry == 8)
            {
                Curry = 0;
            } 
            else Curry++;
        }

        if(i == KeyEvent.VK_LEFT)
        {
            if(Currx == 0)
            {
                Currx = 8;
            } 
            else Currx--;
        }

        if(i == KeyEvent.VK_RIGHT)
        {
            if(Currx == 8)
            {
                Currx = 0;
            } 
            else Currx++;
        }

        if((i == KeyEvent.VK_C)&&(!board[Currx][Curry].isPlaced))
        {
            board[Currx][Curry].num = 0;
        }
        repaint();
    }
    //key listener

    public void keyReleased(KeyEvent arg0) 
    {

    }


    public void keyTyped(KeyEvent arg0) 
    {

    }  

}