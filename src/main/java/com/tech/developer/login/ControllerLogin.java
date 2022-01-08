package com.tech.developer.login;

import static com.tech.developer.util.Strings.getBundleValue;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.kordamp.bootstrapfx.BootstrapFX;

import com.tech.developer.dialog.DialogManager;
import com.tech.developer.domain.Person;
import com.tech.developer.domain.StageAwareController;
import com.tech.developer.domain.ValidationState;
import com.tech.developer.persistance.DAO;
import com.tech.developer.persistance.DAOPerson;
import com.tech.developer.persistance.PersistanceException;
import com.tech.developer.persistance.SQLCriteriaFactory;
import com.tech.developer.util.Dictionary;
import com.tech.developer.util.FXMLUtil;
import com.tech.developer.util.FXMLUtil.Layout;
import com.tech.developer.util.ResourceBundles;
import com.tech.developer.util.Strings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ControllerLogin implements StageAwareController, ValidationState {

	@FXML
	private TextField mUsername;

	@FXML
	private PasswordField mPassword;

	@FXML
	private Button mLoginButton;

	@FXML
	private Button mCancelButton;

	@FXML
	private CheckBox mLogged;

	@FXML
	private Label title;

	private Person person = new Person();

	private DAO<Person> persondao;
	

	private Stage loginStage;

	@FXML
	public void initialize() {

		persondao = DAOPerson.getInstance();
						
		try {			
			String logged = Dictionary.getLogged();
			if (!Strings.isEmpty(logged)) {			
					person = persondao.get(SQLCriteriaFactory.getPersonByUsername(), List.of(logged)).stream().findFirst().orElse(new Person());
					person.setPassword("");			
			}		
		} catch (PersistanceException e) {
			DialogManager.dialogError(getBundleValue("alert-login-title"),getBundleValue("alert-head-error"), e.getMessage());
		}

		mUsername.textProperty().bindBidirectional(person.usernameProperty());
		mPassword.textProperty().bindBidirectional(person.passwordProperty());
		mLogged.selectedProperty().bindBidirectional(person.loggedProperty());
		setInvalidationListener(mUsername, mPassword);
		
	}

	@FXML
	public void onCancelHandler(ActionEvent event) {
		mUsername.clear();
		mPassword.clear();
		mLogged.setSelected(false);
	}

	@FXML
	public void onLoginHandler(ActionEvent event) {
		inflate();
	}

	@FXML
	public void onPasswordHandler(ActionEvent event) {
		inflate();
	}


	@FXML
	public void onRegisterHandler(ActionEvent event) {

		Stage stage = (Stage) ((Hyperlink) event.getSource()).getScene().getWindow();

		try {
			Pane root = FXMLUtil.load(stage, Layout.ACCOUNT,ResourceBundles.getResouce());

			Scene scene = new Scene(root);
			scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
			stage.setScene(scene);
			stage.setTitle(getBundleValue("hyperlink"));
			stage.centerOnScreen();
			stage.setResizable(false);
			stage.show();

		} catch (IOException e) {
			DialogManager.dialogError(getBundleValue("alert-login-title"),getBundleValue("alert-head-error"), e.getMessage());
		}

	}

	private void inflate() {
		try {

			if (Strings.validate(person, this)) {

				Optional<Person> op = persondao.get(SQLCriteriaFactory.getPersonByPasswordAndUsername(),
						List.of(persondao.encrypt(mPassword.getText()),mUsername.getText().toUpperCase())).stream().findFirst();

				try {

					if (op.get() != null) {

						Person p = op.get();

						if (person.isLogged()) {
							p.setLogged(person.isLogged());
							persondao.update(p);							
							Dictionary.setProperty("logged", person.getUsername());
						} else {
							Dictionary.setProperty("logged", "");
						}

						Stage stage = new Stage();
							
						Dictionary.setProperty("current",person.getUsername());

						Pane root = FXMLUtil.load(stage, Layout.MAIN,ResourceBundles.getResouce());

						Scene scene = new Scene(root);
						scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

						stage.setScene(scene);
						stage.setTitle(getBundleValue("alert-main-title"));
						stage.centerOnScreen();
						stage.setResizable(false);						
						stage.show();
						loginStage.close();
					}

				} catch (NoSuchElementException e) {
					DialogManager.dialogError(getBundleValue("alert-login-title"),getBundleValue("alert-head-error"), e.getMessage());
					
				}

			}

		} catch (Exception e) {
			DialogManager.dialogError(getBundleValue("alert-login-title"),getBundleValue("alert-head-error"), e.getMessage());			
		}
	}

	@Override
	public void setStage(Stage stage) {
		this.loginStage = stage;
	}

	@Override
	public void change(Field field) {
		if(Field.USERNAME == field) {
			mUsername.getStyleClass().add("border-error");
			showToolTip(loginStage, mUsername,field.getMessage());
		} 
			
		if(Field.PASSWORD == field) {
			mPassword.getStyleClass().add("border-error");
			showToolTip(loginStage, mPassword,field.getMessage());
		}
	}

	

	private void setInvalidationListener(TextInputControl... fields) {
		for (TextInputControl field : fields) {
			field.textProperty().addListener(e -> {
				if (field.getStyleClass().contains("border-error")) {
					field.getStyleClass().remove("border-error");
				}
			});
		}
	}
			
}
