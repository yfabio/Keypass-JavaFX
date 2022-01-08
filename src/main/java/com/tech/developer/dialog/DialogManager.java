package com.tech.developer.dialog;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.tech.developer.domain.Person;
import com.tech.developer.persistance.PersistanceException;
import com.tech.developer.util.Strings;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

public class DialogManager {
	
	/**
	 * 
	 * The dialogInfo shows a info dialog whose parameters are title, header and content. 
	 * @param  title
	 * @param  header
	 * @param  content 
	 *  
	 */
	public static void dialogInfo(String title, String header, String content) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}

	/**
	 * 
	 *  The overloaded dialogInfo shows only title and the content. 
	 * @param  title
	 * @param  header
	 * @param  content
	 *  
	 */
	public static void dialogInfo(String title, String content) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}
	
	
	/**
	 * 
	 * The dialogWarning shows a warning dialog whose parameters are title, header and content. 
	 * @param  title
	 * @param  header
	 * @param  content
	 *  
	 */
	
	public static void dialogWarning(String title, String header, String content) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
		
	
	/**
	 * 
	 *  The dialogInfo shows a error dialog whose parameters are title, header and content.
	 * @param  title
	 * @param  header
	 * @param  content
	 *  
	 */
	public static void dialogError(String title, String header, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
	
	/**
	 * 
	 *  The dialogConfirm shows a confirmation dialog whose parameters are title, header, content and a DialogConfirmationListener 
	 *  which is the instance that needs to be implemented in order to be called when the dialog's ok button is pressed.
	 *   
	 * @param title
	 * @param header
	 * @param content
	 * @param listener
	 */
	public static void dialogConfirm(String title, String header, String content, DialogConfirmationListener listener) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		Optional<ButtonType> result =  alert.showAndWait();
		if(result.get() == ButtonType.OK && listener != null) {
			listener.onConfirm();
		}
	}
	
	
	/**
	 *  The dialogException shows a exception dialog whose parameters are title, header, content and ex which is the Exception.
	 * 
	 * @param title
	 * @param header
	 * @param content
	 * @param ex
	 */
	public static void dialogException(String title, String header, String content, Exception ex) {
		
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		
		String exceptionText = sw.toString();

		Label label = new Label("The exception stacktrace was:");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);


		alert.getDialogPane().setExpandableContent(expContent);
		alert.showAndWait();
		
	}
	
	
	/**
	 * The dialogImport show a import dialog whose parameter are stage and an instance of FileChooserListener which will be called when the ok button
	 * impressed. 
	 * 
	 * @param stage
	 * @param listener
	 * @throws PersistanceException 
	 */
	public static void dialogImport(Stage stage, Person person,FileChooserListener listener) {
			
		Alert alert = new Alert(AlertType.WARNING);
		
		
		alert.setTitle(Strings.getBundleValue("alert-import-title"));
		alert.setHeaderText(Strings.getBundleValue("alert-import-head"));
		alert.setContentText(Strings.getBundleValue("alert-import-content"));
		
		alert.getButtonTypes().addAll(ButtonType.CANCEL);
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialFileName(person.getUsername().concat(".txt"));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));			   
		 		
		Optional<ButtonType> result =alert.showAndWait();
		
		if(ButtonType.OK == result.get() && listener != null) {			
			File file = fileChooser.showOpenDialog(stage);
			if(file!=null)
			listener.onFileChooser(file);						
		}else {
			alert.close();
		}
				
	}
	
	/**
	 *  The dialogAbout show a dialog containing the version as well as the developer who own the system itself.
	 * 
	 * @param title
	 * @param content
	 */
	public static void dialogAbout(String title, String content) {
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setContentText(content);
		
		GridPane gp = new GridPane();
				
			
		Label version = new Label(Strings.getBundleValue("label-version"));
		
		Label versionNumber = new Label("4.0");
		
		Label labelOwner = new Label(Strings.getBundleValue("label-owner"));
		
		Label owner = new Label("Fabio Yasuo");
		
			
		gp.add(labelOwner, 0, 0);
		gp.add(owner, 1,0);
		gp.add(version, 0, 1);
		gp.add(versionNumber, 1,1);
		
		
		alert.getDialogPane().setExpandableContent(gp);
		alert.show();
		
		
	}
	
	
	public static boolean dialogCredential(Person person) {
		
		
		
		// Create the custom dialog.
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("Login Dialog");
		dialog.setHeaderText("File Export Preference Location");

		// Set the icon (must be included in the project).
		//dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

		// Set the button types.
		ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

		// Create the username and password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField username = new TextField();
		username.setPromptText("Username");
		PasswordField password = new PasswordField();
		password.setPromptText("Password");

		grid.add(new Label("Username:"), 0, 0);
		grid.add(username, 1, 0);
		grid.add(new Label("Password:"), 0, 1);
		grid.add(password, 1, 1);

		// Enable/Disable login button depending on whether a username was entered.
		Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
		loginButton.setDisable(true);

		// Do some validation (using the Java 8 lambda syntax).
		username.textProperty().addListener((observable, oldValue, newValue) -> {
		    loginButton.setDisable(newValue.trim().isEmpty());
		});

		dialog.getDialogPane().setContent(grid);

		// Request focus on the username field by default.
		Platform.runLater(() -> username.requestFocus());

		// Convert the result to a username-password-pair when the login button is clicked.
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == loginButtonType) {
		        return new Pair<>(username.getText(), password.getText());
		    }
		    return null;
		});

		Optional<Pair<String, String>> result = dialog.showAndWait();
		
		List<Boolean> success = new ArrayList<Boolean>();

		result.ifPresent(usernamePassword -> {
			
			boolean p_user = usernamePassword.getKey().equalsIgnoreCase(person.getUsername());
			boolean p_pass = usernamePassword.getValue().equalsIgnoreCase(person.getPassword());
			
			success.add(p_user);
			success.add(p_pass);
			
		});
		
		return success.stream().allMatch(e -> e);
	}
	
	
	
	
	
	
	
}
