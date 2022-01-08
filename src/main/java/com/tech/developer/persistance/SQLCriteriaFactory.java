package com.tech.developer.persistance;

import java.util.function.Supplier;

/**
 * The SQLCriteriaFactory contains all the SQLs statemens in the system.
 * 
 * @author yfabio
 *
 */
public interface SQLCriteriaFactory {

	// STANDARD SQL QUERIES

	// Pin criteria

	public static Supplier<String> insertPin() {
		return () -> "INSERT INTO pin (title,username,password,link,notes,person_id) VALUES (?,?,?,?,?,?)";
	}

	public static Supplier<String> deletePin() {
		return () -> "DELETE FROM pin WHERE pin.id = ?";
	}

	public static Supplier<String> deleteAllPin() {
		return () -> "DELETE FROM pin where pin.id > 0";
	}

	public static Supplier<String> updatePin() {
		return () -> "UPDATE pin SET title = ?, username = ?, password =?, link = ?, notes = ?, person_id = ? WHERE id = ?";
	}

	public static Supplier<String> getPinsByPersonID() {
		return () -> "SELECT * FROM pin INNER JOIN person b ON person_id = ? ORDER BY id";
	}

	public static Supplier<String> getPins() {
		return () -> "SELECT * FROM pin";
	}

	public static Supplier<String> getPinByTitle() {
		return () -> "SELECT * FROM pin WHERE UPPER(title) like ?";
	}

	public static Supplier<String> filterByTitle() {
		return () -> "SELECT * FROM pin WHERE UPPER(title) like ? ORDER BY id";
	}

	public static Supplier<String> findPinByID() {
		return () -> "SELECT * FROM pin WHERE id = ?";
	}

	// Person criteria

	public static Supplier<String> insertPerson() {
		return () -> "INSERT INTO person (username,password,location,lang,backup,logged) VALUES (?,?,?,?,?,?)";
	}

	public static Supplier<String> deleteAllPerson() {
		return () -> "DELETE FROM person WHERE id > ?";
	}

	public static Supplier<String> deletePerson() {
		return () -> "DELETE FROM person WHERE id = ?";
	}

	public static Supplier<String> deletePersonByUsername() {
		return () -> "DELETE FROM person WHERE username = ?";
	}

	public static Supplier<String> updatePerson() {
		return () -> "UPDATE person SET username = ?, password = ? , location = ?, lang = ?, backup = ?,logged = ? WHERE id = ?;";
	}

	public static Supplier<String> getPersons() {
		return () -> "SELECT * FROM person";
	}

	public static Supplier<String> getPersonByID() {
		return () -> "SELECT * FROM person WHERE id = ?";
	}

	public static Supplier<String> getPersonByUsername() {
		return () -> "SELECT * FROM person WHERE username = ?";
	}

	public static Supplier<String> getPersonByPasswordAndUsername() {
		return () -> "SELECT * FROM person WHERE password = ? and UPPER(username) = ?";
	}

	// Config

	public static Supplier<String> insertConfig() {
		return () -> "INSERT INTO holder (id,current,logged,lang) VALUES(?,?,?,?)";
	}

	public static Supplier<String> updateConfig(String key) {
		String sql = String.format("UPDATE holder SET %s = ? WHERE id = 1;", key);
		return () -> sql;
	}

	public static Supplier<String> getLoggedFromConfig() {
		return () -> "SELECT logged FROM holder WHERE id = ?";
	}

	public static Supplier<String> getCurrentFromConfig() {
		return () -> "SELECT current FROM holder WHERE id = ?";
	}

	public static Supplier<String> getLangFromConfig() {
		return () -> "SELECT lang FROM holder WHERE id = ?";
	}

	public static Supplier<String> getCountFromConfig() {
		return () -> "SELECT COUNT(*) FROM holder";
	}

}
