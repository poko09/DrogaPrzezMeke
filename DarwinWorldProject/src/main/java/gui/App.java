package gui;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.IAppObserver;

public class App extends Application implements IAppObserver {
    @Override
    public void positionChangedApp() {

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.show();

    }
}
