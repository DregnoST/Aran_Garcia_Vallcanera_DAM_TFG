package controllers;

import java.io.IOException;

import org.controlsfx.control.ToggleSwitch;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import dataModels.AverageDamageDataModel;
import dataModels.UnitDataModel;
import dataModels.UnitManager;
import dataModels.WeaponDataModel;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import mongoDB.DialogMessage;
import mongoDB.DatabaseManager;
import simulation.Handler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class UnitController {
	
	private UnitDataModel unitData;
	
	@FXML 
	private VBox root; // contenedor principal de la ficha de unidad
	
	@FXML 
	private Label unitNameLabel, unitNameWarningLabel;
	
    @FXML 
    private TextField unitNameTextField;
    
    @FXML 
    private ToggleSwitch hasChargedToggleSwitch;
    
    @FXML
    private Button addWeaponButton, saveUnitButton, deleteUnitButton;
	
	@FXML
    private VBox weaponsContainer;
	
    
    public void initialize() {
    	unitNameWarningLabel.setVisible(false);
    	
    	// validacion cuando se crea una unidad para evitar nombres de unidad iguales.
    	// Si, es una trementa trampa lo que se esta haciendo, espera cuando la taba de daño cambia para hacer la validacion
    	Handler.averageDamageData.addListener((ListChangeListener.Change<? extends AverageDamageDataModel> change) -> {
    		unitNameValidateInput(unitData.getUnitName());
		});
    	
    	// boton guardar
    	if (!Handler.isConnected()) {
    		saveUnitButton.setDisable(true);
    	}
    	
    	// boton agregar arma
    	FontIcon addIcon = new FontIcon(BootstrapIcons.PLUS_SQUARE_FILL);
		addIcon.setIconColor(Color.WHITE);
		addIcon.setIconSize(18);
		addWeaponButton.setGraphic(addIcon);
    }
		
	@FXML 
	void handleDelete() {
		// Lógica para eliminar la ficha de la vista principal
        ((VBox)root.getParent()).getChildren().remove(root);
        
        // Lógica adicional para actualizar el modelo o avisar a otros componentes
        UnitManager.removeUnit(unitData);

	}

	@FXML
	private void handleAddNewWeapon() {
		try {
			// Crear una nueva instancia de UnitDataModel con los datos por defecto
			WeaponDataModel newWeapon = new WeaponDataModel("Weapon profile", 1, "1", 4, 4, 0, "1", false, false, false, false, false, false);
			
			unitData.getWeaponList().add(newWeapon); // Añade el arma a la unidad en el modelo de datos centralizado
			createWeaponView(newWeapon); // Crea la vista de la unidad
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Handler.runSimulation();		
	}
	
	@FXML
	private void saveUnit() {		
		try {
			DatabaseManager dbManager = DatabaseManager.getInstance();
			
			if (dbManager.existsById(unitData.getUnitName())) {
				if (DialogMessage.confirmDialog("Confirm save", unitData.getUnitName() + " alredy exist.\nDo you want to overwrite?")) {
					dbManager.updateUnit(unitData.getUnitName(), unitData);
				}
			} else {
				dbManager.insertUnit(unitData);
			}
		} catch (Exception e) {
			DialogMessage.showAlert("Connection error", "The connection to the database has been lost.\nCheck connection and try again.");
		}

	}
	
	private void handleAddLoadedNewWeapon(UnitDataModel loadedUnitData) {
		try {
			
			for (WeaponDataModel weapon : loadedUnitData.getWeaponList()) {
				createWeaponView(weapon); // Crea la vista de la unidad				
			}			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Handler.runSimulation();		
	}

	private void createWeaponView(WeaponDataModel weaponData) {
		try {
			// Cargar el FXML para la ficha del arma
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/weaponFrame.fxml"));
			Node weaponCard = loader.load(); // Aquí se crea la vista a partir del FXML
			
			// Obtener el controlador y pasarle los datos a al arma
			WeaponController controller = loader.getController();
			controller.setWeaponData(weaponData); // Configurar los datos del arma en el controlador
			controller.setWeaponList(unitData.getWeaponList());
			
			// Añadir el arma a la interfaz de usuario
			weaponsContainer.getChildren().add(weaponCard);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void setUnitData(UnitDataModel unitData) {
		this.unitData = unitData;
		
		// vinculamos los datos de la interfaz con el objeto
		// nombre de la unidad
		unitNameTextField.setText(unitData.getUnitName());
		unitNameTextField.textProperty().addListener((obs, oldVal, newVal) -> {
			 unitData.setUnitName(newVal);
			 unitNameLabel.setText("Unit (" + newVal + ")");
			 
			 Handler.updateUnitNamesFields();
			 
			 
			 unitNameValidateInput(newVal);
		});
		
		// switch
		hasChargedToggleSwitch.setSelected(unitData.isHasCharged());
		hasChargedToggleSwitch.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
			unitData.setHasCharged(isSelected);
			Handler.runSimulation();
		});		
		
		// añade un arma basica a la unidad al crearla
		handleAddNewWeapon();
		
		// Añadir listeners si necesario para propagar cambios
	}
	
	public void setLoadedUnitData(UnitDataModel unitData) {
		this.unitData = unitData;
		
		// vinculamos los datos de la interfaz con el objeto
		// nombre de la unidad
		unitNameTextField.setText(unitData.getUnitName());
		unitNameTextField.textProperty().addListener((obs, oldVal, newVal) -> {
			 unitData.setUnitName(newVal);
			 unitNameLabel.setText("Unit (" + newVal + ")");
			 
			 Handler.updateUnitNamesFields();
			 unitNameValidateInput(newVal);
		});
		
		// switch
		hasChargedToggleSwitch.setSelected(unitData.isHasCharged());
		hasChargedToggleSwitch.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
			unitData.setHasCharged(isSelected);
			Handler.runSimulation();
		});		
		
		// añade un arma basica a la unidad al crearla
		handleAddLoadedNewWeapon(unitData);
		
		// Añadir listeners si necesario para propagar cambios
	}
	
	private void unitNameValidateInput(String text) {
		if (text.isEmpty() || !unitNameUniqueValidation(text)) {
			unitNameWarningLabel.setVisible(true);
			saveUnitButton.setDisable(true);
			unitNameTextField.getStyleClass().removeAll("text-field-valid");
			unitNameTextField.getStyleClass().add("text-field-invalid");
		
		} else {
			unitNameWarningLabel.setVisible(false);
			
			if (Handler.isConnected()) {
				saveUnitButton.setDisable(false);				
			} else {
				saveUnitButton.setDisable(true);
			}
			
			unitNameTextField.getStyleClass().removeAll("text-field-invalid");
			unitNameTextField.getStyleClass().add("text-field-valid");
		}
	}

	// validacion para evitar nombres de unidad iguales
	private boolean unitNameUniqueValidation(String text) {
		int contador = 0;
		
		for (UnitDataModel unit : UnitManager.units) {
			if (unit.getUnitName().equals(text)) {
				contador++;
			}
		}

		if (contador > 1) {
			return false;
		} else {
			return true;
		}
		
	}

}
