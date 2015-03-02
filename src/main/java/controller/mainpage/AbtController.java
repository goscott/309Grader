package controller.mainpage;

import resources.ResourceFetcher;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
public class AbtController {
    
    @FXML
    private Button done_button;
    
    @FXML
    private Button mute_button;
    
    @FXML
    private BorderPane pane;
    
    @FXML 
    private ImageView background;
    
    private static AudioClip clip;
    
    public void initialize() {
        String url = ResourceFetcher.class.getClassLoader().getResource("resources/song.mp3").toString();
    
        background.setImage(new Image(ResourceFetcher.class.getClassLoader().getResourceAsStream("resources/background.jpg")));
        clip = new AudioClip(url);
        clip.play();
    }
    
    public void close() {
        clip.stop();
        ((Stage) pane.getScene().getWindow()).close();
    }
    
    public void mute() {
        clip.stop();
    }
    
    public static void muteStatic() {
        clip.stop();
    }
}
