package dataModels;

import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UnitDataModel {

	// Propiedades de arma
	private String unitName;
	private ObservableList<WeaponDataModel> weaponList;
	
	// Condiciones
	private boolean hasCharged;
	
	// Constructores
	public UnitDataModel() {
		super();
		this.unitName = "Unit";
		this.weaponList = null;
		this.hasCharged = false;
	}
	
	public UnitDataModel(String unitName) {
		super();
		this.unitName = unitName;
		this.weaponList = null;
		this.hasCharged = false;
	}

	public UnitDataModel(String unitName, ObservableList<WeaponDataModel> weaponList, boolean hascharged) {
		super();
		this.unitName = unitName;
		this.weaponList = weaponList;
		this.hasCharged = hascharged;
	}

	// Gettesrs y Setters
	public String getUnitName() {
		return unitName;
	}


	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}


	public ObservableList<WeaponDataModel> getWeaponList() {
		return weaponList;
	}


	public void setWeaponList(ObservableList<WeaponDataModel> weaponList) {
		this.weaponList = weaponList;
	}


	public boolean isHasCharged() {
		return hasCharged;
	}


	public void setHasCharged(boolean hascharged) {
		this.hasCharged = hascharged;
	}
	
	public int getWeaponListSize() {
		return this.weaponList.size();
	}
	
	public Document toDocument() {
        return new Document("_id", unitName)
                .append("weaponList", weaponList.stream().map(WeaponDataModel::toDocument).collect(Collectors.toList()))
                .append("hasCharged", hasCharged);
    }

	
	public static UnitDataModel fromDocument(Document doc) {
		
		// Obtener la lista de Document y la convertimos a ObservableList<WeaponDataModel>
		List<Document> weaponDoc = doc.getList("weaponList", Document.class);
		
		ObservableList<WeaponDataModel> weapons = FXCollections.observableArrayList();
		
		if (weaponDoc != null) {
			weapons.addAll(weaponDoc.stream()
					.map(WeaponDataModel::fromDocument)
					.collect(Collectors.toList()));
		}
		
		return new UnitDataModel(
					doc.getString("_id"),
					weapons, 
					doc.getBoolean("hasCharged")
				);
	}
}
