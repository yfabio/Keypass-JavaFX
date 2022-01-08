package com.tech.developer.backup;

/**
 * 
 * The FileStreamCallBacks is used to notify about a fail, succeed and an scheduled
 * 
 * @author yfabio
 *
 */
public interface FileStreamCallBacks {
	
	public void failed();

	public void succeeded();

	public void scheduled();

}
