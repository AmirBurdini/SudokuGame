import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu {
    
    Game game;

    public Menu() {

        JFrame mainFrame = new JFrame();
        JPanel options = new JPanel();
        JPanel radio = new JPanel();

        JButton newGame = new JButton("start Game");
        JRadioButton easy, medium, hard, insane, help;
        ButtonGroup difficultyOptions = new ButtonGroup();

        easy = new JRadioButton("easy");
        difficultyOptions.add(easy);
        
        medium = new JRadioButton("medium");
        difficultyOptions.add(medium);
        
        hard = new JRadioButton("hard");
        difficultyOptions.add(hard);
        
        insane = new JRadioButton("insane");
        difficultyOptions.add(insane);
        
        help = new JRadioButton("help");
        difficultyOptions.add(help);

        FlowLayout f = new FlowLayout();
        radio.setLayout(f);
        radio.add(easy);
        radio.add(medium);
        radio.add(hard);
        radio.add(insane);
        radio.add(help);

        options.setLayout(f);
        options.add(radio);
        options.add(newGame);

        newGame.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {

                DEF.DIFFICULTY gameLevel = DEF.DIFFICULTY.EASY;
                if (help.isSelected()) {

                    JFrame help = new JFrame("help");
                    help.add(new JLabel("Welcome to Sudoku by Amir Burdini!! \n  \n 1-9 for filling numbers \n arrows for moving \n C for value deletion \n enjoy!"));
                    help.setVisible(true);
                    help.setLocation(450, 100);
                    help.setSize(500, 150);
                    help.setResizable(false);
                } else {

                    if (easy.isSelected()) {

                        gameLevel = DEF.DIFFICULTY.EASY;
                    }

                    if (medium.isSelected()) {

                        gameLevel = DEF.DIFFICULTY.MEDIUM;
                    }

                    if (hard.isSelected()) {

                        gameLevel = DEF.DIFFICULTY.HARD;
                    }

                    if (insane.isSelected()) {

                        gameLevel = DEF.DIFFICULTY.INSANE;
                    }
                    
                    game = new Game(gameLevel);
                }
            }
        });
        JLabel headline = new JLabel("Sudoku by Amir Burdini");
        headline.setSize(10, 20);
        headline.setAlignmentY(JFrame.CENTER_ALIGNMENT);

        mainFrame.add(options, BorderLayout.SOUTH);
        mainFrame.add(headline, BorderLayout.NORTH);
        mainFrame.setVisible(true);
        mainFrame.setLocation(450, 10);
        mainFrame.setSize(460, 100);
        mainFrame.setResizable(false);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}
