package com.tech.developer.backup;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.tech.developer.crypto.CryptoUtilCallback;
import com.tech.developer.domain.Person;
import com.tech.developer.domain.Pin;
import com.tech.developer.util.JSONConverter;

import javafx.collections.ObservableList;

/**
 * 
 * The FIleBackup is used to actually perform the backup in the background
 * 
 * @author yfabio
 *
 */
public class FileBackup extends FileTaskBackup<Void>{
	
	private FileStreamCallBacks fileStreamCallBacks;
	
	private CryptoUtilCallback cryptoUtilCallback;
	
	private ObservableList<Pin> pins;
	
	private Person person;

	
	/**
	 * 
	 * The constructor taskes a FileStreamCallBacks, CryptoUtilCallBack, ObservableList and a person.
	 * 
	 * @param fileStreamCallBacks
	 * @param cryptoUtilCallback
	 * @param pins
	 * @param person
	 */
	public FileBackup(FileStreamCallBacks fileStreamCallBacks, CryptoUtilCallback cryptoUtilCallback,ObservableList<Pin> pins, Person person) {
		this.fileStreamCallBacks = fileStreamCallBacks;	
		this.cryptoUtilCallback = cryptoUtilCallback;
		this.person = person;
		this.pins = pins;
	}

	@Override
	public void backup() {		
		
		Path dir = Paths.get(person.getLocation(),person.getUsername().concat(".txt"));
				
		try(BufferedWriter bw = Files.newBufferedWriter(dir,StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING)){
				
			for (int i = 0; i < pins.size(); i++) {
				
				Pin pin = pins.get(i);
				
				String jsonStr = JSONConverter.convertPinAsString(pin, cryptoUtilCallback);

				bw.write(jsonStr);
				bw.flush();
				bw.newLine();		
				
				Thread.sleep(10);
							
			}
			
			
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
		
	}
	
	@Override
	protected void succeeded() {	
		this.fileStreamCallBacks.succeeded();
	}

	@Override
	protected void failed() {		
		this.fileStreamCallBacks.failed();
	}
	
	@Override
	protected void scheduled() {
		fileStreamCallBacks.scheduled();
	}
	
		
	
}
