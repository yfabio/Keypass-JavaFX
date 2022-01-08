package com.tech.developer.persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;
import java.util.List;
import java.util.function.Supplier;

import com.tech.developer.crypto.CryptoException;
import com.tech.developer.crypto.CryptoUtil;


/**
 * 
 * The DAO is used to perform the CRUD on the domain classes
 * 
 * @author yfabio
 *
 * @param <E>
 */
public interface DAO<E> {
	
	public static final byte[] SECRET_KEY = "LAuJALJ8AkjeHSEQ".getBytes();
	
	public void insert(E e) throws PersistanceException;
	public void delete(E e) throws PersistanceException;
	public void update(E e)throws PersistanceException;
	public List<E> get(Supplier<String> value,List<Object> params) throws PersistanceException;
	public Object get(Supplier<String> value,Object...params) throws PersistanceException;
	
	
	/**
	 * The create method creates the databse as well as the tables 
	 * @throws PersistanceException
	 */
	public static void create() throws PersistanceException {		
		try(Connection con = ConnectionFactory.getConnection()){		
			try(Statement st = con.createStatement()) {					
				st.execute(personTable());
				st.execute(pinTable());							
			}			
		} catch (SQLException e) {
			throw new PersistanceException(e);
		}
	}
	
	/**
	 * The execute method executes a raw sql 
	 * @param sql
	 * @throws PersistanceException
	 */
	public default void execute(String sql) throws PersistanceException {
		try(Connection con = ConnectionFactory.getConnection()){		
			try(Statement st = con.createStatement()) {				
				st.execute(sql);		
			}			
		} catch (SQLException e) {
			throw new PersistanceException(e);
		}
	}
	
	/**
	 *  The overloaded execute performs insert, delete and update by using object parameters
	 * @param sql
	 * @param params
	 * @return
	 * @throws PersistanceException
	 */
	public default int execute(String sql,List<Object> params) throws PersistanceException {
		int index = 1;
		int rows = 0;
		try(Connection con = ConnectionFactory.getConnection()){		
			try(PreparedStatement ps = con.prepareStatement(sql)) {				
					if(params!= null && !params.isEmpty()) {
						for(Object obj: params) {
							ps.setObject(index, obj);
							index++;
						}
					  rows = ps.executeUpdate();
					}
			}
			return rows;
		} catch (SQLException e) {
			throw new PersistanceException(e);
		}
	}
	
	
	
	/**
	 * The encrypt method encrypts the password by using the encrypt environment
	 * @param password
	 * @return
	 */
	public default String encrypt(String password) {		
		try {
			byte[] encryptedPassword = CryptoUtil.encryptAES(SECRET_KEY, password.getBytes());
			
			byte[] base64Bytes = Base64.getEncoder().encode(encryptedPassword);
			
			return new String(base64Bytes);
		} catch (Exception e) {
			throw new CryptoException(e);
		}
	}
	
	
	/**
	 * The encrypt method decrypts the password by using the encrypt environment
	 * @param password
	 * @return
	 */
	public default String decrypt(String password) {
		
		try {
			byte[] base64Bytes = password.getBytes();
			
			byte[] encryptedPassword = Base64.getDecoder().decode(base64Bytes);
			
			byte[] data = CryptoUtil.decryptAES(SECRET_KEY, encryptedPassword);
			
			return new String(data);
			
		} catch (CryptoException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * The personTable creates a person table into the database.
	 * @return
	 */
	public static String personTable() {		
		StringBuilder sb = new StringBuilder();		
		sb.append("create table if not exists person ("
				+ "id  int auto_increment primary key,"
				+ "username varchar(40) not null,"
				+ "password varchar(180) not null,"
				+ "location varchar(150) not null,"
				+ "lang varchar(40) not null,"
				+ "backup boolean null,"
				+ "logged boolean null);");				
		return sb.toString();
	} 
	
	
	/**
	 * the pinTable creates a pin table into the database.
	 * @return
	 */
	public static String pinTable() {
		StringBuilder sb = new StringBuilder();		
		sb.append("create table if not exists pin ("
				+ "id  int auto_increment primary key,"
				+ "title varchar(40) not null,"
				+ "username varchar(40) not null,"
				+ "password varchar(80) not null,"
				+ "link varchar(150) not null,"
				+ "notes varchar(255),"
				+ "person_id int,"
				+ "foreign key (person_id) references person (id));");		
		return sb.toString();
	}
	
	
		
}
