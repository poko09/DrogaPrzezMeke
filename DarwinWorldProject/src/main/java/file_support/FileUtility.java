package file_support;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileUtility {

    private String filePath;

    public FileUtility(String filePath) {
        this.filePath = filePath;
    }

    public  Map<String, Integer> getHashMapFromTextFile(){

        Map<String, Integer> mapFileContents = new HashMap<String, Integer>();
        BufferedReader br = null;

        try{

            File file = new File(filePath);

            br = new BufferedReader( new FileReader(file) );

            String line = null;

            while ( (line = br.readLine()) != null ){

                String[] parts = line.split(":");

                String name = parts[0].trim();
                Integer age = Integer.parseInt( parts[1].trim() );

                if( !name.equals("") && !age.equals("") )
                    mapFileContents.put(name, age);
            }

        }catch(Exception e){
            e.printStackTrace();
        }finally{


            if(br != null){
                try {
                    br.close();
                }catch(Exception e){};
            }
        }

        return mapFileContents;

    }

}
