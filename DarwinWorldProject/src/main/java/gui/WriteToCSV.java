package gui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriteToCSV {

    String fileName;
    File newFile;
    String[] nameOfColumns ={"Day of simulation", "Number of animals", "Number of plants", "Number of free fields",
            "Most popular genotype", "Average energy", "Average life length"};;


    public WriteToCSV(String name) throws IOException {
        this.fileName=name;
        String newFileName = fileName + ".csv";
        this.newFile = new File(newFileName);
        this.appendDataToAFile(nameOfColumns, false);

    }

    public String toString() {
        return newFile.getPath();
    }




    public String convertToCSV(String[] data) {
        return String.join(", ", data);
    }


    public void appendDataToAFile(String[] data, boolean appendToAFile) throws IOException {

        //deleteLastLane();
        //updateValues(data);
        FileWriter writer = new FileWriter(newFile,appendToAFile);
        writer.write(convertToCSV(data));
        writer.write("\n");


        writer.close();
    }
}

