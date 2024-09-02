package dataModels;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import simulation.Handler;

public class UnitManager {
	public static ObservableList<UnitDataModel> units = FXCollections.observableArrayList();
	public static ObservableList<UnitDataModel> databaseUnitsLoad = FXCollections.observableArrayList();
	
	
    public ObservableList<UnitDataModel> getUnits() {
        return units;
    }

    public static void addUnit(UnitDataModel unit) {
        units.add(unit);
        //Handler.runSimulation();
    }

    public static void removeUnit(UnitDataModel unit) {
        units.remove(unit);
        Handler.runSimulation();

    }
}
