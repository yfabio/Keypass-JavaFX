package com.tech.developer.form;

import static com.tech.developer.util.Strings.getBundleValue;

import java.util.List;
import java.util.stream.Stream;

import com.tech.developer.dialog.DialogManager;
import com.tech.developer.domain.Bundle;
import com.tech.developer.domain.BundleListener;
import com.tech.developer.domain.Person;
import com.tech.developer.domain.Pin;
import com.tech.developer.domain.ReloadListener;
import com.tech.developer.domain.ValidationState;
import com.tech.developer.persistance.DAOPerson;
import com.tech.developer.persistance.DAOPin;
import com.tech.developer.persistance.PersistanceException;
import com.tech.developer.persistance.SQLCriteriaFactory;
import com.tech.developer.util.Dictionary;
import com.tech.developer.util.Strings;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.stage.Stage;

public class ControllerForm implements BundleListener, ValidationState {

	@FXML
	private Button mSaveButton;

	@FXML
	private Button mCancelButton;

	@FXML
	private TextField mTextTitle;

	@FXML
	private TextField mTextUsername;

	@FXML
	private TextField mTextLink;

	@FXML
	private TextArea mTextNotes;

	@FXML
	private PasswordField mTextPassword;

	private Person person;

	private Pin pin;

	private DAOPin pindao;

	private DAOPerson persondao;

	private ReloadListener listener;
	
	private Stage stage;
	
	private static StringBuilder holder = new StringBuilder();

	@FXML
	public void initialize() {

		pindao = DAOPin.getInstance();
		persondao = DAOPerson.getInstance();

		setInvalidationListener(mTextTitle, mTextUsername, mTextPassword, mTextLink);

		try {
			String current = Dictionary.getCurrent();
			if (!Strings.isEmpty(current)) {
				person = persondao.get(SQLCriteriaFactory.getPersonByUsername(), List.of(current)).stream().findFirst()
						.orElseThrow();
			}
		} catch (PersistanceException e) {
			DialogManager.dialogError("Form", getBundleValue("alert-head-error"), e.getMessage());
		}
		
		

		if (person.getId() != null)
			Platform.runLater(() -> mTextTitle.requestFocus());
	}

	@FXML
	public void onCancelHandler(ActionEvent event) {
		if(this.stage!=null) {
			mTextPassword.setText(holder.toString());
			holder.delete(0,holder.length());
			stage.close();
		}		
	}

	@FXML
	public void onSaveHandler(ActionEvent event) {

		Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

		try {
			if (validateParties()) {
				if (pin.getId() == null) {
					pin.setPerson(person);
					pindao.insert(pin);
					DialogManager.dialogInfo("Form", getBundleValue("alert-saved-sucess-head"),
							getBundleValue("alert-saved-sucess-content"));
					clear(mTextTitle, mTextUsername, mTextPassword, mTextLink, mTextNotes);
					listener.reload();
					stage.close();
				} else {
					pin.setPerson(person);
					pindao.update(pin);
					DialogManager.dialogInfo("Form", getBundleValue("alert-updated-sucess-head"),
							getBundleValue("alert-updated-sucess-content"));
					clear(mTextTitle, mTextUsername, mTextPassword, mTextLink, mTextNotes);
					listener.reload();
					stage.close();
				}
			}
		} catch (PersistanceException e) {
			DialogManager.dialogError("Form", getBundleValue("alert-head-error"), e.getMessage());
		}

	}

	@Override
	public void putExtra(Bundle bundle) {
		if (bundle != null) {
			pin = (Pin) bundle.getObject("pin");
			listener = (ReloadListener) bundle.getObject("reload");				
			if(!Strings.isEmpty(pin.getPassword())) {
				holder.append(pin.getPassword());
				pin.setPassword(pindao.decrypt(pin.getPassword()));
			}
			bind();			
		}
	}

	private void bind() {
		if (pin != null) {
			mTextTitle.textProperty().bindBidirectional(pin.titleProperty());
			mTextUsername.textProperty().bindBidirectional(pin.usernameProperty());
			mTextPassword.textProperty().bindBidirectional(pin.passwordProperty());
			mTextLink.textProperty().bindBidirectional(pin.linkProperty());
			mTextNotes.textProperty().bindBidirectional(pin.notesProperty());
		}
	}

	private void unbind() {
		if (pin != null) {
			mTextTitle.textProperty().unbindBidirectional(pin.titleProperty());
			mTextUsername.textProperty().unbindBidirectional(pin.usernameProperty());
			mTextPassword.textProperty().unbindBidirectional(pin.passwordProperty());
			mTextLink.textProperty().unbindBidirectional(pin.linkProperty());
			mTextNotes.textProperty().unbindBidirectional(pin.notesProperty());
		}
	}

	private void clear(TextInputControl... fields) {
		unbind();
		Stream.of(fields).forEach(e -> e.clear());
	}

	@Override
	public void change(Field field) {
		if (Field.TITLE == field) {
			mTextTitle.getStyleClass().add("border-error");
			showToolTip(stage, mTextTitle, field.getMessage());
		}  
		
		if (Field.USERNAME == field) {
			mTextUsername.getStyleClass().add("border-error");
			showToolTip(stage, mTextUsername, field.getMessage());
		}  
		
		if (Field.PASSWORD == field) {
			mTextPassword.getStyleClass().add("border-error");
			showToolTip(stage, mTextPassword, field.getMessage());
		}  
		
		if (Field.LINK == field) {
			mTextLink.getStyleClass().add("border-error");
			showToolTip(stage, mTextLink, field.getMessage());
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

	private boolean validateParties() {
		return pin != null && Strings.validate(pin, this) && person != null && listener != null;
	}

	@Override
	public void setStage(Stage stage) {
		this.stage = stage;
		if(this.stage!=null) {
			this.stage.setOnCloseRequest(e -> {
				onCancelHandler(null);
			});
		}
	}
	
	

}
