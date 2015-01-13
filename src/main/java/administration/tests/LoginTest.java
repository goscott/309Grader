package administration.tests;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 
 * @author Mason Stevenson
 *
 */
public class LoginTest extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(LoginTest.class, (java.lang.String[])null);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Scene scene = new Scene((Parent) FXMLLoader.load(getClass().getResource("../resources/user_login.fxml")));
            
            primaryStage.setScene(scene);
            primaryStage.setTitle("GraderTool: Login");
            
            primaryStage.show();
            
        } catch (Exception ex) {
            Logger.getLogger(LoginTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
