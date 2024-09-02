package controllers;

import dataModels.AverageDamageDataModel;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import simulation.Handler;

public class GraficaController {

	@FXML
	private BarChart<String, Number> barChart;

	@FXML
	private LineChart<String, Number> lineChart;
    
	public void initialize() {
	
		barChart.setAnimated(false);
		lineChart.setAnimated(false);
		
		// cada vez que la fuente de datos cambie actualiza el grafico
		Handler.averageDamageData.addListener((ListChangeListener.Change<? extends AverageDamageDataModel> change) -> {
			 updateBarChart();
			 updateBarLineChart();
		});

	}
	
	private void updateBarLineChart() {
		lineChart.getData().clear();
		for (AverageDamageDataModel item : Handler.averageDamageData) {
			XYChart.Series<String, Number> series = new XYChart.Series<>();
			series.setName(item.getUnitName());
			series.getData().add(new XYChart.Data<>("+2", Math.round(item.getSave2() * 100.0) / 100.0));
			series.getData().add(new XYChart.Data<>("+3", Math.round(item.getSave3() * 100.0) / 100.0));
			series.getData().add(new XYChart.Data<>("+4", Math.round(item.getSave4() * 100.0) / 100.0));
			series.getData().add(new XYChart.Data<>("+5", Math.round(item.getSave5() * 100.0) / 100.0));
			series.getData().add(new XYChart.Data<>("+6", Math.round(item.getSave6() * 100.0) / 100.0));
			series.getData().add(new XYChart.Data<>("-", Math.round(item.getSave7() * 100.0) / 100.0));
			
			lineChart.getData().add(series);
		}		
		
	}

	private void updateBarChart() {
		barChart.getData().clear();
		for (AverageDamageDataModel item : Handler.averageDamageData) {
			XYChart.Series<String, Number> series = new XYChart.Series<>();
			series.setName(item.getUnitName());
			series.getData().add(new XYChart.Data<>("+2", Math.round(item.getSave2() * 100.0) / 100.0));
			series.getData().add(new XYChart.Data<>("+3", Math.round(item.getSave3() * 100.0) / 100.0));
			series.getData().add(new XYChart.Data<>("+4", Math.round(item.getSave4() * 100.0) / 100.0));
			series.getData().add(new XYChart.Data<>("+5", Math.round(item.getSave5() * 100.0) / 100.0));
			series.getData().add(new XYChart.Data<>("+6", Math.round(item.getSave6() * 100.0) / 100.0));
			series.getData().add(new XYChart.Data<>("-", Math.round(item.getSave7() * 100.0) / 100.0));
			
			barChart.getData().add(series);
		}			
		
	}

}
