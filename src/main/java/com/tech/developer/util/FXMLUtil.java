package com.tech.developer.util;

import java.io.IOException;
import java.util.ResourceBundle;
import com.tech.developer.domain.Bundle;
import com.tech.developer.domain.BundleListener;
import com.tech.developer.domain.StageAwareController;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public abstract class FXMLUtil {
	
	
	/**
	 * 
	 * @author yfabio
	 *  Layout is used for creating the specific layout
	 */
	public enum Layout{
		
		LOGIN("/fxml/login_ui.fxml"),
		MAIN("/fxml/main_ui.fxml"),
		FORM("/fxml/form_ui.fxml"),
		ACCOUNT("/fxml/account_ui.fxml"),
		PREFERENCE("/fxml/pref_ui.fxml");
		
		String url;

		private Layout(String url) {
			this.url = url;
		}
		
		public String getUrl() {
			return url;
		}
		
	}
	
	
	/**
	 * 
	 * @param <T>
	 * @param stage
	 * @param Layout value
	 * @param ResourceBundle bundle
	 * @return  The load will not only notify the controller that implements either StageAwareController or BundleLister 
	 * but also returns the Pane container layout.
	 * @throws IOException
	 *  
	 */
	public static <T> T load(Stage stage,Layout value,ResourceBundle bundle) throws IOException {
		FXMLLoader loader = new FXMLLoader(FXMLUtil.class.getResource(value.getUrl()),bundle);
		T root = loader.load();		
		Object controller = loader.getController();		
		if(controller != null && controller instanceof StageAwareController) {
			StageAwareController stageAware = (StageAwareController) controller;
			stageAware.setStage(stage);			
		}		
		return root;
	}
	
	
	/**
	 * 
	 * @param <T>
	 * @param stage
	 * @param Layout value
	 * @param ResourceBundle bundle
	 * @return  The load will not only notify the controller that implements either StageAwareController or BundleLister 
	 * but also returns the Pane container layout.
	 * @throws IOException
	 *  
	 */
	public static <T> T load(Stage stage,Layout value,ResourceBundle bundle, Bundle extras) throws IOException {
		FXMLLoader loader = new FXMLLoader(FXMLUtil.class.getResource(value.getUrl()),bundle);
		T root = loader.load();		
		Object controller = loader.getController();		
		if(controller != null && controller instanceof BundleListener) {
				BundleListener bundleListener = (BundleListener) controller;
				bundleListener.putExtra(extras);
				bundleListener.setStage(stage);
		}		
		return root;
	}
	
	
	
	
}
