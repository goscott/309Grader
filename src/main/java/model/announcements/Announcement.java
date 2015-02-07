package model.announcements;

import java.text.SimpleDateFormat;
import java.util.Calendar;

//here is a basic announcement model class you can build on
public class Announcement {
    private String subject;
    private String postedBy;
    private String dateTime; // you should swap this for a Calendar object or something
    
    private String content;
    
    public Announcement(String newSubject, String newPostedBy, String newContent) {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
    	Calendar cal = Calendar.getInstance();
        subject = newSubject;
        postedBy = newPostedBy;
        dateTime = dateFormat.format(cal.getTime());
        content = newContent;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public String getPostedBy() {
        return postedBy;
    }
    
    public String getDateTime() {
        return dateTime;
    }
    
    public String getContent() {
    	return content;
    }
}
