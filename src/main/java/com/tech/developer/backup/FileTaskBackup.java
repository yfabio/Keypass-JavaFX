package com.tech.developer.backup;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 *  The FIleTaskBackup holds a task that will be performed when the backup is fired
 * @author yfabio
 *
 * @param <E>
 */
public abstract class FileTaskBackup<E> extends Service<E> implements FileStream<E> {

	@Override
	public void backup() {		
	}

	@Override
	public E imported() {	
		return null;
	}

	@Override
	protected Task<E> createTask() {
		Task<E> task = new Task<>() {
			@Override
			protected E call() throws Exception {
				backup();
				return null;
			}
		};
		return task;
	}

	
	

	

	
	
	
	
}
