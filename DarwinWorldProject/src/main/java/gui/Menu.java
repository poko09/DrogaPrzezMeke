package gui;

import file_support.Counter;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;


public class Menu extends Application {

    private Counter counter=new Counter();
    private TextField txtNameOfFilePath = new TextField("src/main/resources/parametry.txt");

    @Override
    public void start(Stage primaryStage) throws Exception {

        music();
        VBox root = new VBox(50);

        Text welcomeText = new Text();
        welcomeText.setText("Welcome to Darwin World Simulation!");
        welcomeText.setTextAlignment(TextAlignment.CENTER);
        welcomeText.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Label labelStart = new Label("Press the button if you are ready to start simulation");
        labelStart.setFont(Font.font("Arial", 15));
        Button startButton = new Button("Start Simulation" );
        startButton.setMinWidth(100);
        startButton.setMinHeight(50);
        startButton.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        VBox vboxStart = new VBox(10,labelStart, startButton);
        vboxStart.setAlignment(Pos.CENTER);

        Label labelFilePath = new Label("Enter the path of the file from which you want to retrieve the data");
        labelFilePath.setAlignment(Pos.CENTER);
        labelFilePath.setFont(Font.font("Arial", 15));
        VBox pathNameVBox = new VBox(10,labelFilePath, this.txtNameOfFilePath);
        vboxStart.setAlignment(Pos.CENTER);

        root.getChildren().addAll( welcomeText, pathNameVBox, vboxStart);
        root.setAlignment(Pos.CENTER);

        final Scene scene = new Scene(root,450, 450, Color.DARKSEAGREEN);
        scene.setFill(Color.DARKSEAGREEN);

        primaryStage.setTitle("DarwinWorldSimulation");
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
        setButtonFunctions(startButton);

        primaryStage.setScene(scene);
        primaryStage.show();
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

    public void music() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        String musicLocation = "src/main/resources/dancingAnimals.wav";
        File musicPath = new File (musicLocation);
        AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
        Clip clip = AudioSystem.getClip();
        clip.open(audioInput);
        clip.start();
    }

    public void runApplication() throws IOException {

        App application = new App();
        application.init(this.counter, this.txtNameOfFilePath.getText());
        try {
            application.start(new Stage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}