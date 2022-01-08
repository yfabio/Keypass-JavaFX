package com.tech.developer.domain;

import java.io.Serializable;

/**
 * The ReloadListener  is used to notify the controlle about something happen 
 * @author yfabio
 *
 */
public interface ReloadListener extends Serializable {
	public void reload();
}
