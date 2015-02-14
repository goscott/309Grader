package run;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import testing.administration.LoginTest;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.driver.Debug;
import model.server.Server;


public class Launcher extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	Server.init();
        Application.launch(Launcher.class, (java.lang.String[])null);
    }
    
    /**
     * Launches the user login view.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            Scene scene = new Scene((VBox) FXMLLoader.load(getClass().getClassLoader().getResource("view/administration/user_login.fxml")));
            
            primaryStage.setTitle("GraderTool: Login");
            primaryStage.setScene(scene);
            setUser(scene);
            
            primaryStage.show();
            primaryStage.setResizable(false);
            
            Debug.newFile();
            Debug.log("Grader Tool", "Initializing...\n");
            
        } catch (Exception ex) {
            Logger.getLogger(LoginTest.class.getName()).log(Level.SEVERE, null, ex);
            Debug.log("ERROR", "Exception thrown during application start");
        }
    }
    
    /**
     * Fills in the username that was last logged into.
     * @param scene The current scene.
     */
    private void setUser(Scene scene) {
        //String filename = "src/main/java/model/administration/login.txt";
        String filename = "LoginData/login.txt";
        File file = new File(filename);
        TextField field;
        BufferedReader reader;
        
        if (file.exists()) {
            //set username field
            field = (TextField) scene.lookup("#user_name");
            
            try {
                //reader = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("model/administration/login.txt")));
                //System.out.println("**");
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
                field.setText(reader.readLine());
                reader.close();
                ((PasswordField) scene.lookup("#password")).requestFocus();
            }
            catch (Exception e) {
            	Debug.log("ERROR", "Could not read login file");
                e.printStackTrace();
            }
        }
    }
}