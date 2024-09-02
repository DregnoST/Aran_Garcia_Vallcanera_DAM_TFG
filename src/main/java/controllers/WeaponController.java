package controllers;

import java.io.IOException;

import org.controlsfx.control.ToggleSwitch;

import dataModels.WeaponDataModel;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import simulation.Handler;

public class WeaponController {
	
	private WeaponDataModel weaponData;
	private ObservableList<WeaponDataModel> weaponList; // Lista del modelo donde se almacenan las armas
	
	@FXML 
	private VBox root; // contenedor principal de la ficha de unidad

	@FXML
	private Button editButton, deleteButton;
	
	@FXML
	private Label weaponNameLabel, numberModelsLabel, numberAttacksLabel, toHitLabel, toWoundLabel, rendLabel, damageLabel;
	
	@FXML
	private ToggleSwitch enabledToggleSwitch;
	
	@FXML
	private FlowPane weaponStatsFlowPane;
	
	public void initialize() {
						
		// Vincular los elementos al ToggleSwitch
        weaponNameLabel.disableProperty().bind(Bindings.not(enabledToggleSwitch.selectedProperty()));
        weaponStatsFlowPane.disableProperty().bind(Bindings.not(enabledToggleSwitch.selectedProperty()));        
    }
	
	@FXML 
	void handleDelete() {
		// Lógica para eliminar la ficha de la vista principal
        ((VBox)root.getParent()).getChildren().remove(root);
        
        // Lógica adicional para actualizar el modelo o avisar a otros componentes
        weaponList.remove(weaponData); // Elimina el arma de la lista
        Handler.runSimulation();
	}
	
	@FXML
	void editWeapon() {
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/editWeaponFrame.fxml"));
			Parent rootEditWeapon = loader.load();
			EditWeaponController editController = loader.getController();
			editController.setMainController(this); // Establece la referencia al controlador principal
			editController.setWeapon(weaponData); // Pasa el arma a editar
			editController.setLabelsToChange(weaponNameLabel, numberModelsLabel, numberAttacksLabel, toHitLabel, toWoundLabel, rendLabel, damageLabel);
			
			Stage stage = new Stage();
			stage.setScene(new Scene(rootEditWeapon));
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Edit Profile");
			editController.setStage(stage); // para poder cerrar la ventana al pulsar un botón
			stage.showAndWait();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setWeaponData(WeaponDataModel weaponData) {
		this.weaponData = weaponData;
		weaponNameLabel.setText(weaponData.getWeaponName());
		
		enabledToggleSwitch.setSelected(weaponData.isEnabled());
		
		numberModelsLabel.setText(String.valueOf(weaponData.getModels()));
		numberAttacksLabel.setText(weaponData.getAttacks());
		toHitLabel.setText(String.valueOf(weaponData.getToHit()));
		toWoundLabel.setText(String.valueOf(weaponData.getToWound()));
		rendLabel.setText(String.valueOf(weaponData.getRend()));
		damageLabel.setText(weaponData.getDamage());
		
		this.weaponData.setChampion(weaponData.isChampion());
		this.weaponData.setAntiX(weaponData.isAntiX());
		this.weaponData.setCharge(weaponData.isCharge());
		this.weaponData.setCritImpacts(weaponData.isCritImpacts());
		this.weaponData.setCritWounds(weaponData.isCritWounds());
		this.weaponData.setCritMortal(weaponData.isCritMortal());
		
		setupListeners(); // llamamos al metodo para establecer los eventos

		// Añadir listeners si necesario para propagar cambios
	}

	// Vincular la lista de la unidad con la del controlador
	public void setWeaponList(ObservableList<WeaponDataModel> weaponList) {
		this.weaponList = weaponList;	
	}
	
	// Añadimos eventos para actualizar los cambios del usuario en el modelo
    private void setupListeners() {
    	// nombre arma
    	weaponNameLabel.textProperty().addListener((obs, oldVal, newVal) -> {
    		weaponData.setWeaponName(newVal); // Actualiza el modelo cuando el texto cambia
        });
    	
    	// habilitado
    	enabledToggleSwitch.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
    		weaponData.setEnabled(isSelected);
    		Handler.runSimulation();
    	});
    	
    	// numero modelos
    	numberModelsLabel.textProperty().addListener((obs, oldVal, newVal) -> {
    		weaponData.setModels(Integer.valueOf(newVal));
        });
    	
    	// ataques
    	numberAttacksLabel.textProperty().addListener((obs, oldVal, newVal) -> {
    		weaponData.setAttacks(newVal);
        });
    	
    	// impactar
    	toHitLabel.textProperty().addListener((obs, oldVal, newVal) -> {
    		weaponData.setToHit(Integer.valueOf(newVal));
        });
    	
    	// herir
    	toWoundLabel.textProperty().addListener((obs, oldVal, newVal) -> {
    		weaponData.setToWound(Integer.valueOf(newVal));
        });
    	
    	//perforacion
    	rendLabel.textProperty().addListener((obs, oldVal, newVal) -> {
    		weaponData.setRend(Integer.valueOf(newVal));
        });
    	
    	// daño
    	damageLabel.textProperty().addListener((obs, oldVal, newVal) -> {
    		weaponData.setDamage(newVal);
        });
    }
    
    

}
