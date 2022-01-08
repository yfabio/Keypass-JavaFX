package com.tech.developer.persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.tech.developer.domain.Person;
import com.tech.developer.domain.Pin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 * DAOPin is used to deal with only pin
 * @author yfabio
 *
 */
public class DAOPin implements DAO<Pin> {
	
	private static DAOPin instance;
	
	private DAOPin() {}

	@Override
	public void insert(Pin pin) throws PersistanceException {

		try (Connection con = ConnectionFactory.getConnection()) {

			try (PreparedStatement ps = con.prepareStatement(SQLCriteriaFactory.insertPin().get())) {
				ps.setString(1, pin.getTitle());
				ps.setString(2, pin.getUsername());
				ps.setString(3, encrypt(pin.getPassword()));
				ps.setString(4, pin.getLink());
				ps.setString(5, pin.getNotes());
				ps.setInt(6, pin.getPerson().getId());				
				ps.executeUpdate();
			}

		} catch (SQLException e) {
			throw new PersistanceException(e);
		}

	}

	@Override
	public void delete(Pin pin) throws PersistanceException {
		try (Connection con = ConnectionFactory.getConnection()) {

			try (PreparedStatement ps = con.prepareStatement(SQLCriteriaFactory.deletePin().get())) {
				ps.setInt(1, pin.getId());
				ps.executeUpdate();
			}

		} catch (SQLException e) {
			throw new PersistanceException(e);
		}

	}

	@Override
	public void update(Pin pin) throws PersistanceException {
		try (Connection con = ConnectionFactory.getConnection()) {

			try (PreparedStatement ps = con.prepareStatement(SQLCriteriaFactory.updatePin().get())) {
				ps.setString(1, pin.getTitle());
				ps.setString(2, pin.getUsername());
				ps.setString(3, encrypt(pin.getPassword()));
				ps.setString(4, pin.getLink());
				ps.setString(5, pin.getNotes());
				ps.setInt(6, pin.getPerson().getId());
				ps.setInt(7, pin.getId());
				ps.executeUpdate();
			}

		} catch (SQLException e) {
			throw new PersistanceException(e);
		}
	}

	@Override
	public List<Pin> get(Supplier<String> value,List<Object>  params) throws PersistanceException {
		
		List<Pin> pins = new ArrayList<>();		
		String sql = value.get();
		int index = 1;	
		
		try(Connection con = ConnectionFactory.getConnection()){
			
			try(PreparedStatement ps = con.prepareStatement(sql)){
				
				if(params != null) {
					for (int i = 0; i < params.size(); i++) {
						ps.setObject(index, params.get(i));
						index++;
					}	
				}	
				
				try(ResultSet set = ps.executeQuery()){
					
					while(set.next()) {						
						Pin pin = new Pin();
						pin.setId(set.getInt("id"));
						pin.setTitle(set.getString("title"));
						pin.setUsername(set.getString("username"));						
						pin.setPassword(set.getString("password"));						
						pin.setLink(set.getString("link"));
						pin.setNotes(set.getString("notes"));
						Person person = new Person();
						person.setId(set.getInt("person_id"));
						pin.setPerson(person);
						pins.add(pin);
					}
					
					
				}
				
			} 
			
		} catch (SQLException e) {
			throw new PersistanceException(e);
		}
		
		return pins;
	}
	
	
	@Override
	public Object get(Supplier<String> value, Object... params) throws PersistanceException {
		
		return null;
	}

	
	public ObservableList<String> extractNotesFromPins(List<? extends Pin> pins){
		ObservableList<String> notes = FXCollections.observableArrayList();
		if(pins !=null) {
			 notes.addAll(pins.stream().map(e -> e.getNotes()).collect(Collectors.toList()));
		}
		return notes;	
	}

	public static DAOPin getInstance() {
		return instance == null ? new DAOPin() : instance;
	}

	
	
	
}
