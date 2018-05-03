package controller;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import model.Bill;
import model.Reservation;
import model.Room;
import model.RoomState;
import view.CheckInView;
import view.CheckOutView;
import view.MainView;

public class MainController extends Controller {
	
	//FXML loaders
	private FXMLLoader fxmlcheckIn = new FXMLLoader((getClass().getResource("../view/CheckInView.fxml")));
	private FXMLLoader fxmlcheckOut = new FXMLLoader((getClass().getResource("../view/CheckOutView.fxml")));
	private FXMLLoader fxmlMain = new FXMLLoader((getClass().getResource("../view/MainView.fxml")));
	
	// Views
	private CheckInView checkIn;
	private MainView mainView;
	private CheckOutView checkOut;
	
	//Parents
	private Parent mainParent;
	private Parent checkInParent;
	private Parent checkOutParent;
	
	
	private ArrayList<Reservation> resvList;

	public MainController() {
		
		//Load loaders
		try {
			checkInParent = fxmlcheckIn.load();
			checkOutParent = fxmlcheckOut.load();
			mainParent = fxmlMain.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Init views
		checkIn = fxmlcheckIn.getController();
		mainView = fxmlMain.getController();
		checkOut = fxmlcheckOut.getController();
		
		// Set controllers
		mainView.setController(this);
		checkIn.setController(this);
		checkOut.setController(this);
		
		//Set databases
		

		// Set parents
		mainView.setParent(mainParent);
		checkIn.setParent(checkInParent);
		checkOut.setParent(checkOutParent);
		
		//Set stages
		mainView.setStage("Linnaeus Hotel");
		checkIn.setStage("Check In");
		checkOut.setStage("Check Out");
		
	}
	
	public Stage getMainView() throws Exception {
		return mainView.display();
	}
	
	public Stage getDatabaseViewStage() throws Exception {
		return app.getNetController().getDbView();
	}
	
	public ArrayList<Reservation> checkedOutRes(ArrayList<Reservation> resvList) {
		ArrayList<Reservation> temp = new ArrayList<Reservation>(); 
		if(!resvList.isEmpty())
		for(int i = 0; i < resvList.size(); i++){
			if(resvList.get(i).getCheckedIn() == false && resvList.get(i).getStartDate().equals(LocalDate.now())) {
				temp.add(resvList.get(i));
			}
		}
		return temp;
	}
	
	public void refreshCheckInView(){
		resvList = app.getDatabase().findReservation();
		resvList = checkedOutRes(resvList);
		checkIn.setTable(resvList);
		checkIn.initialize();
	}
	
	public void refreshCheckInView(String s){
		resvList = app.getDatabase().findReservationByName(s);
		resvList = checkedOutRes(resvList);
		checkIn.setTable(resvList);
		checkIn.initialize();
	}
	
	public Stage getCheckInView() throws Exception {
		resvList = app.getDatabase().findReservation();
		resvList = checkedOutRes(resvList);
		checkIn.setTable(resvList);
		checkIn.initialize();
		return checkIn.display();
	}
	
	public Stage getCheckOutView() throws Exception {
		return checkOut.display();
	}
	
	//TODO
	public void checkOut(Reservation res) throws FileNotFoundException, UnsupportedEncodingException {
		// Find reserved room for price
		ArrayList<Room> rooms = app.getDatabase().findRooms();
		Room room = null;
		if (!rooms.isEmpty()) {
			for (int i =0; i < rooms.size(); i++) {
				if (rooms.get(i).getRoomNum().equals(res.getRoom())) {
					room = rooms.get(i);
					break;
				}
			}
		}
		// print bill
		printBill(res,false);
		app.getDatabase().deleteReservation(res);
		//TODO app.getDatabase().updateRoom(room.getRoomNum(), RoomState.free);
	}
	
	public ArrayList<Reservation> getCheckedInReservations() {
		return app.getDatabase().findCheckedInReservations();
	}

	public void checkIn(Reservation res) {
	app.getDatabase().deleteReservation(res);
	res.setCheckedIn(true);
	app.getDatabase().writeReservation(res);
		
	}
	
	
	
	public void printBill(Reservation res, Boolean cancellation) throws FileNotFoundException, UnsupportedEncodingException {
		Bill bill = new Bill(res.getGuestName(), Integer.parseInt(res.getPrice()), res.getStartDate(), res.getEndDate(),cancellation);
		String fileName = bill.getGuestName() + bill.getArrival().toString() + "-" + bill.getDeparture() + ".txt";
		
		PrintWriter writer = new PrintWriter(fileName, "UTF-8");
		
		writer.println("Bill for " + bill.getGuestName());
		writer.println("-------------------------------------");
		if(bill.isCancellation()) {
		writer.println("Cancelled Reservation from " + bill.getArrival().toString() + " to " + bill.getDeparture().toString());
		writer.println("Room Price : " + bill.getRoomPrice());
		writer.println("Cancellation fee (15% of total room reservation price) : " + bill.calculateBill());
		writer.println("Total : " + bill.calculateBill() + "SEK");
		}else{
		writer.println("Stay from " + bill.getArrival().toString() + "to " + bill.getDeparture().toString());
		writer.println("Room price : " + bill.getRoomPrice() + " SEK (per night)"); 
		writer.println("Total Price : " + bill.calculateBill() + "SEK"); 
		}
		writer.close();
		Desktop desktop = Desktop.getDesktop();
		try {
			desktop.open(new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
	}


