import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class Menu 
{
    int difficulty;
    Game game;
    
    public static void main(String[] args) 
    {
        Menu m = new Menu();
    }

    public Menu()
    {
        difficulty = 0;
        JFrame mainFrame = new JFrame();
        JPanel options = new JPanel();
        JPanel radio = new JPanel();
        
        JButton newGame = new JButton("start Game");
        JRadioButton easy,medium,hard,insane;
        ButtonGroup difficultyOptions = new ButtonGroup();

        easy = new JRadioButton("easy"); difficultyOptions.add(easy);
        medium = new JRadioButton("medium"); difficultyOptions.add(medium);
        hard = new JRadioButton("hard"); difficultyOptions.add(hard);
        insane = new JRadioButton("insane"); difficultyOptions.add(insane);

        easy.setActionCommand("1");
        medium.setActionCommand("2");
        hard.setActionCommand("3");
        insane.setActionCommand("4");

        FlowLayout f = new FlowLayout();
        radio.setLayout(f);
        radio.add(easy);
        radio.add(medium);
        radio.add(hard);
        radio.add(insane);

        options.setLayout(f);
        options.add(radio);
        options.add(newGame);

        newGame.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                if(easy.isSelected())
                {
                    difficulty = 1;
                }

                if(medium.isSelected())
                {
                    difficulty = 2;
                }

                if(hard.isSelected())
                {
                    difficulty = 3;
                }
                if(insane.isSelected())
                {
                    difficulty = 4;
                }
                game = new Game(difficulty);
            }
        });
        JLabel headline = new JLabel("Sudoku by Amir Burdini");
        headline.setSize(10,20);
        headline.setAlignmentY(JFrame.CENTER_ALIGNMENT);

        mainFrame.add(options,BorderLayout.SOUTH);
        mainFrame.add(headline,BorderLayout.NORTH);
        mainFrame.setVisible(true);
        mainFrame.setLocation(450,10);
        mainFrame.setSize(420,100);
        mainFrame.setResizable(false);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}