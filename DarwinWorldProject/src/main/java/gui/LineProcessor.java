package gui;

import javafx.application.Application;
import javafx.stage.Stage;

public class LineProcessor extends Application  {



    @Override
    public void start(Stage primaryStage) throws Exception {
        App application = new App();
        application.init();
        try {
            application.start(new Stage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//        System.out.println("XXXXXXXXXXXXXXXX\n"+ Thread.currentThread().getId());

    }

}
