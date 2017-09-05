/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userInterface;

import core.Board;
import core.Die;
import java.awt.BorderLayout;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.LINE_END;
import static java.awt.BorderLayout.PAGE_END;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.Timer;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author justin
 */
public class BoggleUI {

    private JFrame frame;
    private JMenuBar menuBar;
    private JMenu boggleMenu;
    private JMenuItem newGame;
    private JMenuItem exit;
    private JPanel buttonsPanel;
    private JPanel infoPanel;
    private JPanel southPanel;
    private JScrollPane scrollPane;
    private JLabel timeLabel;
    private JLabel currentWordLabel;
    private JLabel currentScoreLabel;
    private BorderLayout layout;
    private JTextArea textArea;
    private JButton shakeDice;
    private JButton submitWordButton;

    private ArrayList<Die> diceObjects;
    private String currentWord = "";
    private int currentScore = 0;
    private Board gameBoard;
    private ArrayList<String> wordList = new ArrayList<>();
    private JButton[][] buttonBoard = new JButton[4][4];
    private Timer timer;
    private ArrayList<String> usedWords = new ArrayList<>();

    final private long THREE_MINUTES = 180000;
    private long time = THREE_MINUTES;

    public BoggleUI(Board board, ArrayList<String> dictionaryList) {

        gameBoard = board;
        diceObjects = board.shakeDice();
        wordList = dictionaryList;
        initComponents();
    }

    private void initComponents() {

        // Setting the frame
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Boggle");
        //frame.pack();
        frame.setSize(900, 600);
        layout = new BorderLayout();
        frame.setLayout(layout);

        // Setting the menu bar and adding two items 
        menuBar = new JMenuBar();
        boggleMenu = new JMenu("Boggle");
        boggleMenu.setMnemonic('B');
        newGame = new JMenuItem("New Game");
        newGame.setMnemonic('N');
        boggleMenu.add(newGame);
        exit = new JMenuItem("Exit");
        exit.setMnemonic('E');
        boggleMenu.add(exit);
        menuBar.add(boggleMenu);
        frame.setJMenuBar(menuBar);

        // Creates letter button panel
        buttonsPanel = new JPanel();
        buttonsPanel.setSize(new Dimension(100, 100));
        buttonsPanel.setMaximumSize(new Dimension(100, 100));
        buttonsPanel.setPreferredSize(buttonsPanel.getPreferredSize());
        buttonsPanel.setBorder(BorderFactory.createTitledBorder("Boggle Board"));
        buttonsPanel.setLayout(new GridLayout(4, 4));

        // Assigns the letters for each of the jbuttons
        setNewBoard();

        // Creates right side panel for info
        infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setSize(new Dimension(300, 300));

        // Creates and adds JTextArea JScrollPane, label, button
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(50, 50));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Enter Words Found"));
        infoPanel.add(scrollPane);
        
        timeLabel = new JLabel("    3:00    ");
        timeLabel.setBorder(BorderFactory.createTitledBorder("Time Left"));
        timeLabel.setFont(new Font("Serif", Font.PLAIN, 70));
        timeLabel.setMaximumSize(timeLabel.getPreferredSize());
        

        infoPanel.add(timeLabel);
        shakeDice = new JButton("Shake Dice");
        shakeDice.setFont(new Font("Arial", Font.PLAIN, 20));
        shakeDice.setPreferredSize(new Dimension(0, 100));
        infoPanel.add(shakeDice);

        frame.add(infoPanel, LINE_END);

        //-----------------------------------------------------------------
        //--------------------------------------------Assignment 3 sections
        southPanel = new JPanel();
        FlowLayout flow = new FlowLayout();
        flow.setHgap(50);
        southPanel.setLayout(flow);

        frame.add(southPanel, PAGE_END);

        currentWordLabel = new JLabel("                   ");
        currentWordLabel.setFont(new Font("Arial", Font.PLAIN, 60));
        currentWordLabel.setBorder(BorderFactory.createTitledBorder("Current Word"));;
        currentWordLabel.setPreferredSize(currentWordLabel.getPreferredSize());
        currentWordLabel.setMinimumSize(currentWordLabel.getPreferredSize());

        southPanel.add(currentWordLabel);

        submitWordButton = new JButton(" Submit Word ");
        submitWordButton.setFont(new Font("Arial", Font.PLAIN, 20));
        southPanel.add(submitWordButton);

        currentScoreLabel = new JLabel("    " + currentScore + "    ");
        currentScoreLabel.setFont(new Font("Arial", Font.PLAIN, 40));
        currentScoreLabel.setBorder(BorderFactory.createTitledBorder("Current Score"));
        currentScoreLabel.setSize(currentScoreLabel.getPreferredSize());
        currentScoreLabel.setPreferredSize(currentScoreLabel.getPreferredSize());
        currentScoreLabel.setMinimumSize(currentScoreLabel.getPreferredSize());
        southPanel.add(currentScoreLabel);

