package view;

import java.io.IOException;
import java.util.List;

import controller.GuestController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GuestView extends Application {
	@FXML
	private TextField fullName; 
	@FXML
	private TextField address; 
	@FXML
	private TextField email; 
	@FXML
	private TextField phoneNumber; 
	@FXML
	private TextField citizenship;
	@FXML
	private TextField creditCardNum; 
	@FXML
	private CheckBox smoker; 
	@FXML
	private TextField favRoom; 
	@FXML 
	private TextField company; 
	@FXML 
	private DatePicker birthDate;
	@FXML 
	private Button cancelButton;
	@FXML
	private Button submitButton;
	@FXML
	private Button editButton;
	@FXML 
	private Button deleteButton;
	
	
	
	
	ObservableList<String> cityList = FXCollections.observableArrayList("Vaxjo","Kalmar"); 

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("GuestView.fxml"));
		primaryStage.setTitle("Guest View");
		primaryStage.setScene(new Scene(root));
		primaryStage.setResizable(false);
		primaryStage.show(); 
	}
	
	@FXML
	public void initialize() {
		phoneNumber.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
			  @Override public void handle(KeyEvent keyEvent) {
			    if (!"0123456789".contains(keyEvent.getCharacter())) {
			      keyEvent.consume();
			    }
			  }
		});
		creditCardNum.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
			  @Override public void handle(KeyEvent keyEvent) {
			    if (!"0123456789".contains(keyEvent.getCharacter())) {
			      keyEvent.consume();
			    }
			  }
		});
		
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void onClickConfirm(){
	if(inputCheck()) {
	GuestController gc = new GuestController(); 
	gc.createGuest(fullName.getText(),birthDate.getValue().toString(),company.getText(),citizenship.getText(), address.getText(),smoker.isSelected(),email.getText(), phoneNumber.getText(), favRoom.getText(), creditCardNum.getText());
	}
	}
	public void onClickEdit(){
		
	}
	public void onClickCancel(){
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}
	public void onClickDelete(){
		
	}
	
	
	public boolean inputCheck(){
		if(fullName.getText().length()<3) {
		showError("Name is too short !","Please enter a name that is longer than 3 characters");
		return false; 	
		}
		if(birthDate.getValue() == null) {
		showError("No Birthdate","Please specify the birthdate");
		return false; 	
		}
		if(citizenship.getText() == null) {
			showError("No Citizenship","Please specify the citizenship");
			return false; 
		}
		if(address.getText() == null){
			showError("No Address","Please specify the address");
			return false; 
		}
	
		if(creditCardNum.getText() == null) {
			showError("No Creditcard number","Please specify the credit card number");
			return false; 
		}
		return true;
		
	}
	 public static void showError(String title, String message) {
	        Alert alert = new Alert(Alert.AlertType.ERROR);
	        alert.initStyle(StageStyle.UTILITY);
	        alert.setTitle("Error");
	        alert.setHeaderText(title);
	        alert.setContentText(message);

	        alert.showAndWait();
	    }
	
	
	
	
}
