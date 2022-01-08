package com.tech.developer.account;

import static com.tech.developer.util.Strings.getBundleValue;

import java.io.IOException;

import org.kordamp.bootstrapfx.BootstrapFX;

import com.tech.developer.dialog.DialogManager;
import com.tech.developer.domain.Person;
import com.tech.developer.domain.StageAwareController;
import com.tech.developer.domain.ValidationState;
import com.tech.developer.persistance.DAOPerson;
import com.tech.developer.persistance.PersistanceException;
import com.tech.developer.util.FXMLUtil;
import com.tech.developer.util.FXMLUtil.Layout;
import com.tech.developer.util.ResourceBundles;
import com.tech.developer.util.Strings;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class ControllerAccount implements ValidationState, StageAwareController {

	 	@FXML
	    private TextField mUsername;

	    @FXML
	    private PasswordField mPassword;

	    @FXML
	    private Button mSaveButton;

	    @FXML
	    private Button mCancelButton;

	    @FXML
	    private Label title;
	    
	    private Person person = new Person();
	    
	    private DAOPerson persondao;

		private Stage stage;
	    	      
	    
	    @FXML
	    public void initialize() {
	    		    	
	    	persondao = DAOPerson.getInstance();		    	
	    	person.usernameProperty().bind(mUsername.textProperty());
	    	person.passwordProperty().bind(mPassword.textProperty());	    	    	
	    	setInvalidationListener(mUsername,mPassword);    	
	    	
	    }
	    

	    @FXML
	    void onCancelHandler(ActionEvent event) {
	    	backward(event);
	    }

	    @FXML
	    void onSaveHandler(ActionEvent event) {
	    	
	    	try {
				if(Strings.validate(person,this)) {					
					persondao.insert(person);						
					clear();
					DialogManager.dialogInfo(getBundleValue("alert-account-title"),getBundleValue("alert-account-content"));
					backward(event);
				}
			} catch (PersistanceException e) {
				 DialogManager.dialogError(getBundleValue("alert-account-title"),getBundleValue("alert-head-error"), e.getMessage());
			}
	    	

	    }
	    
	    public void clear() {
	    	mUsername.clear();
	    	mPassword.clear();
	    }


		@Override
		public void change(Field field) {			
			if(Field.USERNAME == field) {
				mUsername.getStyleClass().add("border-error");	
				showToolTip(stage, mUsername, field.getMessage());
			}
			
			if(Field.PASSWORD == field) {
				mPassword.getStyleClass().add("border-error");
				showToolTip(stage, mPassword, field.getMessage());
			}			
		}

		
		
		
		private void backward(ActionEvent event) {			
			
			Stage stage = event.getSource() instanceof Button ?	(Stage) ((Button) event.getSource()).getScene().getWindow() :
				(Stage) ((PasswordField) event.getSource()).getScene().getWindow();                                                                                                              
			    	
		    	try {
		    		
		    		Pane root = FXMLUtil.load(stage, Layout.LOGIN,ResourceBundles.getResouce());              
								
					Scene scene = new Scene(root);
					scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
					
					stage.setScene(scene);
					stage.setTitle("Login");
					stage.centerOnScreen();
					stage.setResizable(false);
					stage.show();
									
				} catch (IOException e) {
					DialogManager.dialogError(getBundleValue("alert-account-title"),getBundleValue("alert-head-error"),e.getMessage());
				}		    	
		    	
		}
		
		
		private void setInvalidationListener(TextInputControl...fields) {
			for (TextInputControl field : fields) {
				field.textProperty().addListener((e) -> {
		    		if(field.getStyleClass().contains("border-error")) {
		    			field.getStyleClass().remove("border-error");
		    		}
		    	});
			}
		}


		@Override
		public void setStage(Stage stage) {
			this.stage = stage;			
		}
		
	
}
