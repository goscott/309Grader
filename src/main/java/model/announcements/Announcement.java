package model.announcements;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import model.driver.Grader;
import model.roster.Student;
import model.server.Server;

/**
 * Model Announcement class containing the data for an announcement.
 * 
 * @author Jacob Hardi
 *
 */
public class Announcement implements Serializable{
	/** Announcements are serializable **/
	private static final long serialVersionUID = 8866620564619813117L;
	private String subject;
	private String postedBy;
	private String dateTime;
	private String content;

	/**
	 * Constructs an Announcement object with the specified subject, poster, and body content.
	 * @param newSubject The subject of this Announcement.
	 * @param newPostedBy the name of the user that posted this Announcement.
	 * @param newContent The body content of this Announcement.
	 */
	public Announcement(String newSubject, String newPostedBy, String newContent) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		Calendar cal = Calendar.getInstance();
		subject = newSubject;
		postedBy = newPostedBy;
		dateTime = dateFormat.format(cal.getTime());
		content = newContent;
	}

	/**
	 * Returns the subject of this Announcement.
	 * @return the subject of this Announcement.
	 */
	/*@
    ensures(
        \result.equals(subject)
    );
	@*/
	public String getSubject() {
		return subject;
	}

	/**
	 * Returns the name of the user that posted this Announcement.
	 * @return the name of the user that posted this Announcement.
	 */
	/*@
    ensures(
        \result.equals(postedBy)
    );
	@*/
	public String getPostedBy() {
		if(Grader.getUser().getId().equals(postedBy)) {
			return "You";
		}
		Student poster = Server.findUser(postedBy);
		if(poster != null) {
			return poster.getName();
		}
		return "Unknown User";
	}
	
	/**
	 * Returns the string formatted to represent the date and time that this Announcement was posted.
	 * @return the string formatted to represent the date and time that this Announcement was posted.
	 */
	/*@
    ensures(
        \result.equals(dateTime)
    );
	@*/
	public String getDateTime() {
		return dateTime;
	}

	/**
	 * Returns the body content of this Announcement.
	 * @return the body content of this Announcement.
	 */
	/*@
    ensures(
        \result.equals(content)
    );
	@*/
	public String getContent() {
		return content;
	}
}
