package simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import dataModels.AverageDamageDataModel;
import dataModels.ProbabilityXDamageDataModel;
import dataModels.UnitDataModel;
import dataModels.UnitManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import logic.Debouncer;
import logic.ProbabilityXDamage;
import logic.UnitAverageDamageCalculator;
import logic.UnitDamageCalculator;

public class Handler {
	// daño medio
	public static ObservableList<AverageDamageDataModel> averageDamageData = FXCollections.observableArrayList();
	
	// resultado simulacion
	// public static ObservableList<SimulationDamageDataModel> simulationDamageData = FXCollections.observableArrayList();
	
	// probabilidad X daño
	public static ObservableList<ProbabilityXDamageDataModel> probXDamageSave2 = FXCollections.observableArrayList();
	public static ObservableList<ProbabilityXDamageDataModel> probXDamageSave3 = FXCollections.observableArrayList();
	public static ObservableList<ProbabilityXDamageDataModel> probXDamageSave4 = FXCollections.observableArrayList();
	public static ObservableList<ProbabilityXDamageDataModel> probXDamageSave5 = FXCollections.observableArrayList();
	public static ObservableList<ProbabilityXDamageDataModel> probXDamageSave6 = FXCollections.observableArrayList();
	public static ObservableList<ProbabilityXDamageDataModel> probXDamageSave7 = FXCollections.observableArrayList();
	
	// probabilidad X o menos daño
	public static ObservableList<ProbabilityXDamageDataModel> probXDamageOrLessSave2 = FXCollections.observableArrayList();
	public static ObservableList<ProbabilityXDamageDataModel> probXDamageOrLessSave3 = FXCollections.observableArrayList();
	public static ObservableList<ProbabilityXDamageDataModel> probXDamageOrLessSave4 = FXCollections.observableArrayList();
	public static ObservableList<ProbabilityXDamageDataModel> probXDamageOrLessSave5 = FXCollections.observableArrayList();
	public static ObservableList<ProbabilityXDamageDataModel> probXDamageOrLessSave6 = FXCollections.observableArrayList();
	public static ObservableList<ProbabilityXDamageDataModel> probXDamageOrLessSave7 = FXCollections.observableArrayList();
	
	// debouncers
	private static final Debouncer simulationDebouncer = new Debouncer(350, TimeUnit.MILLISECONDS);
	private static final Debouncer advancedSimulationDebouncer = new Debouncer(350, TimeUnit.MILLISECONDS);
	private static final Debouncer unitNameChangeDebouncer = new Debouncer(350, TimeUnit.MILLISECONDS);
	 
	// base de datos
	public static boolean connected = true;
	public static ObservableList<UnitDataModel> allUnitsLoaded = FXCollections.observableArrayList(); // Lista de todas las unidades de la base de datos
	public static ObservableList<UnitDataModel> unitToSave = FXCollections.observableArrayList(); // Unidad a guardar
	public static ObservableList<UnitDataModel> unitToDelete= FXCollections.observableArrayList(); // Unidad a eliminar
	public static ObservableList<UnitDataModel> unitToLoad = FXCollections.observableArrayList(); // Unidad a cargar
	
	// daño maximo de la simulacion
	public static double maxDamage;
	public static double tempMaxDamage;
		
	// ejecuta los calculos tras recibir una notificacion de modificación en los datos del escenario de simulacion
	public static void runSimulation() {
		simulationDebouncer.call(() -> {
			// calcular daño promedio
			if (TargetData.target.isWardValueValidation() || !TargetData.target.isWard()) {
				AverageDamageDataModel.loadData(getAllAverageUnitDamage(UnitManager.units));			
			}		
			
		});
		
	}
	
	// ejecutar la simulación avanzada
	public static void runAdvancedSimulation() {
		advancedSimulationDebouncer.call(() -> {
			// calcular daño
			if (TargetData.target.isWardValueValidation() || !TargetData.target.isWard()) {
				ProbabilityXDamage.loadData(getAllUnitDamage(UnitManager.units));	
			}			
		});

	}
	
    // Método para detener adecuadamente el Debouncer cuando la aplicación se cierra
    public static void shutdownDebouncers() {
        simulationDebouncer.shutdown();
        advancedSimulationDebouncer.shutdown();
    }
	
	public static void updateUnitNamesFields() {
		unitNameChangeDebouncer.call(() -> {
			AverageDamageDataModel.updateUnitNames(getAllAverageUnitDamage(UnitManager.units));			
		});
		
	}
	
	// devuelve el daño medio de todas las unidades validas para cada tipo de salvacion
	public static List<double[]> getAllAverageUnitDamage(ObservableList<UnitDataModel> unitList) {
		
		List<double[]> allAverageUnitDamageList = new ArrayList<double[]>();
		
		// determina cuantas unidades validas hay (con nombre y al menos 1 arma)
		for (UnitDataModel unit : unitList) {
			allAverageUnitDamageList.add(getAverageUnitDamage(unit));
		}
		
		return allAverageUnitDamageList;	
	}
	
	// devuelve los resultados de daño de la unidad de la simulacion para cada salvacion
	public static List<double[][]> getAllUnitDamage(ObservableList<UnitDataModel> unitList) {
		
		List<double[][]> allAverageUnitDamageList = new ArrayList<double[][]>();
		
		// determina cuantas unidades validas hay (con nombre y al menos 1 arma)
		for (UnitDataModel unit : unitList) {
			allAverageUnitDamageList.add(getUnitDamage(unit));
		}
		
		return allAverageUnitDamageList;	
	} 
	
	// devuelve los daños promedio de una unidad para cada una de las salvaciones
	public static double[] getAverageUnitDamage(UnitDataModel unidad) {
		return UnitAverageDamageCalculator.unitDamageOutAverage(unidad);
	}
	
	// devuelve los resultados de daño del arma de cada simulacion para cada salvacion
	public static double [][] getUnitDamage(UnitDataModel unidad) {
		double[][] temp = null;
		try {
			temp = UnitDamageCalculator.unitDamageOutMultiThreading(unidad);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		return temp;

	}

	public static ObservableList<AverageDamageDataModel> getAverageDamageData() {
		return averageDamageData;
	}

	public static void setAverageDamageData(ObservableList<AverageDamageDataModel> averageDamageData) {
		Handler.averageDamageData = averageDamageData;
	}

	public static boolean isConnected() {
		return connected;
	}

	public static void setConnected(boolean isConnected) {
		Handler.connected = isConnected;
	}

	public static ObservableList<UnitDataModel> getAllUnitsLoaded() {
		return allUnitsLoaded;
	}

	public static void setAllUnitsLoaded(ObservableList<UnitDataModel> allUnitsLoaded) {
		Handler.allUnitsLoaded = allUnitsLoaded;
	}

	
	

}
