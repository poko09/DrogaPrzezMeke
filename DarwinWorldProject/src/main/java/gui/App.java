package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import org.example.*;

import java.io.FileNotFoundException;

public class App extends Application implements IAppObserver {

    private InfernalPortal map;
    private GridPane gridPane = new GridPane();
    private final int SQUARE_SIZE = 30;
    private final int MOVE_DELAY = 1000;
    private Simulation simulation;


    public void init() {
        this.map = new InfernalPortal(10,15);
        this.simulation = new Simulation(map);
        simulation.addAppObserver(this);
        simulation.setMoveDelay(MOVE_DELAY);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {

        this.drawGridPane();
        Scene scene = new Scene(this.gridPane, SQUARE_SIZE * (map.getWidth() + 1), SQUARE_SIZE * (map.getHeight() + 1));
        primaryStage.setScene(scene);
        primaryStage.show();
        Thread thread = new Thread(simulation);
        thread.start();
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
                if (this.map.isOccupied(new Vector2d(j, i))) {
                    IElement object = (IElement) this.map.objectAt(new Vector2d(j, i)).get(0);
                    if (object != null) {
                        GuiElementBox element = new GuiElementBox(object);
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
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
