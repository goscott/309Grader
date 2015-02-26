package model.announcements;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Model Announcement class containing the data for an announcement.
 * 
 * @author Jacob Hardi
 *
 */
public class Announcement {
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
	public String getSubject() {
		return subject;
	}

	/**
	 * Returns the name of the user that posted this Announcement.
	 * @return the name of the user that posted this Announcement.
	 */
	public String getPostedBy() {
		return postedBy;
	}

	public String getDateTime() {
		return dateTime;
	}

	/**
	 * Returns the body content of this Announcement.
	 * @return the body content of this Announcement.
	 */
	public String getContent() {
		return content;
	}
}
