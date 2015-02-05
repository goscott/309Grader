package model.announcements;

//here is a basic announcement model class you can build on
public class Announcement {
    public String subject;
    public String postedBy;
    public String dateTime; // you should swap this for a Calendar object or something
    
    private String content;
    
    public Announcement(String newSubject, String newPostedBy, String newDateTime) {
        subject = newSubject;
        postedBy = newPostedBy;
        dateTime = newDateTime;
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
}
