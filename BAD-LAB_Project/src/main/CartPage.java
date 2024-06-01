package main;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Cart;
import models.Courier;
import util.Connect;

public class CartPage extends Application implements EventHandler<ActionEvent>{

	public static void main(String[] args) {
		launch(args);
	}
	
	Scene scene;
	BorderPane root;
	GridPane formPane, rightForm;
	VBox rightSide;
	
	MenuBar menuBar;
	Menu homeMenu;
	MenuItem home, cart, logOut;
	
	Label cartOwner, deleteItem, courier, courierPrice, totalPrice;
	TableView<Cart> cartTable;
	Button delBtn, checkoutBtn;
	ComboBox<String> courierChoices;
	CheckBox insuranceOpt;
	
	Vector<Cart> cartDatas;
	
	String cupIdTemp = null;
	
	private static String username;
	
	public static String getUsername() {
		return username;
	}

	public static void setUsername(String username) {
		CartPage.username = username;
	}
	
	private Connect connect = Connect.getInstance();

	private void initialize() {
		root = new BorderPane();
		formPane = new GridPane();
		rightForm = new GridPane();
		rightSide = new VBox();
		
		cartDatas = new Vector<Cart>();
		
		menuBar = new MenuBar();
        homeMenu = new Menu("Menu");

        home = new MenuItem("Home");
        cart = new MenuItem("Cart");
        logOut = new MenuItem("Log Out");
        
		cartOwner = new Label(username + "'s Cart");
		deleteItem = new Label("Delete Item");
		courier = new Label("Courier");
		courierPrice = new Label("Courier Price :");
		totalPrice = new Label("Total Price :");
		
		cartTable = new TableView<Cart>();
		
		delBtn = new Button("Delete Item");
		checkoutBtn = new Button("Checkout");
		
		courierChoices = new ComboBox<>();
		
		insuranceOpt = new CheckBox("Use Delivery Insurance");
				
		scene = new Scene(root, 800, 600);
	}

	private void addComponent() {
		formPane.add(cartOwner, 0, 0);
		formPane.add(cartTable, 0, 1);
		
        homeMenu.getItems().add(home);
        homeMenu.getItems().add(cart);
        homeMenu.getItems().add(logOut);
        
        menuBar.getMenus().addAll(homeMenu);
		
		rightForm.add(deleteItem, 0, 0);
		
		delBtn.setMinWidth(110);
		delBtn.setMinHeight(40);
		delBtn.setPrefWidth(110);
		delBtn.setPrefHeight(40);
		rightForm.add(delBtn, 0, 1);
		
//		courierChoices.getItems().add("JNA");
//		courierChoices.getItems().add("TAKA");
//		courierChoices.getItems().add("LoinParcel");
//		courierChoices.getItems().add("IRX");
//		courierChoices.getItems().add("JINIA");
		
		addCourierChoices();
		
		rightForm.add(courier, 0, 2);
		rightForm.add(courierChoices, 0, 3);
		rightForm.add(courierPrice, 0, 4);
		rightForm.add(insuranceOpt, 0, 5);
		rightForm.add(totalPrice, 0, 6);
		
		checkoutBtn.setMinWidth(110);
		checkoutBtn.setMinHeight(40);
		checkoutBtn.setPrefWidth(110);
		checkoutBtn.setPrefHeight(40);
		rightForm.add(checkoutBtn, 0, 7);
		
		rightSide.getChildren().add(rightForm);
		
		formPane.add(rightSide, 1, 1);
		
		root.setTop(menuBar);
		root.setCenter(formPane);
	}
	
	private void setTable() {
		TableColumn<Cart, String> nameCol = new TableColumn<>("Cup Name");
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		nameCol.setMinWidth(150);
		
		TableColumn<Cart, Integer> priceCol = new TableColumn<>("Cup Price");
		priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
		priceCol.setMinWidth(80);
		
		TableColumn<Cart, Integer> qtyCol = new TableColumn<>("Quantity");
		qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		qtyCol.setMinWidth(80);
		
		TableColumn<Cart, Integer> totalCol = new TableColumn<>("Total");
		totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
		totalCol.setMinWidth(100);
		
		cartTable.getColumns().addAll(nameCol, priceCol, qtyCol, totalCol);
		
		refreshTable();
	}

	private void arrangeComponent() {
		formPane.setAlignment(Pos.BOTTOM_LEFT);
		BorderPane.setMargin(formPane, new Insets(10));
		formPane.setVgap(10);
		formPane.setHgap(10);
		
		rightForm.setVgap(15);
	}

	private void styleComponent() {
		cartOwner.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");
		deleteItem.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");
		courier.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");
		courierPrice.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");
		totalPrice.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");
	}

	private void setEvent() {
		delBtn.setOnAction(this);
		checkoutBtn.setOnAction(this);
		
		insuranceOpt.setOnAction(e -> {
			calculateTotalPrice();
		});
		
		cartTable.setOnMouseClicked(tableMouseEvent());
	}
	
