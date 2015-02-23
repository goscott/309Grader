package controller.announcements;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.announcements.Announcement;

public class AnnouncementsController {
    
    @FXML
    private TableView<Announcement> table; // This is how you reference your fxml objects
    
    @FXML
    private TableColumn<Announcement, String> subject_column;
    
    @FXML
    private TableColumn<Announcement, String> posted_by_column;
    
    @FXML
    private TableColumn<Announcement, String> date_time_column;
    
    @FXML
    private Button add_announcent;
    
    private ObservableList<Announcement> data;
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void initialize() { //this gets called automatically at startup
            
        //Here is some fake data to populate your table with initially.
        //Eventually, it should come from our "server" (A text file or something)
        data =
                FXCollections.observableArrayList(
                    new Announcement("Subject 1", "Gene Fisher", "content1"),
                    new Announcement("Subject 2", "Gavin Scott", "content2"),
                    new Announcement("Subject 3", "Barack Obama", "content3"),
                    new Announcement("Subject 4", "Snoopy", "content4"),
                    new Announcement("Subject 5", "Blornog, King of the Martians", "content5")
                );
        
        //These dictate how the columns get the info from the announcement object
        subject_column.setCellValueFactory(new Callback() {
            public SimpleStringProperty call(CellDataFeatures<Announcement, String> param) {
                return new SimpleStringProperty(param.getValue().getSubject()); //Here is where it happens (Note the call on Announcement.getSubject())
            }

            public Object call(Object param) {
                return call((CellDataFeatures<Announcement, String>) (param));
            }
        });
        
        //These dictate how the columns get the info from the announcement object
        posted_by_column.setCellValueFactory(new Callback() {
            public SimpleStringProperty call(CellDataFeatures<Announcement, String> param) {
                return new SimpleStringProperty(param.getValue().getPostedBy()); //Here is where it happens (Note the call on Announcement.getPostedBy())
            }

            public Object call(Object param) {
                return call((CellDataFeatures<Announcement, String>) (param));
            }
        });
        
        //These dictate how the columns get the info from the announcement object
        date_time_column.setCellValueFactory(new Callback() {
            public SimpleStringProperty call(CellDataFeatures<Announcement, String> param) {
                return new SimpleStringProperty(param.getValue().getDateTime()); //Here is where it happens (Note the call on Announcement.getDateTime())
            }

            public Object call(Object param) {
                return call((CellDataFeatures<Announcement, String>) (param));
            }
        });
        
        //This makes something happen when you double click on a row in the table
        //It should launch an announcement viewer
        table.setOnMousePressed(new EventHandler<MouseEvent>() {
            @SuppressWarnings("static-access")
			@Override 
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                	Stage newStage = new Stage();
                	ViewAnnouncementController popup = new ViewAnnouncementController();
            		popup.setAnnouncement(table.getSelectionModel().getSelectedItem());
            		popup.start(newStage);
                    //this is how you get a reference to the selected announcement
                    //JOptionPane.showMessageDialog(null, table.getSelectionModel().getSelectedItem().getContent(),
                    //	"" + table.getSelectionModel().getSelectedItem().getSubject(), JOptionPane.PLAIN_MESSAGE);
                }
            }
        });
        
        table.setItems(data);
    }
    
    //This should launch a new announcement dialog, and then add the announcement to the list
    public void addAnnouncement() {
    	Stage newStage = new Stage();
    	AddAnnouncementController popup = new AddAnnouncementController();
		popup.setParent(this);
		popup.start(newStage);
    	/*
    	String content, subject;
    	subject = JOptionPane.showInputDialog(null, "Enter announcement subject:", "Add Announcement: Subject", JOptionPane.OK_CANCEL_OPTION);
    	if (subject != null)
    	{	content = JOptionPane.showInputDialog(null, "Enter announcement content:", "Add Announcement: Content", JOptionPane.OK_CANCEL_OPTION);
    	
    	}
    	else
    	{
    		content = null;
    	}
    	if (content != null)
    	{
    		data.add(new Announcement(subject, "You", content));
    		table.setItems(data);
    	}*/
    }
    
    void addAnnouncementToList(Announcement announcement) {
    	data.add(announcement);
		table.setItems(data);
    }
}
