package controllers;

import dataModels.UnitDataModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import mongoDB.DatabaseManager;
import mongoDB.DialogMessage;
import simulation.Handler;

public class ImportUnitController {

	private NavigationController mainController;
	private UnitDataModel unitData;
	private Stage importStage;
	
	@FXML
	private TableColumn<UnitDataModel, String> unitColumn;
	
	@FXML
	private TableView<UnitDataModel> unitsListTableView;
	
	@FXML
	private Button deleteButton, loadButton;
	
	
	public void initialize() {
		// configurar la tabla para recibir datos
		unitColumn.setCellValueFactory(new PropertyValueFactory<UnitDataModel, String>("unitName"));
				
		unitsListTableView.setItems(Handler.getAllUnitsLoaded());
		
		// Listener a la selección de la tabla
		unitsListTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			deleteButton.setDisable(newSelection == null);
			loadButton.setDisable(newSelection == null);

		});
	}
	
	@FXML
	private void handleLoad() {
		UnitDataModel selectedUnit = unitsListTableView.getSelectionModel().getSelectedItem();
		mainController.loadItem(selectedUnit);
		importStage.close();
	}
	
	@FXML
	private void handleDelete() {
		DatabaseManager dbManager = DatabaseManager.getInstance();
		UnitDataModel selectedUnit = unitsListTableView.getSelectionModel().getSelectedItem();
		
		try {
			if (selectedUnit != null) {
				dbManager.deleteUnit(selectedUnit.getUnitName());
			}
			
		} catch (Exception e) {
			DialogMessage.showAlert("Operation error", "The unit could not be deleted.\nCheck and try again.");
		}
		
		updateTableView();
	}
	
	@FXML
	private void handleCancel() {
		importStage.close();
	}
	
	private void updateTableView() {
		DatabaseManager dbManager = DatabaseManager.getInstance();  
		Handler.allUnitsLoaded.clear();
		
		try {
			Handler.setAllUnitsLoaded(dbManager.loadAllUnits());
		} catch (Exception e) {
			DialogMessage.showAlert("Connection error", "The connection to the database has been lost.\nCheck connection and try again.");
		}
		
		unitsListTableView.setItems(Handler.getAllUnitsLoaded());
		
	}
	
	public void setMainController(NavigationController navigationController) {
		this.mainController = navigationController;
	}
	
	public void setUnit(UnitDataModel unitData) {
		this.unitData = unitData;
	}
	
	public void setStage(Stage stage) {
		this.importStage = stage;
	}
}
