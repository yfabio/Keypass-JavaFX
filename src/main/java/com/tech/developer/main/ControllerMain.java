package com.tech.developer.main;

import static com.tech.developer.util.Strings.getBundleValue;

import java.io.IOException;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.kordamp.bootstrapfx.BootstrapFX;

import com.tech.developer.backup.FileBackup;
import com.tech.developer.backup.FileExport;
import com.tech.developer.backup.FileImport;
import com.tech.developer.backup.FileStreamCallBacks;
import com.tech.developer.backup.FileTaskBackup;
import com.tech.developer.backup.FileTaskImported;
import com.tech.developer.crypto.CryptoUtilCallback;
import com.tech.developer.dialog.DialogManager;
import com.tech.developer.domain.Bundle;
import com.tech.developer.domain.Person;
import com.tech.developer.domain.Pin;
import com.tech.developer.domain.ReloadListener;
import com.tech.developer.domain.StageAwareController;
import com.tech.developer.domain.ValidationState;
import com.tech.developer.persistance.DAOPerson;
import com.tech.developer.persistance.DAOPin;
import com.tech.developer.persistance.PersistanceException;
import com.tech.developer.persistance.SQLCriteriaFactory;
import com.tech.developer.util.Dictionary;
import com.tech.developer.util.FXMLUtil;
import com.tech.developer.util.FXMLUtil.Layout;
import com.tech.developer.util.ResourceBundles;
import com.tech.developer.util.Strings;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ControllerMain implements Serializable, StageAwareController, ReloadListener, CryptoUtilCallback, ValidationState {

	private static final long serialVersionUID = 1L;

	@FXML
	private BorderPane mRoot;

	@FXML
	private ProgressIndicator mProgress;

	@FXML
	private Button mNewButton;

	@FXML
	private Button mFolderButton;

	@FXML
	private Button mExitButton;

	@FXML
	private TextField mSearchText;

	@FXML
	private Button mSearchButton;

	@FXML
	private Button mRefreshButton;

	@FXML
	private ListView<String> mListView;

	@FXML
	private Button mEditButton;

	@FXML
	private Button mDeleteButton;

	@FXML
	private TableView<Pin> mTable;

	@FXML
	private Label mCurrentUser;

	@FXML
	private Label mSelectedRow;

	@FXML
	private Label mCurrentDate;
	
	@FXML 
	private ToggleButton mSave;

	private Person person = new Person();

	private DAOPerson persondao;

	private DAOPin pindao;

	private Stage stage;

	@FXML 
	private Button mDisplayPin;

	

	@FXML
	public void initialize() {

		persondao = DAOPerson.getInstance();
		pindao = DAOPin.getInstance();

		try {
			String current = Dictionary.getCurrent();
			if (!Strings.isEmpty(current)) {
				person = persondao.get(SQLCriteriaFactory.getPersonByUsername(), List.of(current)).stream().findFirst()
						.orElse(new Person());
				List<Pin> pins = pindao.get(SQLCriteriaFactory.getPinsByPersonID(), List.of(person.getId()));
				load(pins);
			}
		} catch (PersistanceException e) {
			DialogManager.dialogError(getBundleValue("alert-main-title"), getBundleValue("alert-head-error"),
					e.getMessage());
		}

		mTable.getSelectionModel().selectedItemProperty().addListener((obs, older, newer) -> {
			if (newer != null) {
				mSelectedRow.textProperty()
						.bind(new SimpleStringProperty(getBundleValue("label-selected-row")).concat(newer.getId()));
				mListView.getSelectionModel().select(mTable.getItems().indexOf(newer));
			}
		});

		
		mSearchText.textProperty().addListener((obs, older, newer) -> {
			if(Strings.isEmpty(newer)) {
				reload();				
			}			
		});
		

		mDeleteButton.disableProperty().bind(mTable.getSelectionModel().selectedItemProperty().isNull());
		mEditButton.disableProperty().bind(mTable.getSelectionModel().selectedItemProperty().isNull());
		mRefreshButton.disableProperty().bind(mSearchText.textProperty().isEmpty());
		setBottom(mCurrentUser, mCurrentDate);
		
		mNewButton.setTooltip(new Tooltip(getBundleValue("tool-tip-new")));
		mFolderButton.setTooltip(new Tooltip(getBundleValue("tool-tip-import")));
		mExitButton.setTooltip(new Tooltip(getBundleValue("tool-tip-off")));
		mSave.setTooltip(new Tooltip(getBundleValue("button-backup-save")));
		mDisplayPin.setTooltip(new Tooltip(getBundleValue("button-display-pin")));
		

	}

	@FXML
	public void onRefreshHandler(ActionEvent event) {
		reload();
		mSearchText.clear();
	}

	@FXML
	public void onSearchHandler(ActionEvent event) {

		StringBuilder sb = new StringBuilder();

		String search = mSearchText.getText();

		sb.append("%").append(search.toUpperCase()).append("%");

		try {
			List<Pin> pins = pindao.get(SQLCriteriaFactory.filterByTitle(), List.of(sb.toString()));
			load(pins);
		} catch (PersistanceException e) {
			DialogManager.dialogError(getBundleValue("alert-main-title"), getBundleValue("alert-head-error"),
					e.getMessage());
		}

	}

	@FXML
	public void onAboutHandler(ActionEvent event) {
		DialogManager.dialogAbout(getBundleValue("alert-main-title"), getBundleValue("alert-about-content"));
	}

	@FXML
	public void onDeleteHandler(ActionEvent event) {
		Pin pin = mTable.getSelectionModel().getSelectedItem();
		if (pin != null) {
			DialogManager.dialogConfirm(getBundleValue("alert-main-title"),
					getBundleValue("alert-delete-confirmation-head"), getBundleValue("alert-confirmation-content"),
					() -> {
						try {
							pindao.delete(pin);
							mTable.getItems().remove(pin);
							load(mTable.getItems());
							DialogManager.dialogInfo(getBundleValue("alert-main-title"),
									getBundleValue("alert-deleted-success-head"),
									getBundleValue("alert-deleted-success-content"));
						} catch (PersistanceException e) {
							DialogManager.dialogError(getBundleValue("alert-main-title"),
									getBundleValue("alert-head-error"), e.getMessage());
						}
					});
		}
	}

	@FXML
	public void onEditHandler(ActionEvent event) {

		Pin pin = mTable.getSelectionModel().getSelectedItem();

		try {
			if (pin != null) {

				Bundle bundle = new Bundle();
				bundle.putExtra("pin", pin).putExtra("reload", this);

				Stage stage = new Stage();

				Pane root = FXMLUtil.load(stage, Layout.FORM, ListResourceBundle.getBundle("properties/application"),
						bundle);

				Scene scene = new Scene(root);

				stage.setTitle(getBundleValue("alert-form-edit-title"));
				stage.centerOnScreen();
				stage.setResizable(false);
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.setScene(scene);
				stage.showAndWait();

			}

		} catch (IOException e) {
			DialogManager.dialogError(getBundleValue("alert-main-title"), getBundleValue("alert-head-error"),
					e.getMessage());
		}

	}

	@FXML
	public void onExitHandler(ActionEvent event) {
		exit();
	}

	@FXML
	public void onImportHandler(ActionEvent event) {

		FileStreamCallBacks fileStreamCallBacks = new FileStreamCallBacks() {

			@Override
			public void succeeded() {
				mRoot.setDisable(false);
				mProgress.setVisible(false);
			}

			@Override
			public void failed() {
				mRoot.setDisable(false);
				mProgress.setVisible(false);
				DialogManager.dialogError(getBundleValue("alert-main-title"), getBundleValue("alert-head-error"),
						getBundleValue("alert-file-import-content"));
			}

			@Override
			public void scheduled() {
				mRoot.setDisable(true);
				mProgress.setVisible(true);
				mProgress.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
			}

		};

		DialogManager.dialogImport(stage, person, file -> {

			FileTaskImported<List<Pin>> task = new FileImport(fileStreamCallBacks, this, file);

			try {

				task.restart();
				task.valueProperty().addListener((obs, older, newer) -> {
					if (newer != null) {
						deleteAndInsert(newer);
					}
				});

			} catch (Exception e) {
				DialogManager.dialogError(getBundleValue("alert-main-title"), getBundleValue("alert-head-error"),
						e.getMessage());
			}
		});

	}

	private void deleteAndInsert(List<Pin> newer) {
		
		ExecutorService ex = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());		
	    List<Callable<Void>> tasks = new ArrayList<Callable<Void>>();
	    	    	    
	    try {
	    	
	    	Callable<Void> taskDelete = () -> {
		    	pindao.execute(SQLCriteriaFactory.deleteAllPin().get());
				return null;	    	
		    };
		    		    
		    Callable<Void> taskInsert = () -> {
		    	for (Pin pin : newer) {
		    		pindao.insert(pin);
				}
				return null;	    	
		    };
			
		    Callable<Void> taskReload =  () -> {
		    	reload();
		    	return null;
		    };
			
		    tasks.add(taskDelete);
		    tasks.add(taskInsert);
		    tasks.add(taskReload);
		    
		    ex.invokeAll(tasks);
		    ex.shutdown();
			
		} catch (Exception e) {
			Platform.runLater(() -> {
				DialogManager.dialogError(getBundleValue("alert-main-title"), getBundleValue("alert-head-error"),e.getMessage());
			});
		}
	    
		
		
	}

	@FXML
	public void onNewHandler(ActionEvent event) {

		try {

			Bundle bundle = new Bundle();
			bundle.putExtra("pin", new Pin()).putExtra("reload", this);

			Stage stage = new Stage();

			Pane root = FXMLUtil.load(stage, Layout.FORM, ResourceBundles.getResouce(), bundle);

			Scene scene = new Scene(root);

			stage.setTitle(getBundleValue("alert-form-new-title"));
			stage.centerOnScreen();
			stage.setResizable(false);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setScene(scene);
			stage.showAndWait();

		} catch (IOException e) {
			DialogManager.dialogError(getBundleValue("alert-main-title"), getBundleValue("alert-head-error"),
					e.getMessage());
		}

	}

	@FXML
	public void onPrefHandler(ActionEvent event) {

		try {

			ReloadListener reload = () -> {
				ControllerMain.this.reload();
			};

			Bundle bundle = new Bundle();
			bundle.putExtra("reload", reload);

			Stage stage = new Stage();
			Pane root = FXMLUtil.load(stage, Layout.PREFERENCE, ResourceBundles.getResouce(), bundle);

			Scene scene = new Scene(root);
			scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
			
			stage.setTitle(getBundleValue("label-preference"));
			stage.centerOnScreen();
			stage.setResizable(false);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setScene(scene);
			stage.showAndWait();

		} catch (IOException e) {
			DialogManager.dialogError(getBundleValue("alert-main-title"), getBundleValue("alert-head-error"),
					e.getMessage());
		}

	}

	@Override
	public void setStage(Stage stage) {
		this.stage = stage;
		this.stage.setOnCloseRequest(event -> {
			exit();
			event.consume();
		});

	}

	private void setBottom(Label... labels) {
		labels[0].setText(Strings.getCurrentUser(person));
		labels[1].setText(Strings.getCurrentDate());
	}

	@FXML
	private void exit() {

		FileStreamCallBacks fileStreamCallBacks = new FileStreamCallBacks() {

			@Override
			public void succeeded() {
				mRoot.setDisable(false);
				mProgress.setVisible(false);
				close();
			}

			@Override
			public void failed() {
				DialogManager.dialogError(getBundleValue("alert-main-title"), getBundleValue("alert-head-error"),
						getBundleValue("alert-file-backup-content"));
				mRoot.setDisable(false);
				mProgress.setVisible(false);
				close();
			}

			@Override
			public void scheduled() {
				mRoot.setDisable(true);
				mProgress.setVisible(true);
				mProgress.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
			}
		};

		DialogManager.dialogConfirm(getBundleValue("alert-main-title"), "Exit",
				getBundleValue("alert-confirmation-content"), () -> {
					try {
						if (person != null && person.isBackup() && !mTable.getItems().isEmpty()) {
							String current = Dictionary.getCurrent();
							person = persondao.get(SQLCriteriaFactory.getPersonByUsername(), List.of(current)).stream()
									.findFirst().orElseThrow();
							if (person != null && mTable.getItems().size() > 0) {
								FileTaskBackup<Void> task = new FileBackup(fileStreamCallBacks, this, mTable.getItems(),
										person);
								task.restart();
							}
						} else {
							close();
						}
					} catch (PersistanceException e) {
						DialogManager.dialogException(getBundleValue("alert-main-title"),
								getBundleValue("alert-head-error"), e.getMessage(), e);
					}
				});

	}

	

	private void load(final List<Pin> pins) {
		if (!pins.isEmpty()) {
			Platform.runLater(() -> {
				mListView.setItems(pindao.extractNotesFromPins(pins.stream().distinct().collect(Collectors.toList())));
				mTable.setItems(
						FXCollections.observableArrayList(pins.stream().distinct().collect(Collectors.toList())));
			});
		}
	}

	@Override
	public void reload() {
		try {
			mTable.getItems().clear();
			String current = Dictionary.getCurrent();
			person = persondao.get(SQLCriteriaFactory.getPersonByUsername(), List.of(current)).stream().findFirst().orElseThrow();
			Platform.runLater(() -> mSave.setSelected(person.isBackup()));
			List<Pin> pins = pindao.get(SQLCriteriaFactory.getPinsByPersonID(), List.of(person.getId()));
			load(pins);
		} catch (PersistanceException e) {
			DialogManager.dialogError(getBundleValue("alert-main-title"), getBundleValue("alert-head-error"),
					e.getMessage());
		}
	}

	@Override
	public String encryptValue(String password) {
		synchronized (this) {
			password = persondao.encrypt(password);
		}
		return password;
	}

	@Override
	public String decryptValue(String password) {
		synchronized (this) {
			password = persondao.decrypt(password);
		}
		return password;
	}

	private void close() {
		if (person != null) {
			try {
				Dictionary.setProperty("lang", person.getLang());
			} catch (Exception e) {
				DialogManager.dialogError(getBundleValue("alert-main-title"), getBundleValue("alert-head-error"),
						e.getMessage());
			}
			stage.close();
		}
	}

	@FXML 
	public void onSaveBackup() {
					
		try {
								
			person = getPersonProvider();
			
			if(mSave.isSelected()) {
				person.setBackup(true);							
			}else {
				person.setBackup(false);
			}	
			
			persondao.update(person);
			
		} catch (PersistanceException e) {
			DialogManager.dialogError("preference", getBundleValue("alert-head-error"), e.getMessage());
		}
		
		
	}
	
	
	
	private class PersonProvider implements Callable<Person> {
		@Override
		public Person call() throws Exception {
			String current = Dictionary.getCurrent();
			return persondao.get(SQLCriteriaFactory.getPersonByUsername(), List.of(current)).stream().findFirst().orElse(new Person());			
		}		
	}
	
	private Person getPersonProvider() throws PersistanceException   {
		
		ExecutorService ex = Executors.newSingleThreadExecutor();
		
		try {
					
			PersonProvider personProvider = new PersonProvider();
			
			Future<Person> data = ex.submit(personProvider);
						
			return data.get();
			
		} catch (InterruptedException | ExecutionException e) {
			throw new PersistanceException(e);
		}finally {
			ex.shutdown();
		}
		
	}

	@FXML 
	public void onDisplayPin(ActionEvent event) {
		
		Pin pin = mTable.getSelectionModel().getSelectedItem();
		
		if(pin!=null) {
			
			String message = MessageFormat.format("The passowrd is {0} ",pindao.decrypt(pin.getPassword()));
			
			showToolTip(stage,mTable,message);			
		}
	}

	/*
	 * This method belongs to the ValidationState, so it does not need to implement because the ControllerMain
	 * is just using the showToolTip method
	 */
	@Override
	public void change(Field field) {		
	}

	@FXML 
	public void onExportSQL() {
		
		
		FileStreamCallBacks fileStreamCallBacks = new FileStreamCallBacks() {

			@Override
			public void succeeded() {
				mRoot.setDisable(false);
				mProgress.setVisible(false);				
			}

			@Override
			public void failed() {
				DialogManager.dialogError(getBundleValue("alert-main-title"), getBundleValue("alert-head-error"),
						getBundleValue("alert-file-backup-content"));
				mRoot.setDisable(false);
				mProgress.setVisible(false);				
			}

			@Override
			public void scheduled() { 
				mRoot.setDisable(true);
				mProgress.setVisible(true);
				mProgress.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
			}
		};
		
		
		
		try {
			List<Pin> pins = pindao.get(SQLCriteriaFactory.getPinsByPersonID(), List.of(person.getId()));
			
			if(DialogManager.dialogCredential(person)) {
				
				var pinsDecrypted = pins.stream().map(e -> new Pin(e.getId(),
						e.getTitle(),
						e.getUsername(),
						pindao.decrypt(e.getPassword()),
						e.getNotes())).collect(Collectors.toList());
				
				
				FileExport export = new FileExport(fileStreamCallBacks, pinsDecrypted, person);
				export.restart();			
				
			}
			
			
		} catch (PersistanceException e) {
			DialogManager.dialogError("preference", getBundleValue("alert-head-error"), e.getMessage());
		}
		
		
	}

	
	

}
