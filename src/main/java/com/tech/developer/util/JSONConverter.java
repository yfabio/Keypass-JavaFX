package com.tech.developer.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.tech.developer.crypto.CryptoUtilCallback;
import com.tech.developer.domain.Person;
import com.tech.developer.domain.Pin;

public class JSONConverter {
	
	private static Gson gson = new Gson();
	
	private static class Holder {		
		public int id;
		public String title;
		public String username;
		public String password;
		public String link;
		public String notes;
		public int person_id;		
	}
	
	
	
	
	public static String convertPinAsString(Pin pin,CryptoUtilCallback cryptoUtilCallback) {
		
		JsonObject jo = new JsonObject();
		
		jo.add("id", new JsonPrimitive(pin.getId()));
		jo.add("title", new JsonPrimitive(pin.getTitle()));
		jo.add("username", new JsonPrimitive(pin.getUsername()));		
		jo.add("password", new JsonPrimitive(pin.getPassword()));		
		jo.add("link",new JsonPrimitive(pin.getLink()));
		jo.add("notes", new JsonPrimitive(pin.getNotes()));
		jo.add("person_id", new JsonPrimitive(pin.getPerson().getId()));
		
		return  gson.toJson(jo);
	
		
	}
	
	
	public static Pin convertStringAsPin(String line, CryptoUtilCallback cryptoUtilCallback) {
		
		Holder holder = gson.fromJson(line, Holder.class);
		
		Pin pin = new Pin();
		
		pin.setId(holder.id);
		pin.setTitle(holder.title);				
		pin.setUsername(holder.username);
						
		String decrypted = cryptoUtilCallback.decryptValue(holder.password);
		
		pin.setPassword(decrypted);				
		
		pin.setLink(holder.link);
		pin.setNotes(holder.notes);
		Person person = new Person();
		person.setId(holder.person_id);
		pin.setPerson(person);
				
		return pin;
	}
	

}
