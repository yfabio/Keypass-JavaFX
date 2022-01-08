package com.tech.developer.backup;

/**
 * 
 * The FileStream is used either to backup the data but also to import 
 * 
 * @author yfabio
 *
 * @param <E>
 * 
 */
public interface FileStream<E>  {	
	public void backup();
	public E imported(); 	 
}
