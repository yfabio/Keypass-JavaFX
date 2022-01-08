package com.tech.developer.backup;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.tech.developer.domain.Person;
import com.tech.developer.domain.Pin;

public class FileExport extends FileTaskBackup<Void> { 
	
	private FileStreamCallBacks fileStreamCallBacks;
	
	private List<Pin> pins;
	
	private Person person;
	
	
	public FileExport(FileStreamCallBacks fileStreamCallBacks,List<Pin> pins , Person person) {
		this.fileStreamCallBacks = fileStreamCallBacks;			
		this.person = person;
		this.pins = pins;
	}

	
	@Override
	public void backup() {		
		
		Path dir = Paths.get(person.getLocation(),"keypass.sql");
		
				
		try(BufferedWriter bw = Files.newBufferedWriter(dir,StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING)){
				
			for (int i = 0; i < pins.size(); i++) {
				
				Pin pin = pins.get(i);
		        
				
			    var date = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
			    var time = LocalTime.now().format(DateTimeFormatter.ISO_TIME);
				var today = String.join(" ", date,time);
			    
				
				String sql = MessageFormat.format("INSERT INTO Services (Id,Title,Username,Password,Created,Comment,AccountId)"
						+ " VALUES ({0},{1},{2},{3},{4},{5},{6});",
						pin.getId(),
						"'" + pin.getTitle() + "'",
						"'"+pin.getUsername()+"'",
						"'"+pin.getPassword()+"'",
						"'"+today+"'",
						"'"+pin.getNotes()+"'",
						person.getId());
				
				bw.write(sql);
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
