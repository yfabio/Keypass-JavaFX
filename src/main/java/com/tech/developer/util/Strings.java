package com.tech.developer.util;



import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;
import com.tech.developer.domain.Person;
import com.tech.developer.domain.Pin;
import com.tech.developer.domain.ValidationState;
import com.tech.developer.domain.ValidationState.Field;
import static java.nio.file.FileSystems.getDefault;

/**
 * The Strings keeps all the work regard about strings as weel as validation.
 * @author yfabio
 *
 */
public class Strings {

	private static ResourceBundle bundle = ResourceBundles.getResouce();
	
	public static boolean isEmpty(String value) {
		if (value == null) {
			return true;
		}
		return value.trim().length() == 0;
	}

	public static boolean passwordLength(String value) {
		return isEmpty(value) ? false : value.length() > 16;
	}

	public static String getCurrentDate() {
		DateTimeFormatter df = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG);
		ZonedDateTime today = ZonedDateTime.now();
		return df.format(today);
	}

	public static String getCurrentUser(Person person) {
		return String.format("%s %s", bundle.getString("label-logged-user"), person.getUsername());
	}

	public static String getBundleValue(String key) {
		return bundle.getString(key);
	}
	
	public static String getDatabasePath() {
		StringBuilder sb = new StringBuilder();		
		sb.append(System.getProperty("user.home"))
			.append(getDefault().getSeparator())
			.append("keypass")
			.append(getDefault().getSeparator())
			.append("data");		
		return sb.toString();
	}
	
	
	

	public static boolean validate(Person person, ValidationState state) {
		
		boolean result = true;

		if (isEmpty(person.getUsername())) {
			Field field = Field.USERNAME;
			field.setMessage(bundle.getString("alert-validation-username"));
			state.change(field);
			result = false;
		} 
		if (isEmpty(person.getPassword())) {
			Field field = Field.PASSWORD;
			field.setMessage(bundle.getString("alert-validation-password"));
			state.change(field);
			result = false;
		} 
		if (passwordLength(person.getPassword())) {
			Field field = Field.PASSWORD;
			field.setMessage(bundle.getString("alert-validation-password-length"));
			state.change(field);
			result = false;
		} 
		if (isEmpty(person.getLocation())) {
			Field field = Field.LOCATION;
			field.setMessage(bundle.getString("alert-validation-location"));
			state.change(field);
			result = false;
		}

		return result;
	}

	public static boolean validate(Pin pin, ValidationState state) {	
		
		boolean result = true;
		
		if (isEmpty(pin.getTitle())) {
			Field field = Field.TITLE;
			field.setMessage(bundle.getString("alert-validation-title"));
			state.change(field);
			result = false;
		}
		
		if (isEmpty(pin.getUsername())) {
			Field field = Field.USERNAME;
			field.setMessage(bundle.getString("alert-validation-username"));
			state.change(field);
			result = false;
		}
		
		if (isEmpty(pin.getPassword())) {
			Field field = Field.PASSWORD;
			field.setMessage(bundle.getString("alert-validation-password"));
			state.change(field);
			result = false;
		}
		
		if (passwordLength(pin.getPassword())) {
			Field field = Field.PASSWORD;
			field.setMessage(bundle.getString("alert-validation-password-length"));
			state.change(field);
			result = false;
		}
		
		if (isEmpty(pin.getLink())) {
			Field field = Field.LINK;
			field.setMessage(bundle.getString("alert-validation-link"));
			state.change(field);
			result = false;
		}
		
		return result;
	}
	
	
	
	
	

}
