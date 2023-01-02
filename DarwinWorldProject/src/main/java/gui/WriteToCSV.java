package gui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WriteToCSV {

    String fileName;
    File newFile;


    public WriteToCSV(String name) {
        this.fileName=name;
        String newFileName = fileName + ".csv";
        this.newFile = new File(newFileName);

    }

    public String toString() {
        return newFile.getPath();
    }




    public String convertToCSV(String[] data) {
        return Stream.of(data)
                .collect(Collectors.joining(","));
    }

    public void writeToFile(String data) throws IOException {

        FileWriter writer = new FileWriter(newFile,true);
        writer.write("\n");
        writer.write(convertToCSV(new String[]{data}));


        writer.close();
    }
}
