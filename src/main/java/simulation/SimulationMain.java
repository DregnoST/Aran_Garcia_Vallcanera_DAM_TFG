package simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dataModels.UnitDataModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import logic.UnitAverageDamageCalculator;

public class SimulationMain {
	public static final int[] VALORES_SALVACION = {2,3,4,5,6,7};

	public static int simulaciones = 5000;
	public static ObservableList<UnitDataModel> unitsList = FXCollections.observableArrayList();
	public static ArrayList<double[]> unitsDamageDealtList = new ArrayList<double[]>();
	public static Map<UnitDataModel, double[]> unitsDamageMap = new HashMap<UnitDataModel, double[]>();
	
	public static Map<UnitDataModel, double[]> getUnitsListAverageDamage() {
		for (UnitDataModel unit : unitsList) {
			double[] value = getAverageUnitDamage(unit);
			unitsDamageDealtList.add(value);
			unitsDamageMap.put(unit, value);
		}
		return unitsDamageMap;
	}
	
	// puede que haya que borrarlo
	public static double[][] getAllAverageUnitDamage(ObservableList<UnitDataModel> unitList) {
		
		List<double[]> allAverageUnitDamageList = new ArrayList<double[]>();
		
		// determina cuantas unidades validas hay (con nombre y al menos 1 arma)
		for (UnitDataModel unit : unitList) {
			if (!((unit.getUnitName().isEmpty() || unit.getUnitName().contentEquals("")) && unit.getWeaponListSize() < 1)) {
				allAverageUnitDamageList.add(getAverageUnitDamage(unit));
			}
		}
		
		return null;	
	}
	
	// puede que haya que borrarlo
	public static double[] getAverageUnitDamage(UnitDataModel unit) {
		return UnitAverageDamageCalculator.unitDamageOutAverage(unit);
	}

	// puede que haya que borrarlo
	public static void addToUnitList(UnitDataModel unit) {
		unitsList.add(unit);
	}

	// Getters y Setters
	public static int getSimulaciones() {
		return simulaciones;
	}

	public static void setSimulaciones(int simulaciones) {
		SimulationMain.simulaciones = simulaciones;
	}

	public static ObservableList<UnitDataModel> getUnitsList() {
		return unitsList;
	}

	public static void setUnitsList(ObservableList<UnitDataModel> unitsList) {
		SimulationMain.unitsList = unitsList;
	}

	public static ArrayList<double[]> getUnitsDamageDealtList() {
		return unitsDamageDealtList;
	}

	public static void setUnitsDamageDealtList(ArrayList<double[]> unitsDamageDealtList) {
		SimulationMain.unitsDamageDealtList = unitsDamageDealtList;
	}

}
