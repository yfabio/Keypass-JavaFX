package com.tech.developer.domain;

import javafx.stage.Stage;

/** 
 *  The StageAwareControlle is used to send the stage to the controller that has been implemented it
 * @author yfabio
 *
 */
public interface StageAwareController {	
	public void setStage(Stage stage);	
}


