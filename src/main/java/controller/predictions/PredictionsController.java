package controller.predictions;

import resources.ResourceLoader;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PredictionsController {
    
    @FXML
    private ImageView image;
    
    public void initialize() {
        image.setImage(new Image(ResourceLoader.class.getClassLoader().getResourceAsStream("resources/dog.jpg")));
    }
}
