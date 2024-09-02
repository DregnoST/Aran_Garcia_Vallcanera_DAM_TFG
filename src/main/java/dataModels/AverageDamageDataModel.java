package dataModels;

import java.util.List;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import simulation.Handler;

public class AverageDamageDataModel {
		
	private String unitName;
	private Double  save2;
	private Double  save3;
	private Double  save4;
	private Double  save5;
	private Double  save6;
	private Double  save7;

	public AverageDamageDataModel(String unitName, Double save2, Double save3, Double save4, Double save5, Double save6, Double save7) {
		this.unitName = unitName;
		this.save2 = save2;
		this.save3 = save3;
		this.save4 = save4;
		this.save5 = save5;
		this.save6 = save6;
		this.save7 = save7;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public Double getSave2() {
		return save2;
	}

	public void setSave2(Double save2) {
		this.save2 = save2;
	}

	public Double getSave3() {
		return save3;
	}

	public void setSave3(Double save3) {
		this.save3 = save3;
	}

	public Double getSave4() {
		return save4;
	}

	public void setSave4(Double save4) {
		this.save4 = save4;
	}

	public Double getSave5() {
		return save5;
	}

	public void setSave5(Double save5) {
		this.save5 = save5;
	}

	public Double getSave6() {
		return save6;
	}

	public void setSave6(Double save6) {
		this.save6 = save6;
	}

	public Double getSave7() {
		return save7;
	}

	public void setSave7(Double save7) {
		this.save7 = save7;
	}

	
	public static void loadData(List<double[]> list) {

		Platform.runLater(() -> { // asegura que los cambios se aplican al hilo que gestiona la UI
			Handler.averageDamageData.clear();
			ObservableList<UnitDataModel> unitList = UnitManager.units;
			
			int i = 0;

			// llenar la lista con los datos
			for (UnitDataModel unit : unitList) {
				double[] array = list.get(i);
				Handler.averageDamageData.add(new AverageDamageDataModel(unit.getUnitName(), Math.round(array[0] * 100.0) / 100.0, Math.round(array[1] * 100.0) / 100.0, Math.round(array[2] * 100.0) / 100.0, Math.round(array[3] * 100.0) / 100.0, Math.round(array[4] * 100.0) / 100.0, Math.round(array[5] * 100.0) / 100.0));
				i++;
			}
		});
	}
	
	public static void updateUnitNames(List<double[]> list) {

		Platform.runLater(() -> { // asegura que los cambios se aplican al hilo que gestiona la UI
			Handler.averageDamageData.clear();
			ObservableList<UnitDataModel> unitList = UnitManager.units;
			
			int i = 0;

			// llenar la lista con los datos
			for (UnitDataModel unit : unitList) {
				if (!((unit.getUnitName().isEmpty() || unit.getUnitName().contentEquals("")) && unit.getWeaponListSize() < 1)) {
					double[] array = list.get(i);
					Handler.averageDamageData.add(new AverageDamageDataModel(unit.getUnitName(), Math.round(array[0] * 100.0) / 100.0, Math.round(array[1] * 100.0) / 100.0, Math.round(array[2] * 100.0) / 100.0, Math.round(array[3] * 100.0) / 100.0, Math.round(array[4] * 100.0) / 100.0, Math.round(array[5] * 100.0) / 100.0));
				}
				i++;
			}
		});
	}

	@Override
	public String toString() {
		return "AverageDamageDataModel [unitName=" + unitName + ", save2=" + save2 + ", save3=" + save3 + ", save4="
				+ save4 + ", save5=" + save5 + ", save6=" + save6 + ", save7=" + save7 + "]";
	}
	
	

}
