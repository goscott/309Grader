package testing.administration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import controller.GraderPopup;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/*
 * 
 * @author Mason Stevenson
 *
 */
public class LoginTest extends Application {

    /*
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(LoginTest.class, (java.lang.String[])null);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Scene scene = new Scene((VBox) GraderPopup.getResource("view/administration/user_login.fxml"));
            
            primaryStage.setScene(scene);
            primaryStage.setTitle("GraderTool: Login");
            
            setUser(scene);
            
            primaryStage.show();
            
            
        } catch (Exception ex) {
            Logger.getLogger(LoginTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void setUser(Scene scene) {
        String filename = "src/main/java/model/administration/login.txt";
        File file = new File(filename);
        TextField field;
        BufferedReader reader;
        
        if (file.exists()) {
            //set username field
            field = (TextField) scene.lookup("#user_name");
            
            try {
                reader = new BufferedReader(new FileReader(file));
                field.setText(reader.readLine());
                reader.close();
            }
            
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
