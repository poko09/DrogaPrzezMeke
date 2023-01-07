package console;

import gui.Menu;
import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        // Before running the program please remember to volume up! :)
        try {
            Application.launch(Menu.class, args);
        }
        catch (IllegalArgumentException ex) {
            System.exit(0);

        }
    }
}