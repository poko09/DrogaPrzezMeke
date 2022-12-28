package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.*;

import java.io.FileNotFoundException;

public class App extends Application implements IAppObserver{

    private InfernalPortal map;
    private GridPane gridPane = new GridPane();
    private Label trackedAnimalLabel = new Label();
    private VBox mainVBox = new VBox();
    private final int SQUARE_SIZE = 30;
    private final int MOVE_DELAY = 1000;
    private Simulation simulation;
    private DataSet data;

    private boolean isSuspended;
    private boolean animalIsTracked;
    private Animal trackedAnimal;
    private Thread simulationThread;


    public void init() {
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

        allButtonshbBox.setSpacing(10.0);
        allButtonshbBox.setAlignment(Pos.BOTTOM_CENTER);
        allButtonshbBox.getChildren().addAll(stopButton,resumeButton, stopTrackingButton, genotypeButton);

        this.setButtonFunctions(stopButton,resumeButton, stopTrackingButton, genotypeButton);

        this.mainVBox.getChildren().addAll(allButtonshbBox, this.gridPane, this.trackedAnimalLabel);
        this.drawGridPane();
        Scene scene = new Scene(mainVBox, SQUARE_SIZE * (map.getWidth() + 5), SQUARE_SIZE * (map.getHeight() + 5));
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    public void setButtonFunctions(Button stopButton, Button resumeButton, Button stopTrackingButton, Button genotypeButton) {
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
            this.genotypeButtonLogic();
        });
    }

    public void genotypeButtonLogic() {
        if (this.isSuspended) {

        }
    }

    public void stopButtonLogic() {
        if (!this.isSuspended) {
            this.simulationThread.suspend();
            this.isSuspended =true;
        }
    }

    public void resumeButtonLogic() {
        if (this.isSuspended) {
            this.simulationThread.resume();
            this.isSuspended =false;
        }
    }

    public void stopTrackingButtonLogic() {
        if (this.animalIsTracked) {
            this.animalIsTracked=false;
            this.trackedAnimalLabel.setText("");
            this.trackedAnimal=null;
        }
    }
    public void ifAnimalIsClicked(Animal animal) {
        if (this.isSuspended) {
            this.animalIsTracked=true;
            this.trackedAnimal=animal;
            this.trackedAnimalLabel.setText(animal.toString());
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

    public void drawObjects() throws FileNotFoundException {
        int w = 1;
        for (int i = this.map.getHeight(); i >= 0; i--) {
            int k = 1;
            for (int j = 0; j <= this.map.getWidth(); j++) {
                //ToDo: dodac rosliny
                if (this.map.isOccupied(new Vector2d(j, i))) {
                    IElement object = (IElement) this.map.objectAt(new Vector2d(j, i)).get(0);
                    if (object != null) {
                        GuiElementBox element = new GuiElementBox(object);
                        VBox newVBox = element.getVBoxElement();
                        if (object instanceof Animal && !this.animalIsTracked) {
                            newVBox.setOnMouseClicked(event -> {
                                System.out.println("tracked");
                                this.animalIsTracked=true;
                                ifAnimalIsClicked((Animal)object);
                            });
                        }

                        gridPane.add(element.getVBoxElement(), k, w);
                        GridPane.setHalignment(element.getVBoxElement(), HPos.CENTER);
                        GridPane.setHalignment(element.getVBoxElement(), HPos.CENTER);
                    }
                }
                k++;
            }
            w++;
        }
    }




    private void drawGridPane() throws FileNotFoundException {
        this.gridPane.setGridLinesVisible(false);
        this.gridPane.getColumnConstraints().clear();
        this.gridPane.getRowConstraints().clear();

        this.gridPane.setGridLinesVisible(true);

        this.drawXY();
        this.drawXAxis();
        this.drawYAxis();
        this.drawObjects();

    }

    @Override
    public void positionChangedApp() {
        Platform.runLater(() -> {
            gridPane.getChildren().clear();
            try {
                drawGridPane();
                if (animalIsTracked) {
                    this.trackedAnimalLabel.setText(this.trackedAnimal.toString());
                }

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

//    @Override
//    public void run() {
//        this.init();
//        try {
//            this.start(new Stage());
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    //        addNewSimulation.setOnAction(click -> {
//            System.out.println("new simulation");
//            this.addNewSimulation();
//        });

    //    public void addNewSimulation() {
//        App app = new App();
//        Thread thread = new Thread(app);
//        thread.start();
//    }
}
