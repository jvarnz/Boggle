/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boggle;


import core.Board;
import inputOutput.ReadDataFile;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import userInterface.BoggleUI;

/**
 *
 * @author justin
 */
public class Boggle{

    private static ArrayList<String> data = new ArrayList<String>();
    private static String fileName = new String("BoggleData.txt");

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //Creates an ReadDataFile instance using the filename as arguement  
        ReadDataFile readDataFile = new ReadDataFile(fileName);
        
        //Uses readDatafile method populateData to scan text file
        readDataFile.populateData();
        
        //Adds the text file data to the data ArrayList with getData method
        data = readDataFile.getData();
        
        //Generates a new Board instance with the ArrayList
        Board board = new Board(data);
        
        //Populates the sides if each dice then shakes them to create 4x4 output
        //board.shakeDice();
        
        Boggle b = new Boggle();
        ArrayList<String> wordList = new ArrayList<>(b.dictionaryFileReader());
        
        BoggleUI boggleUI = new BoggleUI(board, wordList);
        
//        for(String s : wordList)
//            System.out.println(s);
    }
    public ArrayList<String> dictionaryFileReader(){
        ArrayList<String> dictionaryWords = new ArrayList<>();
        BufferedReader br = null;
        
        try{
            
            URL url = getClass().getResource("TemporaryDictionary.txt");
            File file = new File(url.toURI());
            
            br = new BufferedReader(new FileReader(file));            
            String currentLine;
            
            while((currentLine = br.readLine()) != null){
                if (currentLine.length() > 2)
                    dictionaryWords.add(currentLine);
                        
            }               
        } catch (FileNotFoundException fnfex) {
           fnfex.printStackTrace();
           
        } catch (IOException ex) {
            Logger.getLogger(Boggle.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        } catch (URISyntaxException ex) {
            Logger.getLogger(Boggle.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(Boggle.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
               
        
        return dictionaryWords;
    }
}
