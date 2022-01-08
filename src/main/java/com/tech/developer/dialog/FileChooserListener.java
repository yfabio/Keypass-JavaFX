package com.tech.developer.dialog;

import java.io.File;


/**
 * 
 * The FileChooseListener is used to notify an instance about a filechoose has been opened
 * 
 * @author yfabio
 *
 */
public interface FileChooserListener {
	public void onFileChooser(File file);
}
