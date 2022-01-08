package com.tech.developer.domain;

import java.io.Serializable;
import java.util.Locale;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * The person represents a person who has an account to login into the system 
 *  
 * @author yfabio
 *
 */
public class Person implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private ObjectProperty<Integer> id = new SimpleObjectProperty<>();

	private  StringProperty username = new SimpleStringProperty();

	private  StringProperty password = new SimpleStringProperty();

	private  StringProperty location = new SimpleStringProperty(System.getProperty("user.home"));
	
	private StringProperty lang = new SimpleStringProperty(Locale.forLanguageTag("en-CA").toLanguageTag());
	
	private BooleanProperty backup = new SimpleBooleanProperty(false);

	private  BooleanProperty logged = new SimpleBooleanProperty(false);
	
	

	public Person() {
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

	public StringProperty locationProperty() {
		return this.location;
	}

	public String getLocation() {
		return this.locationProperty().get();
	}

	public void setLocation(String location) {
		this.locationProperty().set(location);
	}
	
	public BooleanProperty backupProperty() {
		return this.backup;
	}
	

	public boolean isBackup() {
		return this.backupProperty().get();
	}
	

	public void setBackup(boolean backup) {
		this.backupProperty().set(backup);
	}
	
	

	public BooleanProperty loggedProperty() {
		return this.logged;
	}

	public boolean isLogged() {
		return this.loggedProperty().get();
	}

	public void setLogged(boolean logged) {
		this.loggedProperty().set(logged);
	}
	
	
	
	public StringProperty langProperty() {
		return this.lang;
	}
	

	public String getLang() {
		return this.langProperty().get();
	}
	

	public void setLang(final String lang) {
		this.langProperty().set(lang);
	}
	
	

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("id: ").append(getId()).append(System.lineSeparator()).append("username: ").append(getUsername())
				.append(System.lineSeparator()).append("password: ").append(getPassword())
				.append(System.lineSeparator()).append("Location: ").append(getLocation())
				.append(System.lineSeparator()).append("Laguage: ").append(getLang())
				.append(System.lineSeparator()).append("logged: ").append(isLogged()).append(System.lineSeparator());

		return sb.toString();
	}

	@Override
	public int hashCode() {
		return  this.getId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}else if(obj instanceof Person) {
			Person other = (Person) obj;
			return getId().equals(other.getId());
		}
		return true;
	}

	
	

	
	

	

	
	
	
	

}
