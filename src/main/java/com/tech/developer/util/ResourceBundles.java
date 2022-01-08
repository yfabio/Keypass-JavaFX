package com.tech.developer.util;

import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The ResouceBundles is used to get an instance of ResouceBundle regardles of the selected language
 * @author yfabio
 *
 */
public class ResourceBundles {
		
	public static ResourceBundle getResouce()  {		
		String lang = Dictionary.getLang();
		Locale locale = Locale.forLanguageTag(lang);		
		return ListResourceBundle.getBundle("properties/application",locale);
	}
	
	
	
	
}
