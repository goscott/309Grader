package model.announcements;

//here is a basic announcement model class you can build on
public class Announcement {
    private String subject;
    private String postedBy;
    private String dateTime; // you should swap this for a Calendar object or something
    
    private String content;
    
    public Announcement(String newSubject, String newPostedBy, String newDateTime, String newContent) {
        subject = newSubject;
        postedBy = newPostedBy;
        dateTime = newDateTime;
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
