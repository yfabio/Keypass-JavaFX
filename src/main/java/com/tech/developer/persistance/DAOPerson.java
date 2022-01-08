package com.tech.developer.persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.tech.developer.domain.Person;

/**
 * DAOPerson is used to deal with only person
 * @author yfabio
 *
 */
public class DAOPerson implements DAO<Person> {
	
	private static DAOPerson instance;
	
	private DAOPerson() {}

	@Override
	public void insert(Person person) throws PersistanceException {
	
		try (Connection con = ConnectionFactory.getConnection()) {

			try (PreparedStatement ps = con.prepareStatement(SQLCriteriaFactory.insertPerson().get())) {
				ps.setString(1, person.getUsername());
				ps.setString(2, encrypt(person.getPassword()));
				ps.setString(3, person.getLocation());
				ps.setString(4, person.getLang());
				ps.setBoolean(5, person.isBackup());
				ps.setBoolean(6, person.isLogged());				
				ps.executeUpdate();
			}

		} catch (SQLException e) {
			throw new PersistanceException(e);
		}

	}

	@Override
	public void delete(Person person) throws PersistanceException {
		try (Connection con = ConnectionFactory.getConnection()) {

			try (PreparedStatement ps = con.prepareStatement(SQLCriteriaFactory.deletePerson().get())) {
				ps.setInt(1, person.getId());
				ps.executeUpdate();
			}

		} catch (SQLException e) {
			throw new PersistanceException(e);
		}

	}

	@Override
	public void update(Person person) throws PersistanceException {
		try (Connection con = ConnectionFactory.getConnection()) {

			try (PreparedStatement ps = con.prepareStatement(SQLCriteriaFactory.updatePerson().get())) {
				ps.setString(1, person.getUsername());
				ps.setString(2, encrypt(person.getPassword()));
				ps.setString(3, person.getLocation());
				ps.setString(4, person.getLang());
				ps.setBoolean(5, person.isBackup());
				ps.setBoolean(6, person.isLogged());				
				ps.setInt(7, person.getId());
				ps.executeUpdate();
			}

		} catch (SQLException e) {
			throw new PersistanceException(e);
		}

	}

	@Override
	public List<Person> get(Supplier<String> value,List<Object> params) throws PersistanceException {
		
		List<Person> people = new ArrayList<>();		
		String sql = value.get();
		int index = 1;
		
		try (Connection con = ConnectionFactory.getConnection()) {

			try (PreparedStatement ps = con.prepareStatement(sql)) {
				
				if(params != null) {
					for (int i = 0; i < params.size(); i++) {
						ps.setObject(index, params.get(i));
						index++;
					}	
				}			
				
				try(ResultSet set = ps.executeQuery()){
					
					while(set.next()) {
						
						Person person = new Person();
						person.setId(set.getInt("id"));
						person.setUsername(set.getString("username"));
						person.setPassword(decrypt(set.getString("password")));
						person.setLocation(set.getString("location"));
						person.setLang(set.getString("lang"));
						person.setBackup(set.getBoolean("backup"));
						person.setLogged(set.getBoolean("logged"));
						
						people.add(person);
					}
					
				}
				
			}

		} catch (SQLException e) {
			throw new PersistanceException(e);
		}
		
		return people;
	}

	public static DAOPerson getInstance() {
		return instance == null ? new DAOPerson() : instance;
	}

	@Override
	public Object get(Supplier<String> value, Object... params) throws PersistanceException {
		
		return null;
	}

	
	
}
