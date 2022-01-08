package com.tech.developer.persistance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.tech.developer.util.Dictionary;
import com.tech.developer.util.Strings;

/**
 * 
 * The ConnectionFactory is used to communicate with the database, any time is
 * called a new instance is given to the caller.
 * 
 * @author yfabio
 *
 */
public final class ConnectionFactory {

	public static Connection getConnection() throws PersistanceException {

		try {
			String url = Dictionary.getURL();

			String format = Strings.getDatabasePath();

			url = url.concat(format);
			String username = Dictionary.getUsername();
			String password = Dictionary.getPassword();

			return DriverManager.getConnection(url, username, password);

		} catch (SQLException e) {
			throw new PersistanceException(e);
		}

	}

}
