package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.*;

import java.io.FileNotFoundException;
import java.io.IOException;


public class App extends Application implements IAppObserver{

    private InfernalPortal map;
    private GridPane gridPane = new GridPane();
    private Label trackedAnimalLabel = new Label();
    private Label actualStatistics = new Label();
    private VBox mainVBox = new VBox();
    private final int SQUARE_SIZE = 30;
    private final int MOVE_DELAY = 1000;
    private Simulation simulation;
    private DataSet data;

    private boolean isSuspended;
    private boolean animalIsTracked;
    private Animal trackedAnimal;
    private Thread simulationThread;

    private final int RGBSIZE = 255;



    public void init() throws IOException {
        this.data = new DataSet("parametry.txt");
        this.map = new InfernalPortal(this.data);
        this.simulation = new Simulation(map, this.data);
        simulation.addAppObserver(this);
        simulation.setMoveDelay(MOVE_DELAY);
        this.isSuspended =false;
        this.animalIsTracked=false;
        this.simulationThread = new Thread(simulation);
        simulationThread.start();
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        HBox allButtonshbBox = new HBox();
        Button stopButton = new Button("Stop Simulation");
        Button resumeButton = new Button("Resume Simulation");
        Button stopTrackingButton = new Button("Stop tracking");
        Button genotypeButton = new Button("Most popular genotype");
        Button startNewSimulation = new Button("Start New");
        allButtonshbBox.setSpacing(10.0);
        allButtonshbBox.setAlignment(Pos.BOTTOM_CENTER);
        allButtonshbBox.getChildren().addAll(stopButton,resumeButton, stopTrackingButton, genotypeButton);

        this.setButtonFunctions(stopButton,resumeButton, stopTrackingButton, genotypeButton, startNewSimulation);

        this.mainVBox.getChildren().addAll(allButtonshbBox, this.gridPane, this.trackedAnimalLabel, this.actualStatistics);
        this.drawGridPane(false);
        Scene scene = new Scene(mainVBox, SQUARE_SIZE * (map.getWidth() + 5), SQUARE_SIZE * (map.getHeight() + 5));
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    public void setButtonFunctions(Button stopButton, Button resumeButton, Button stopTrackingButton, Button genotypeButton, Button startNewSimulationButton) {
        stopButton.setOnAction(event -> {
            this.stopButtonLogic();
        });
        resumeButton.setOnAction(event -> {
            this.resumeButtonLogic();
        });
        stopTrackingButton.setOnAction(event -> {
            this.stopTrackingButtonLogic();
        });
        genotypeButton.setOnAction(event -> {
            try {
                this.genotypeButtonLogic();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        startNewSimulationButton.setOnAction(event -> {
            this.startNewSImulationLogic();
        });
    }

    public void genotypeButtonLogic() throws FileNotFoundException {
        if (this.isSuspended) {
            System.out.println("button clicked");
            gridPane.getChildren().clear();
            this.drawGridPane(true);
        }
    }

    public void stopButtonLogic() {
        if (!this.isSuspended) {
            this.simulationThread.suspend();
            this.isSuspended =true;
            this.setActualStatisticsLabel();
        }
    }

    public void resumeButtonLogic() {
        if (this.isSuspended) {
            this.simulationThread.resume();
            this.isSuspended =false;
            this.actualStatistics.setText("");
        }
    }

    public void setActualStatisticsLabel() {
        StringBuilder sb = new StringBuilder();
        sb.append("Actual statistics of the map");
        sb.append("\nDay of the simulation: " + simulation.getDayOfSimulation());
        sb.append("\nNumber of animals on the map: " + simulation.getNumberOfAnimals());
        sb.append( "\nNumber of plants:  " + simulation.getNumberOfPlants());
        sb.append( "\nNumber of free fields: " + simulation.getNumberOfFreeFields());
        sb.append("\nMost popular genotype: " + map.mostPopularGenotype());
        sb.append("\nAverage energy of living animals: "+simulation.calculateAverageEnergy());
        sb.append("\nAverage life length of dead animals: " + simulation.calculateAverageLifeLength());
        this.actualStatistics.setText(sb.toString());
    }

    public void stopTrackingButtonLogic() {
        if (this.animalIsTracked) {
            this.animalIsTracked=false;
            this.trackedAnimalLabel.setText("");
            this.trackedAnimal=null;
        }
    }

    public void startNewSImulationLogic(){
//        Thread lp = new Thread(new LineProcessor());
        LineProcessor lp = new LineProcessor();

        try {
            lp.start(new Stage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }




    public void drawXY() {
        Label label = new Label("y/x");
        this.gridPane.add(label,0,0);
        GridPane.setHalignment(label, HPos.CENTER);
    }

    private void drawYAxis() {
        int w = 1;
        for (int i = map.getHeight()-1; i >= 0; i--) {
            this.gridPane.getRowConstraints().add(new RowConstraints(SQUARE_SIZE));
            Label labelY = new Label(Integer.toString(i));
            labelY.setMinHeight(SQUARE_SIZE);
            labelY.setMinWidth(SQUARE_SIZE);
            labelY.setAlignment(Pos.CENTER);
            this.gridPane.add(labelY, 0, w, 1, 1);
            GridPane.setHalignment(labelY, HPos.CENTER);
            w++;
        }
    }


    private void drawXAxis() {
        int k =1;
        for (int j = 0; j < map.getWidth() ; j++) {

            this.gridPane.getColumnConstraints().add(new ColumnConstraints(SQUARE_SIZE));
            Label labelX = new Label(Integer.toString(j));
            labelX.setMinHeight(SQUARE_SIZE);
            labelX.setMinWidth(SQUARE_SIZE);
            labelX.setAlignment(Pos.CENTER);
            this.gridPane.add(labelX,k,0,1,1);
            GridPane.setHalignment(labelX, HPos.CENTER);
            k++;
            }
    }

    public void drawObjects(boolean highlightStrongGenotypes) throws FileNotFoundException {
        int w = 1;
        for (int i = this.map.getHeight()-1; i >= 0; i--) {
            int k = 1;
            for (int j = 0; j <= this.map.getWidth()-1; j++) {
                Vector2d currentposition = new Vector2d(j,i);
                if (this.map.isOccupied(currentposition)) {
                    for (Animal animal : map.animalObjectAt(currentposition)) {
                        Button animalButton;
                        if (highlightStrongGenotypes && animal.getGenotype().equals(this.map.mostPopularGenotype())) {
                            animalButton = drawAnimal(animal, 255, 0, 0);
                        } else {

                            int red = 0;
                            int green = 0;
                            int blue;
                            blue = Math.min(RGBSIZE, RGBSIZE - animal.getEnergy());
                            animalButton = drawAnimal(animal, red, green, blue);
                        }
                        gridPane.add(animalButton, k, w);
                        GridPane.setHalignment(animalButton, HPos.CENTER);
                        GridPane.setHalignment(animalButton, HPos.CENTER);

                    }


                }

                else if (this.map.isOccupiedByGrass(currentposition) && !this.map.isOccupied(currentposition)) {
                        Button plantButton = drawPlant();
                        gridPane.add(drawPlant(),k,w);
                        GridPane.setHalignment(plantButton, HPos.CENTER);
                        GridPane.setHalignment(plantButton, HPos.CENTER);
                }
                k++;
            }
            w++;

        }
    }


    public Button drawAnimal(Animal animal, int red, int green, int blue) {
        Button buttonAnimal = new Button();
        animalButtonLogic(buttonAnimal, animal);
        buttonAnimal.setBackground(new Background(new BackgroundFill(Color.rgb(red, green, blue), CornerRadii.EMPTY, Insets.EMPTY)));
        buttonAnimal.setMinHeight(SQUARE_SIZE-2);
        buttonAnimal.setMinWidth(SQUARE_SIZE-2);
        return buttonAnimal;
    }

    public Button drawPlant() {
        Button plantButton = new Button();
        plantButton.setBackground(new Background(new BackgroundFill(Color.rgb(50,205,50), CornerRadii.EMPTY, Insets.EMPTY)));
        plantButton.setMinHeight(SQUARE_SIZE-2);
        plantButton.setMinWidth(SQUARE_SIZE-2);
        return plantButton;
    }
    public void animalButtonLogic(Button buttonAnimal, Animal animal) {
        buttonAnimal.setOnAction(event -> {
            if (this.isSuspended) {
                this.animalIsTracked=true;
                this.trackedAnimal=animal;
                this.trackedAnimalLabel.setText("Data of the tracked animal: " + animal.toString());
            }
        });

    }



    private void drawGridPane(boolean highlightStrongGenotypes) throws FileNotFoundException {
        this.gridPane.setGridLinesVisible(false);
        this.gridPane.getColumnConstraints().clear();
        this.gridPane.getRowConstraints().clear();

        this.gridPane.setGridLinesVisible(true);

        this.drawXY();
        this.drawXAxis();
        this.drawYAxis();
        this.drawObjects(highlightStrongGenotypes);

    }

    @Override
    public void positionChangedApp() {
        Platform.runLater(() -> {
            gridPane.getChildren().clear();
            try {
                drawGridPane(false);
                if (animalIsTracked) {
                    this.trackedAnimalLabel.setText("Data of tracked animal\n" + this.trackedAnimal.toString());
                }


            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }
}



