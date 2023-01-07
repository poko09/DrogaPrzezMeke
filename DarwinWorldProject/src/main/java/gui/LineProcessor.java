package gui;

import file_support.Counter;
import javafx.application.Application;
import javafx.stage.Stage;

public class LineProcessor extends Application  {

    private Counter counter;
    String filePathName;

    public LineProcessor(Counter counter, String filePathName) {
        this.counter = counter;
        this.filePathName = filePathName;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        App application = new App();
        application.init(counter,this.filePathName);
        try {
            application.start(new Stage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
