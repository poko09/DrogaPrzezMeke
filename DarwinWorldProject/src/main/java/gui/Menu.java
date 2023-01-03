package gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;


public class Menu extends Application {




    @Override
    public void start(Stage primaryStage) throws Exception {


        Text text = new Text();
        text.setText("Welcome to Darwin World Simulation!");
        StackPane root = new StackPane();
        Button leftBox = new Button("Start Simulation" );
        VBox vbox = new VBox(leftBox);
        root.getChildren().addAll(text, vbox);

        final Scene scene = new Scene(root,400, 400, Color.DARKSEAGREEN);
        scene.setFill(Color.DARKSEAGREEN);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 20));


//        leftBox.setLayoutX(200);
        vbox.setAlignment(Pos.BASELINE_LEFT);


//        leftBox.setAlignment(Pos.CENTER_LEFT);
//        HBox.setHgrow(leftBox, Priority.ALWAYS);
//        root.setBottom(leftBox);

        primaryStage.setTitle("DarwinWorldSimulation");

        setButtonFunctions(leftBox);

        primaryStage.setScene(scene);
        primaryStage.show();

//
//        // set title for the stage
//        stage.setTitle("Darwin World Simulation");
//
//        // create a label
//        Label label = new Label("Welcome to DarwinWorldSimulation");
//
//        Text text = new Text();
//        text.setText("Welcome to Darwin World Simulation!");
//        text.setTextAlignment(TextAlignment.CENTER);
//        text.setFont(Font.font("Arial", FontWeight.BOLD, 20));
//
//        // relocate label
//
//
//        // create a Pane
//        Pane pane = new Pane();
//
//        Button button = new Button("Start Simulation");
//        pane.getChildren().addAll(button, text);
//
//        button.relocate(150,150);
//
//        // create a scene
//        Scene scene = new Scene(pane, 400, 300);
//
//        // set the scene
//        stage.setScene(scene);
//
//        stage.show();
    }

    private void setButtonFunctions(Button start) {
        start.setOnAction( event -> {
            try {
                runApplication();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });



    }

    public void runApplication() throws IOException {
        App application = new App();
        application.init();
        try {
            application.start(new Stage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }







}