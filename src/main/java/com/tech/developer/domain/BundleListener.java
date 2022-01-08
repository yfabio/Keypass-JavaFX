package com.tech.developer.domain;

/**
 * The BundlerLister is used to notify the controller 
 * @author yfabio
 *
 */
public interface BundleListener extends StageAwareController {
	public void putExtra(Bundle bundle);
}
