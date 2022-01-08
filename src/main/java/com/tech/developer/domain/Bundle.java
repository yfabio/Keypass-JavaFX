package com.tech.developer.domain;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * The Bundle is used to store data to be sent from one controller to another. It is a simple way to send data between controlles
 * @author yfabio
 *
 */
public final class Bundle {
		
	private  static Map<String, Serializable> map = new LinkedHashMap<>();
		
	
	/**
	 * 
	 * The putExtra is used to add the data and return an instance of itself so that it can be used as a fluent api
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Bundle putExtra(String key,Serializable value) {
		map.put(key, value);
		return this;
	}
	
	/**
	 * The getObject is used to retrieve the data that is previous inserted
	 * @param key
	 * @return
	 */
	public Serializable getObject(String key) {
		 return map.get(key);
	}
	
	public static Map<String, Serializable> getMap() {
		return map;
	}

}
