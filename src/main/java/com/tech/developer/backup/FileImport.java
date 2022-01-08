package com.tech.developer.backup;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.tech.developer.crypto.CryptoUtilCallback;
import com.tech.developer.domain.Pin;
import com.tech.developer.util.JSONConverter;


/**
 * 
 * The FileImport is used to perform the importation in the background
 * 
 * @author yfabio
 *
 */
public class FileImport extends FileTaskImported<List<Pin>> {

	private FileStreamCallBacks fileStreamCallBacks;
	
	private CryptoUtilCallback cryptoUtilCallback;

	private File file;
	
	/**
	 * The constructor takes a FileStreamCallBacks ,CryptoUtilCallback and File 
	 * 
	 * @param fileStreamCallBacks
	 * @param cryptoUtilCallback
	 * @param file
	 */
	public FileImport(FileStreamCallBacks fileStreamCallBacks,CryptoUtilCallback cryptoUtilCallback, File file) {
		this.fileStreamCallBacks = fileStreamCallBacks;
		this.cryptoUtilCallback = cryptoUtilCallback;
		this.file = file;
	}

	@Override
	public List<Pin> imported() {
	
		List<Pin> pins = new ArrayList<>();
		
		Path dir = file.toPath();
		
							
		try {
			
			List<String> lines = Files.readAllLines(dir);
			
			
			for (int i = 0; i < lines.size(); i++) {
				
				String line = lines.get(i);
				
				Pin pin = JSONConverter.convertStringAsPin(line, cryptoUtilCallback);
								
				pins.add(pin);
							
				Thread.sleep(10);
				
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		

		return pins;
	}

	@Override
	public void backup() {
	}

	@Override
	protected void succeeded() {
		fileStreamCallBacks.succeeded();
	}

	@Override
	protected void failed() {
		fileStreamCallBacks.failed();
	}
	
	@Override
	protected void scheduled() {
		fileStreamCallBacks.scheduled();
	}


	

}
