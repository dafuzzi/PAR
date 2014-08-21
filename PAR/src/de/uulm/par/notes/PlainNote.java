package de.uulm.par.notes;

import org.joda.time.DateTime;

/**
 * @author Fabian Schwab
 *
 */
public class PlainNote {
	String title;
	String message;
	NoteType type;
	DateTime created;
	DateTime alert;
	String person;
	String location;
	
	/**
	 * @param title
	 * @param message
	 * @param type
	 * @param created
	 */
	public PlainNote(String title, String message, NoteType type,
			DateTime created) {
		super();
		this.title = title;
		this.message = message;
		this.type = type;
		this.created = created;
	}

	/**
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return
	 */
	public NoteType getType() {
		return type;
	}

	/**
	 * @param type
	 */
	public void setType(NoteType type) {
		this.type = type;
	}

	public DateTime getCreated() {
		return created;
	}

	/**
	 * @param created
	 */
	public void setCreated(DateTime created) {
		this.created = created;
	}

	/**
	 * @return
	 */
	public DateTime getAlert() {
		return alert;
	}

	/**
	 * @param alert
	 */
	public void setAlert(DateTime alert) {
		this.alert = alert;
	}

	/**
	 * @return
	 */
	public String getPerson() {
		return person;
	}

	/**
	 * @param person
	 */
	public void setPerson(String person) {
		this.person = person;
	}

	/**
	 * @return
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location
	 */
	public void setLocation(String location) {
		this.location = location;
	}
}