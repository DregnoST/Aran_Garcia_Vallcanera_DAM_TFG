package controllers;

import dataModels.AverageDamageDataModel;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import simulation.Handler;

public class AverageDamageTableController {
	
	@FXML
	private TableView<AverageDamageDataModel> averageDamgeTableView;
	
	@FXML
	private TableColumn<AverageDamageDataModel, String> unitColumn;
	
	@FXML
	private TableColumn<AverageDamageDataModel, Double> save2Column, save3Column, save4Column, save5Column, save6Column, save7Column;
	
	
	@FXML
	public void initialize() {
		
		// configurar la tabla para recibir datos
		unitColumn.setCellValueFactory(new PropertyValueFactory<AverageDamageDataModel, String>("unitName"));
		save2Column.setCellValueFactory(new PropertyValueFactory<AverageDamageDataModel, Double>("save2"));
		save3Column.setCellValueFactory(new PropertyValueFactory<AverageDamageDataModel, Double>("save3"));
		save4Column.setCellValueFactory(new PropertyValueFactory<AverageDamageDataModel, Double>("save4"));
		save5Column.setCellValueFactory(new PropertyValueFactory<AverageDamageDataModel, Double>("save5"));
		save6Column.setCellValueFactory(new PropertyValueFactory<AverageDamageDataModel, Double>("save6"));
		save7Column.setCellValueFactory(new PropertyValueFactory<AverageDamageDataModel, Double>("save7"));
		
		averageDamgeTableView.setItems(Handler.getAverageDamageData());
	}
	

}
