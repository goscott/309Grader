package run;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.administration.tests.LoginTest;


public class Launcher extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(Launcher.class, (java.lang.String[])null);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Scene scene = new Scene((VBox) FXMLLoader.load(getClass().getResource("../view/administration/user_login.fxml")));
            
            primaryStage.setTitle("GraderTool: Login");
            primaryStage.setScene(scene);
            setUser(scene);
            
            primaryStage.show();
            primaryStage.setResizable(false);
            
            System.out.println("Init");
            
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