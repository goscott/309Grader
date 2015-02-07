package controller.predictions;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PredictionsController {
    
    @FXML
    private ImageView image;
    
    public void initialize() {
        image.setImage(new Image(this.getClass().getClassLoader().getResourceAsStream("controller/predictions/dog.jpg")));
    }
}
