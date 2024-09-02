package controllers;

import dataModels.ProbabilityXDamageDataModel;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import simulation.Handler;

public class discreteChartSave7Controller {
	
	@FXML
	private LineChart<String, Number> lineChart;
	
	
	public void initialize() {
		
		lineChart.setAnimated(false);
		
		// cada vez que la fuente de datos cambie actualiza el grafico	
		Handler.probXDamageSave7.addListener((ListChangeListener.Change<? extends ProbabilityXDamageDataModel> change) -> {
			updateBarLineChart();
		});

	}
	
	private void updateBarLineChart() {
		lineChart.getData().clear();
		for (ProbabilityXDamageDataModel item : Handler.probXDamageSave7) {
			XYChart.Series<String, Number> series = new XYChart.Series<>();
			series.setName(item.getUnitName());
			
			for (int i = 0; i < item.getProbability().length; i++) {
				series.getData().add(new XYChart.Data<>(String.valueOf(i), Math.round(item.getProbability()[i] * 100.0) / 100.0));				
			}
			
			lineChart.getData().add(series);
		}		
		
	}

}
