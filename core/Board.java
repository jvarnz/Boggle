/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author justin
 */
public class Board {

    private final int NUMBER_OF_DICE = 16;
    private final int NUMBER_OF_SIDES = 6;
    
    ArrayList<String> diceData;  //stores dice data
    ArrayList<Die> diceObjects = new ArrayList<Die>();// stores 16 dice objects

    public Board(ArrayList<String> diceDataIn) {
        diceData = diceDataIn;  //transfers the diceDataIn into the members 
    }                           // diceData arrayList         

    /**
     * populateDice method takes the existing ArrayList<String> diceData and
     * assigns each letter from the current list index onto the sides of the
     * die. White space is removed using String class method replaceAll. In
     * order to account for the extra character in "Qu", an if else statement
     * used to add the two letters together as one letter side.
     */
    public void populateDice() {
        Die die;

        for (int i = 0; i < NUMBER_OF_DICE; i++) {
            die = new Die();
            String data = diceData.get(i);
            data = data.replaceAll(" ", ""); //removes all blank spaces

            for (int j = 0; j < NUMBER_OF_SIDES; j++) {
                String letter = Character.toString(data.charAt(j));

                if (j == 5 && data.length() > 6) {
                    letter = letter + Character.toString(data.charAt(j + 1));
                    die.addLetter(letter);
                } else {
                    die.addLetter(letter);
                }
            }
            
            diceObjects.add(die); //Add newly populated die in to the obj array
        }
    }

    /**
     * Loop through the populated dice and print their current letter. Uses the
     * modulo operator to print out 4x4 output
     *
     * @return
     */
    public ArrayList<Die> shakeDice() {
        populateDice();
        
        for(int i = 0; i < diceObjects.size(); i++){
            diceObjects.get(i).getLetter();
        }
        
        
        return diceObjects;
    }
}
