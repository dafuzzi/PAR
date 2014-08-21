package de.uulm.par.notes;

import org.joda.time.DateTime;

public class PlainNote {
	String title;
	String message;
	NoteType type;
	DateTime created;
	DateTime alert;
	String person;
	String location;
	
	public PlainNote(String title, String message, NoteType type,
			DateTime created) {
		super();
		this.title = title;
		this.message = message;
		this.type = type;
		this.created = created;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public NoteType getType() {
		return type;
	}

	public void setType(NoteType type) {
		this.type = type;
	}

	public DateTime getCreated() {
		return created;
	}

	public void setCreated(DateTime created) {
		this.created = created;
	}

	public DateTime getAlert() {
		return alert;
	}

	public void setAlert(DateTime alert) {
		this.alert = alert;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}