        exit.addActionListener(new ExitListener());
        textArea.setEditable(false);

        newGame.addActionListener(new NewGameListener());
        shakeDice.addActionListener(new ShakeDiceListener());
        submitWordButton.addActionListener(new SubmitWordListener());

        frame.setVisible(true);

    }

    //Method that creates new game board of Jbuttons that are shuffled and enabled 
    public void setNewBoard() {
        buttonsPanel.removeAll();

        diceObjects = gameBoard.shakeDice();
        long seed = System.nanoTime();
        Collections.shuffle(diceObjects, new Random(seed));

        int count = 0;
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                buttonBoard[row][col] = new JButton();
                buttonBoard[row][col].putClientProperty("row", row);
                buttonBoard[row][col].putClientProperty("col", col);
                buttonBoard[row][col].addActionListener(new BoardListener());
                buttonBoard[row][col].setText(diceObjects.get(count + col).getLetter());
                buttonBoard[row][col].setFont(new Font("Serif", Font.BOLD, 30));
                buttonsPanel.add(buttonBoard[row][col]);
            }
            count += 4;
        }
        frame.add(buttonsPanel, CENTER);
        usedWords.removeAll(usedWords);
        buttonsPanel.revalidate();
        buttonsPanel.repaint();

    }

    // Creates Action Listener for board JButtons
    public class BoardListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            JButton button = (JButton) e.getSource();
            int rowClick = (int) button.getClientProperty("row");
            int colClick = (int) button.getClientProperty("col");

            buttonBoard[rowClick][colClick].setBackground(Color.red);
            currentWord += button.getText();
            currentWordLabel.setText(currentWord);
            ArrayList<JButton> toBeDisabled = new ArrayList<>();

            for (int row = 0; row < 4; row++) {
                for (int col = 0; col < 4; col++) {
                    toBeDisabled.add(buttonBoard[row][col]);
                }
            }

            // Determine available board moves based on first button clicked
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if ((rowClick + dx) >= 0) {
                        if ((colClick + dy) >= 0) {
                            if ((rowClick + dx) < 4) {
                                if ((colClick + dy) < 4) { //shows available buttons 3x3 grid
                                    if (buttonBoard[rowClick + dx][colClick + dy].getBackground() != Color.red) {

                                        buttonBoard[rowClick + dx][colClick + dy].setBackground(Color.YELLOW);
                                        buttonBoard[rowClick + dx][colClick + dy].setEnabled(true);
                                    }
                                    //buttonBoard[rowClick + dx][colClick + dy].setEnabled(true);
                                    toBeDisabled.remove(buttonBoard[rowClick + dx][colClick + dy]);

                                }
                            }
                        }
                    }
                }
            }
            buttonBoard[rowClick][colClick].setBackground(Color.red);
            buttonBoard[rowClick][colClick].setEnabled(false);

            //Sets all buttons not in the available 3x3 tp false
            for (int i = 0; i < toBeDisabled.size(); i++) {
                if (toBeDisabled.get(i).getBackground() != Color.red) {
                    toBeDisabled.get(i).setEnabled(false);
                    toBeDisabled.get(i).setBackground(new JButton().getBackground());
                }
            }
        }
    }

    //Submits created word to check if used and adds to jtextArea
    private class SubmitWordListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < wordList.size(); i++) {
                if (wordList.get(i).toUpperCase().equals(currentWord.toUpperCase())) {
                    if (!(usedWords.contains(currentWord))) {
                        usedWords.add(currentWord);
                        textArea.append(currentWord + "\n");

                        switch (currentWord.length()) {
                            case 3:
                                currentScore += 1;
                                break;
                            case 4:
                                currentScore += 1;
                                break;
                            case 5:
                                currentScore += 2;
                                break;
                            case 6:
                                currentScore += 3;
                                break;
                            case 7:
                                currentScore += 5;
                                break;
                            case 8:
                                currentScore += 11;
                                break;
                        }
                        currentScoreLabel.setText("   " + currentScore + "   ");
                    } else if(usedWords.contains(currentWord)) {
                        textArea.append("You already submitted the word " + currentWord + "\n");
                    }
                }
            }
            currentWord = "";
            currentWordLabel.setText("" + currentWord);

            for (int row = 0; row < 4; row++) {
                for (int col = 0; col < 4; col++) {
                    buttonBoard[row][col].setBackground(new JButton().getBackground());
                    buttonBoard[row][col].setEnabled(true);
                }
            }
        }
    }

    private class ShakeDiceListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            setNewBoard();
            currentScoreLabel.setText("   " + currentScore + "  ");
            currentWordLabel.setText("");
            currentWord = "";
            textArea.setText("");
            submitWordButton.setEnabled(true);

            //Creates countdown timer for label
            timer = new Timer(1000, new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (time > 0) {
                        long s = ((time / 1000) % 60);
                        long m = (((time / 1000) / 60) % 60);

                        if (s < 10) {
                            timeLabel.setText("    " + m + ":0" + s + "    ");
                        } else {
                            timeLabel.setText("    " + m + ":" + s + "    ");
                        }
                        time -= 1000;

                        // Handles events after time is up
                        if (time == 0) {
                            
                            timeLabel.setText("Time up!");
                            timer.stop();
                            currentWord = "";
                            currentWordLabel.setText("");
                            submitWordButton.setEnabled(false);
                            JOptionPane.showMessageDialog(null, "The Computer is comparing it's words with your words, lets see the results... ");
                            
                            //Selects random found words for the computer to claim
                            if (usedWords.size() > 0) {
                                Random random = new Random();
                                int seed = usedWords.size();

                                int numberComputerFound = random.nextInt(seed) + 1;

                                ArrayList<String> computersWords = new ArrayList<>();
                                ArrayList<String> jTextAreaList = new ArrayList<>();
                                String jTextArea = textArea.getText();
                                
                                //copies jtextarea to add to jtextpane later
                                for (String word : jTextArea.split("\n")) {
                                    jTextAreaList.add(word);
                                }
                                
                                //Collects random words and assigns them to arraylist
                                String output = new String();
                                for (int i = 0; i < numberComputerFound; i++) {
                                    output = usedWords.get(random.nextInt(seed));
                                    
                                    if( !(computersWords.contains(output))) {
                                        computersWords.add(output);
                                    }
                                    
                                }
                                
                                JOptionPane.showMessageDialog(null, "The Computer found words: " + computersWords);
                                
                                //creates jtextpane to replace jtextarea
                                JTextPane endGameText = new JTextPane();
                                endGameText.setSize(scrollPane.getSize());
                                endGameText.setPreferredSize(scrollPane.getPreferredSize());
                                endGameText.setMinimumSize(scrollPane.getMinimumSize());

                                
                                endGameText.setEditable(false);
                                
                                //Uses styledDocument to implement strikthrough on the words the computer found
                                StyledDocument styleDocument = endGameText.getStyledDocument();
                                Style primaryStyle = styleDocument.addStyle("Primary", null);
                                Style secondStyle = styleDocument.addStyle("Second", null);

                                StyleConstants.setStrikeThrough(secondStyle, true);
                                boolean wordUsed;

                                for (int i = 0; i < jTextAreaList.size(); i++) {
                                    wordUsed = false;
                                    for (int j = 0; j < computersWords.size(); j++) {
                                        
                                        if (jTextAreaList.get(i).equals(computersWords.get(j))) {
                                            try {
                                                styleDocument.insertString(styleDocument.getLength(), jTextAreaList.get(i) + "\n", secondStyle);
                                            } catch (BadLocationException ex) {
                                                Logger.getLogger(BoggleUI.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                            wordUsed = true;
                                            switch (computersWords.get(j).length()) {
                                                case 3:
                                                    currentScore -= 1;
                                                    break;
                                                case 4:
                                                    currentScore -= 1;
                                                    break;
                                                case 5:
                                                    currentScore -= 2;
                                                    break;
                                                case 6:
                                                    currentScore -= 3;
                                                    break;
                                                case 7:
                                                    currentScore -= 5;
                                                    break;
                                                case 8:
                                                    currentScore -= 11;
                                                    break;
                                            }
                                            currentScoreLabel.setText("   " + currentScore + "   ");

                                        }

                                    }
                                    if (wordUsed == false) {
                                        try {
                                            styleDocument.insertString(styleDocument.getLength(), jTextAreaList.get(i) + "\n", primaryStyle);
                                        } catch (BadLocationException ex) {
                                            Logger.getLogger(BoggleUI.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                }
                                
                                //endGameText.setBorder(BorderFactory.createTitledBorder("Enter Words Found"));
                                
                                JScrollPane newJScroll = new JScrollPane(endGameText);
                                
                                newJScroll.setPreferredSize(new Dimension(50, 50));
                                newJScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                                newJScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                                newJScroll.setBorder(BorderFactory.createTitledBorder("Enter Words Found"));
                                infoPanel.remove(scrollPane);
                                infoPanel.add(newJScroll, 0);
                                infoPanel.validate();
                                infoPanel.repaint();
                                                                
                                for (int row = 0; row < 4; row++) {
                                    for (int col = 0; col < 4; col++) {
                                        buttonBoard[row][col].setEnabled(false);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            );
            timer.start();

            shakeDice.setEnabled(false);

        }
    }

    private class NewGameListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            textArea.setText(" ");
            //reset jlabel
            shakeDice.setEnabled(true);
            setNewBoard();

            try {
                timer.stop();
            } catch (NullPointerException npe) {
            }

            time = THREE_MINUTES;
            timeLabel.setText("    3:00    ");
            currentScore = 0;
            currentScoreLabel.setText("   " + currentScore + "  ");
        }
    }

    private class ExitListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            int response = JOptionPane.showConfirmDialog(null, "Confirm?",
                    "Exit?", JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
    }
}
