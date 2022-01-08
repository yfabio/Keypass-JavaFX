package com.tech.developer.domain;

import java.io.Serializable;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


/**
 * 
 * The pin represents the password that is needed to be secure onto the system. It also holds other information such as title, username
 * link and notes
 * 
 * @author yfabio
 *
 */
public class Pin implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private ObjectProperty<Integer> id = new SimpleObjectProperty<>();

	private StringProperty title = new SimpleStringProperty();

	private StringProperty username = new SimpleStringProperty();

	private StringProperty password = new SimpleStringProperty();

	private StringProperty link = new SimpleStringProperty();

	private StringProperty notes = new SimpleStringProperty("");

	private Person person = new Person();

	public Pin() {
	}
	
	public Pin(Integer id, String title, String username, String password,String notes) {		
		this.id.set(id);
		this.title.set(title);
		this.username.set(username);
		this.password.set(password);
		this.notes.set(notes);
	}





	public ObjectProperty<Integer> idProperty() {
		return this.id;
	}

	public Integer getId() {
		return this.idProperty().get();
	}

	public void setId(Integer id) {
		this.idProperty().set(id);
	}

	public StringProperty titleProperty() {
		return this.title;
	}

	public String getTitle() {
		return this.titleProperty().get();
	}

	public void setTitle(String title) {
		this.titleProperty().set(title);
	}

	public StringProperty usernameProperty() {
		return this.username;
	}

	public String getUsername() {
		return this.usernameProperty().get();
	}

	public void setUsername(String username) {
		this.usernameProperty().set(username);
	}

	public StringProperty passwordProperty() {
		return this.password;
	}

	public String getPassword() {
		return this.passwordProperty().get();
	}

	public void setPassword(String password) {
		this.passwordProperty().set(password);
	}

	public StringProperty linkProperty() {
		return this.link;
	}

	public String getLink() {
		return this.linkProperty().get();
	}

	public void setLink(String link) {
		this.linkProperty().set(link);
	}

	public StringProperty notesProperty() {
		return this.notes;
	}

	public String getNotes() {
		return this.notesProperty().get();
	}

	public void setNotes(String notes) {
		this.notesProperty().set(notes);
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id: ").append(getId()).append(System.lineSeparator()).append("tite: ").append(getTitle())
				.append(System.lineSeparator()).append("username: ").append(getUsername())
				.append(System.lineSeparator()).append("password: ").append(getPassword())
				.append(System.lineSeparator()).append("link").append(getLink()).append(System.lineSeparator())
				.append("notes: ").append(getNotes()).append(System.lineSeparator()).append("person_id: ")
				.append(getPerson().getId()).append(System.lineSeparator());
		return sb.toString();
	}

	@Override
	public int hashCode() {
		return this.getId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}else if(obj instanceof Pin) {
			Pin other = (Pin) obj;
			return getId().equals(other.getId());
		}
		return true;
	}

	
	
	
	
}
