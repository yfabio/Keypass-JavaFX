package com.tech.developer.preference;

import static com.tech.developer.util.Strings.getBundleValue;

import java.io.File;
import java.util.List;
import java.util.Locale;

import com.tech.developer.dialog.DialogManager;
import com.tech.developer.domain.Bundle;
import com.tech.developer.domain.BundleListener;
import com.tech.developer.domain.Person;
import com.tech.developer.domain.ReloadListener;
import com.tech.developer.domain.ValidationState;
import com.tech.developer.persistance.DAOPerson;
import com.tech.developer.persistance.PersistanceException;
import com.tech.developer.persistance.SQLCriteriaFactory;
import com.tech.developer.util.Dictionary;
import com.tech.developer.util.Strings;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class ControllerPreference implements ValidationState, BundleListener {
	
	
	@FXML
	private TextField mLocation;

	@FXML
	private PasswordField mPassword;

	@FXML
	private TextField mUsername;

	@FXML
	private Label mLabelSecret;

	@FXML
	private Label mLabelPassword;

	@FXML
	private Button mSaveButton;

	@FXML
	private Button mCancelButton;

	@FXML
	private ToggleButton mShowButton;

	@FXML
	private CheckBox mBackup;

	@FXML
	private RadioButton mPortuguese;

	@FXML
	private RadioButton mEnglish;

	private ToggleGroup group = new ToggleGroup();

	private ReloadListener listener;

	private DAOPerson persondao;

	private Person person;

	private Stage stage;

	

	@FXML
	public void initialize() {

		persondao = DAOPerson.getInstance();

		try {
			String current = Dictionary.getCurrent();
			if (!Strings.isEmpty(current)) {
				person = persondao.get(SQLCriteriaFactory.getPersonByUsername(), List.of(current)).stream().findFirst().orElse(new Person());
				mUsername.textProperty().bindBidirectional(person.usernameProperty());
				mPassword.textProperty().bindBidirectional(person.passwordProperty());
				mLocation.textProperty().bindBidirectional(person.locationProperty());
				mBackup.selectedProperty().bindBidirectional(person.backupProperty());
			
			}

		} catch (PersistanceException e) {
			DialogManager.dialogError("preference", getBundleValue("alert-head-error"), e.getMessage());
		}

		setInvalidationListener(mUsername, mPassword, mLocation);

		mEnglish.setToggleGroup(group);
		mPortuguese.setToggleGroup(group);

		if (which(person.getLang()))
			mEnglish.setSelected(true);
		else
			mPortuguese.setSelected(true);

		group.selectedToggleProperty().addListener((obs, older, newer) -> {

			RadioButton selected = (RadioButton) newer.getToggleGroup().getSelectedToggle();

			String radioID = selected.getId();

			Locale locale = Locale.getDefault();

			if (radioID.equalsIgnoreCase("mEnglish")) {
				locale = Locale.CANADA;
			} else {
				locale = Locale.forLanguageTag("pt-BR");
			}

			if (locale != null) {
				person.setLang(locale.toLanguageTag());
				DialogManager.dialogInfo(getBundleValue("label-preference"),
						getBundleValue("alert-preference-content-language-changed"));
			}

		});

	}

	@FXML
	void onBrowserHandler(ActionEvent event) {
		DirectoryChooser dirChooser = new DirectoryChooser();
		dirChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		File file = dirChooser.showDialog(stage);
		if (file != null) {
			String path = file.getAbsolutePath();
			mLocation.setText(path);
		}
	}

	@FXML
	void onCancelHandler(ActionEvent event) {
		stage.close();
	}

	@FXML
	void onSaveHandler(ActionEvent event) {

		try {

			if (person != null && Strings.validate(person, this) && listener != null) {

				persondao.update(person);
				Dictionary.setProperty("logged", person.getUsername());
				Dictionary.setProperty("current", person.getUsername());
				DialogManager.dialogInfo("preference", getBundleValue("alert-preference-head"),	getBundleValue("alert-preference-updated-content"));				
				listener.reload();
				stage.close();
			}

		} catch (PersistanceException e) {
			DialogManager.dialogError(Strings.getBundleValue("preference"), getBundleValue("alert-head-error"),
					e.getMessage());
		}

	}

	@Override
	public void change(Field field) {
		if (Field.USERNAME == field) {
			mUsername.getStyleClass().add("border-error");
			showToolTip(stage, mUsername, field.getMessage());
		}
		
		if (Field.PASSWORD == field) {
			mPassword.getStyleClass().add("border-error");
			showToolTip(stage, mPassword, field.getMessage());
		}
		
		if (Field.LOCATION == field) {
			mLocation.getStyleClass().add("border-error");
			showToolTip(stage, mLocation, field.getMessage());
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

	@Override
	public void putExtra(Bundle bundle) {
		if (bundle != null)
			listener = (ReloadListener) bundle.getObject("reload");
	}

	@Override
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public boolean which(String lang) {
		Locale locale = Locale.forLanguageTag(lang);
		return locale.getDisplayCountry().equalsIgnoreCase("canada");
	}

	

	

}
