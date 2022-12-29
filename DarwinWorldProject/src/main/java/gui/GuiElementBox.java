package gui;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.example.IElement;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GuiElementBox {

    public static final int ELEMENT_SIZE = 20;
    private VBox vBoxElement;

    public VBox getVBoxElement() {
        return vBoxElement;
    }


    public GuiElementBox(IElement element, boolean strongGenotype) throws FileNotFoundException {
        Image image;
        if (strongGenotype) {
            image = new Image (new FileInputStream("src/main/resources/strong.png"));
        } else {
            image = new Image (new FileInputStream(element.getNameOfPathElement()));
        }

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(ELEMENT_SIZE);
        imageView.setFitHeight(ELEMENT_SIZE);
        vBoxElement = new VBox();
        vBoxElement.getChildren().add(imageView);
        vBoxElement.setAlignment(Pos.CENTER);
    }
}
