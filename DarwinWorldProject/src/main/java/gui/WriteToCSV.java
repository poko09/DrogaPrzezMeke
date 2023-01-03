package gui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WriteToCSV {

    String fileName;
    File newFile;

    private int numOfLivingAnimals = 0;
    private int numOfPlants = 0;
    private int numOfEmptyFields = 0;
    private int famousGenotype = 0;
    private double averageEnergy = 0;

    private double[] array = {numOfLivingAnimals, numOfPlants, numOfEmptyFields, famousGenotype, averageEnergy};




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
        String[] allValues = {String.valueOf(array[0]),
                String.valueOf(array[1]),
                String.valueOf(array[2]),
                String.valueOf(array[3]),
                String.valueOf(array[4])};
        writer.write(convertToCSV(allValues));

        writer.close();
    }
}
