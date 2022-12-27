package org.example;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

//FileUtill --> mozna tak klase nazwac
public class FileUtility {

    final static String filePath = "parametry.txt";


    public void run() {

        //read text file to HashMap
        Map<String, Integer> mapFromFile = getHashMapFromTextFile();

        //iterate over HashMap entries
        for(Map.Entry<String, Integer> entry : mapFromFile.entrySet()){
            System.out.println(entry.getKey()  +" " + entry.getValue() );
        }

    }

    public  Map<String, Integer> getHashMapFromTextFile(){

        Map<String, Integer> mapFileContents = new HashMap<String, Integer>();
        BufferedReader br = null;

        try{

            //create file object
            File file = new File(filePath);

            //create BufferedReader object from the File  // try with --> obsluzyc bledy
            br = new BufferedReader( new FileReader(file) );

            String line = null;

            //read file line by line
            while ( (line = br.readLine()) != null ){

                //split the line by :
                String[] parts = line.split(":");

                //first part is name, second is age
                String name = parts[0].trim();
                Integer age = Integer.parseInt( parts[1].trim() );

                //put name, age in HashMap if they are not empty
                if( !name.equals("") && !age.equals("") )
                    mapFileContents.put(name, age);
            }

        }catch(Exception e){
            e.printStackTrace();
        }finally{

            //Always close the BufferedReader
            if(br != null){
                try {
                    br.close();
                }catch(Exception e){};
            }
        }

        return mapFileContents;

    }



    //Methods to save statistics to file
    public void createFile() {
        try {
            File myObj = new File("filename.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error with creating file occurred.");
            e.printStackTrace();
        }
    }
    public void writeToFIle(String text) {
        try {
            FileWriter myWriter = new FileWriter("filename.txt");
            myWriter.write(text);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error with writing to file occurred.");
            e.printStackTrace();
        }
    }

}
