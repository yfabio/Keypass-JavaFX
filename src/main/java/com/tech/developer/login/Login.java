package com.tech.developer.login;

import static com.tech.developer.util.Strings.getBundleValue;

import java.io.IOException;

import org.kordamp.bootstrapfx.BootstrapFX;

import com.tech.developer.dialog.DialogManager;
import com.tech.developer.persistance.DAO;
import com.tech.developer.util.Dictionary;
import com.tech.developer.util.FXMLUtil;
import com.tech.developer.util.FXMLUtil.Layout;
import com.tech.developer.util.ResourceBundles;
import com.tech.developer.util.Strings;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Login extends Application  {
	
		
	@Override
	public void start(Stage stage) {

		try {			
			Pane root = FXMLUtil.load(stage,Layout.LOGIN,ResourceBundles.getResouce());		
			Scene scene = new Scene(root);	
			scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
			stage.setTitle(getBundleValue("alert-login-title"));
			Image icon = new Image(getClass().getResourceAsStream("/images/key.png"));
			stage.getIcons().add(icon);
			stage.centerOnScreen();
			stage.setResizable(false);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {	
			DialogManager.dialogError(getBundleValue("alert-login-title"),Strings.getBundleValue("alert-head-error"),e.getMessage());
		}

	}

	@Override
	public void init() throws Exception {	
		DAO.create();		
	}

	@Override
	public void stop() throws Exception {		
		try {
			Dictionary.setProperty("current","");
		} catch (Exception e) {
			DialogManager.dialogError("Login",	Strings.getBundleValue("alert-head-error"),e.getMessage());
		}
	}
	
}
