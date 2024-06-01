package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.CupList;

public class HomePage extends Application{
	
	public static void main(String[] args) {
		launch(args);
	}
	
	Scene scene;
	BorderPane root;
	GridPane formPane;
	
	Label cupNameLbl, priceLbl, tableTitleLbl;
	Spinner<Integer> nameSpinner;
	TableView<CupList> cupTable;
	Button addCartBtn;
	//Alert a;
	
	private void initialize() {
		root = new BorderPane();
		formPane = new GridPane();
		
		cupNameLbl = new Label("Cup Name");
		priceLbl = new Label("Price");
		tableTitleLbl = new Label("Cup List");
		
		nameSpinner = new Spinner<>(1, 20, 1);
		
		cupTable = new TableView<CupList>();
	
		addCartBtn = new Button("Add to Cart");
		
		scene = new Scene(root, 800, 600);
	}
	
	private void addComponent() {
		
	}
	
	private void arrangeComponent() {
		
	}

	private void styleComponent() {
		
	}

	private void setEvent() {
		
	}

	
	@Override
	public void start(Stage stage) throws Exception {
		initialize();
		addComponent();
		arrangeComponent();
		styleComponent();
		setEvent();
		
		stage.setTitle("cangkIR");
		stage.setScene(scene);
		stage.show();
	}
}
