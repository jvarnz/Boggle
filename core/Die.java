/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author justin
 */
public class Die {

    private final int NUMBER_OF_SIDES = 6;
    ArrayList<String> diceSidesData = new ArrayList<>();
    //Stores data for the sides
    String currentDiceLetter;
    //Stores current letter of each die

    //Uses random number to seed which index to use to obtain letter 
    public void randomLetter() {
        Random random = new Random();
        int seed = random.nextInt(NUMBER_OF_SIDES);
        currentDiceLetter = diceSidesData.get(seed);
    }

    public String getLetter() {
        randomLetter();
        return currentDiceLetter;
    }

    public void addLetter(String dieLetter) {
        diceSidesData.add(dieLetter);
    }

    // Uses enhanced for-loop to output all the letters of the contained in 
    // diceSidesData
    
    /*public void displayAllLetters() {
        for (String letters : diceSidesData) {
            System.out.printf("%s ", letters);
        }

        System.out.println("");
    }*/
}