	private EventHandler<MouseEvent> tableMouseEvent(){
		return new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				TableSelectionModel<Cart> tableSelectionModel = cartTable.getSelectionModel();
				tableSelectionModel.setSelectionMode(SelectionMode.SINGLE);
				Cart cart = tableSelectionModel.getSelectedItem();
				
				cupIdTemp = cart.getCupId();
			}
			
		};
	}
	
	private void calculateTotalPrice() {
		String selectedCourier = courierChoices.getValue();
	    int courierCost = getCourierPrice(selectedCourier);
	    int insuranceCost = insuranceOpt.isSelected() ? 2000 : 0;

	    int totalPriceValue = calculateTotalPriceFromCart() + courierCost + insuranceCost;

	    courierPrice.setText("Courier Price : " + courierCost);
	    totalPrice.setText("Total Price : " + totalPriceValue);
	}

//	private int getCourierCost() {
//		int courierCost = 0;
//		
//	    String query = "SELECT CourierPrice FROM mscourier";
//		connect.rs = connect.execQuery(query);
//		
//		try {
//	        if (connect.rs.next()) {
//	            courierCost = connect.rs.getInt("CourierPrice");
//	            courierPrice.setText("Courier Price : " + courierCost);
//	        }
//	    } catch (SQLException e) {
//	        e.printStackTrace();
//	    }
//
//	    return courierCost;
//	}

	private int calculateTotalPriceFromCart() {
	    int total = 0;
	    for (Cart cartItem : cartDatas) {
	        total += cartItem.getTotal();
	    }
	    return total;
	}
	
	private void getData() {
	    cartDatas.removeAllElements();

	    String query = String.format("SELECT c.CupID, CupName, CupPrice, Quantity, mc.CupPrice * c.quantity AS Total, c.UserID\n" +
                "FROM cart c\n" +
                "JOIN mscup mc ON c.CupID = mc.CupID\n" +
                "JOIN msuser mu ON c.UserID = mu.UserID\n" +
                "WHERE Username = '%s'", username);
        connect.rs = connect.execQuery(query);

        try {
            while (connect.rs.next()) {
                String cupId = connect.rs.getString("CupID");
                String name = connect.rs.getString("CupName");
                Integer price = connect.rs.getInt("CupPrice");
                Integer quantity = connect.rs.getInt("Quantity");
                Integer total = connect.rs.getInt("Total");
                String userId = connect.rs.getString("UserID");

                cartDatas.add(new Cart(userId, cupId, name, price, quantity, total));
            }
        } catch (Exception e) {
	    	
	    }
	}

	
	private void refreshTable() {
		getData();
		ObservableList<Cart> cartObs = FXCollections.observableArrayList(cartDatas);
		cartTable.setItems(cartObs);
	}
	
	private void menuItemsSwitchScene() {
		home.setOnAction(e -> {
			
		});
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		initialize();
		addComponent();
		arrangeComponent();
		styleComponent();
		setEvent();
		setTable();
		menuItemsSwitchScene();
		
		stage.setTitle("cangkIR");
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void handle(ActionEvent e) {	
		if (e.getSource() == delBtn) {
			if (cupIdTemp == null) {
		        Alert alert = new Alert(Alert.AlertType.ERROR);
		        alert.setHeaderText("Deletion Error");
		        alert.setContentText("Please select the item you want to delete");
		        alert.show();
		    } else {
				String query = String.format(
						"DELETE FROM Cart\n" + 
						"WHERE CupID = '%s'AND UserID = (SELECT UserID FROM msuser WHERE Username = '%s')", cupIdTemp, username);
				connect.execUpdate(query);
				
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
		        alert.setHeaderText("Deletion Information");
		        alert.setContentText("Cart deleted successfully!");
		        alert.show();
				
				refreshTable();
		    }
		} else if (e.getSource() == checkoutBtn) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			if (cartDatas.isEmpty()) {
				alert.setHeaderText("Checkout Error");
				alert.setContentText("There are no items to be checked out");
				alert.show();
			} else {
				//checkoutConfirmationPopUp();
			}
		}
	}
	
	private void addCourierChoices() {
        ArrayList<Courier> couriers = getCouriersData();

        for (Courier courier : couriers) {
            courierChoices.getItems().add(courier.getCourierName());
        }

        courierChoices.setOnAction(e -> {
            String selectedCourier = courierChoices.getValue();
            int courierPrice = getCourierPrice(selectedCourier);
            updateCourierPriceLabel(courierPrice);
            calculateTotalPrice();
        });
    }
	
	private ArrayList<Courier> getCouriersData() {
	    ArrayList<Courier> couriers = new ArrayList<>();

	    // Fetch courier data from the database and populate the list
	    String query = "SELECT * FROM mscourier";
	    connect.rs = connect.execQuery(query);

	    try {
	        while (connect.rs.next()) {
	        	String courierId = connect.rs.getString("CourierID");
	            String courierName = connect.rs.getString("CourierName");
	            int courierPrice = connect.rs.getInt("CourierPrice");
	            couriers.add(new Courier(courierId, courierName, courierPrice));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return couriers;
	}

    private int getCourierPrice(String courierName) {
        // Find the price of the selected courier
        for (Courier courier : getCouriersData()) {
            if (courier.getCourierName().equals(courierName)) {
                return courier.getCourierPrice();
            }
        }

        return 0; // Default price if not found
    }

    private void updateCourierPriceLabel(int price) {
        courierPrice.setText("Courier Price: " + price);
    }

}
