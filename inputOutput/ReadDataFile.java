/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inputOutput;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author justin
 */
public class ReadDataFile {

    private Scanner input;
    private String fileName;
    private ArrayList<String> dataFromFile = new ArrayList<String>();

    public ArrayList getData() {
        return dataFromFile;
    }

    public ReadDataFile(String name) {  //ReadDataFile Constructor
        fileName = name;
    }    
    /**
     * Populate data creates a URL (Uniform Resource Locator using 
     * the getResource method, which finds the resource relative to the package 
     * File changes the URL into an abstract path
     */ 
    public void populateData() {

        try {       
            URL url = getClass().getResource(fileName);            
            File file = new File(url.toURI()); 
            input = new Scanner(file);
            
            //Scans in each line from text to dataFromFile String Array
            while (input.hasNext()) {   
                dataFromFile.add(input.nextLine());
            }
            
        } catch (URISyntaxException use) {
            System.out.println("URISyntaxException Occured");
            use.printStackTrace();
        } catch (FileNotFoundException fnf) {
            System.out.println("File Not Found");
            fnf.printStackTrace();
        } finally {
            input.close();
        }
    }
    
    
    
}
