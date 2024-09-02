package controllers;

import org.controlsfx.control.ToggleSwitch;

import dataModels.WeaponDataModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import simulation.Handler;

public class EditWeaponController {
	private WeaponController weaponController;
	private WeaponDataModel weaponData, tempWeapon;
	private Stage editStage;
	private Label weaponNameLabel, numberModelsLabel, numberAttacksLabel, toHitLabel, toWoundLabel, rendLabel, damageLabel;

	// elementos del FXML

	@FXML
	private TextField editWeaponNameTextField, editModelsTextField, editAttacksTextField, editToHitTextField,
			editToWoundTextField, editRendTextField, editDamageTextField;

	@FXML
	private ToggleSwitch editChampionToggleSwitch, editAntiXToggleSwitch, editChargeToggleSwitch,
			editCritImpactsToggleSwitch, editCritWoundsToggleSwitch, editCritMortalToggleSwitch;

	@FXML
	private Button cancelButton, saveButton;

	@FXML
	private Label weaponNameWarningLabel, modelsWarningLabel, attacksWarningLabel, toHitWarningLabel,
			toWoundWarningLabel, rendWarningLabel, damageWarningLabel;

	// FIN elementos del FXML

	public void initialize() {
		weaponNameWarningLabel.setVisible(false);
		modelsWarningLabel.setVisible(false);
		attacksWarningLabel.setVisible(false);
		toHitWarningLabel.setVisible(false);
		toWoundWarningLabel.setVisible(false);
		rendWarningLabel.setVisible(false);
		damageWarningLabel.setVisible(false);

		setUpListeners();
	}
	
	@FXML
	private void handleSave() {
		weaponNameLabel.setText(tempWeapon.getWeaponName()); // aplica cambios en la interfaz
		weaponData.setWeaponName(tempWeapon.getWeaponName()); // aplica cambios en el modelo
		
		numberModelsLabel.setText(String.valueOf(tempWeapon.getModels()));
		weaponData.setModels(tempWeapon.getModels());
		
		numberAttacksLabel.setText(tempWeapon.getAttacks());
		weaponData.setAttacks(tempWeapon.getAttacks());
		
		toHitLabel.setText(String.valueOf(tempWeapon.getToHit()));
		weaponData.setToHit(tempWeapon.getToHit());
		
		toWoundLabel.setText(String.valueOf(tempWeapon.getToWound()));
		weaponData.setToWound(tempWeapon.getToWound());
		
		rendLabel.setText(String.valueOf(tempWeapon.getRend()));
		weaponData.setRend(tempWeapon.getRend());
		
		damageLabel.setText(tempWeapon.getDamage());
		weaponData.setDamage(tempWeapon.getDamage());
		
		weaponData.setChampion(tempWeapon.isChampion());
		weaponData.setAntiX(tempWeapon.isAntiX());
		weaponData.setCharge(tempWeapon.isCharge());
		weaponData.setCritImpacts(tempWeapon.isCritImpacts());
		weaponData.setCritWounds(tempWeapon.isCritWounds());
		weaponData.setCritMortal(tempWeapon.isCritMortal());
		
		editStage.close();
		Handler.runSimulation();
	}
	
	@FXML
	private void handleCancel() {
		editStage.close();
		
		// tambien podria hacerse de la siguiente forma
		// ((Stage) editWeaponNameTextField.getScene().getWindow()).close();
	}

	private void setUpListeners() {
		// asegurarse de que los ToggleSwitch asociados a critImpacts, critWounds y
		// critMortal no estén activos simultaneamente

		editCritImpactsToggleSwitch.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
			if (isSelected && editCritWoundsToggleSwitch.isSelected()) {
				editCritWoundsToggleSwitch.setSelected(false);
			}

			if (isSelected && editCritMortalToggleSwitch.isSelected()) {
				editCritMortalToggleSwitch.setSelected(false);
			}
		});

		editCritWoundsToggleSwitch.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
			if (isSelected && editCritImpactsToggleSwitch.isSelected()) {
				editCritImpactsToggleSwitch.setSelected(false);
			}

			if (isSelected && editCritMortalToggleSwitch.isSelected()) {
				editCritMortalToggleSwitch.setSelected(false);
			}
		});

		editCritMortalToggleSwitch.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
			if (isSelected && editCritWoundsToggleSwitch.isSelected()) {
				editCritWoundsToggleSwitch.setSelected(false);
			}

			if (isSelected && editCritImpactsToggleSwitch.isSelected()) {
				editCritImpactsToggleSwitch.setSelected(false);
			}
		});

		
		// eventos para la actualización de datos
		
		editWeaponNameTextField.textProperty().addListener((obs, oldVal, newVal) -> {
			if (weaponNameValidateInput(newVal)) {
				tempWeapon.setWeaponName(newVal);
			}
		});

		editModelsTextField.textProperty().addListener((obs, oldVal, newVal) -> {
			if (modelsValidateInput(newVal)) {
				tempWeapon.setModels(Integer.valueOf(newVal));
			}
		});

		editAttacksTextField.textProperty().addListener((obs, oldVal, newVal) -> {
			if (attacksValidateInput(newVal)) {
				tempWeapon.setAttacks(newVal);
			}
		});

		editToHitTextField.textProperty().addListener((obs, oldVal, newVal) -> {
			if (toHitValidateInput(newVal)) {
				tempWeapon.setToHit(Integer.valueOf(newVal));
			}
		});

		editToWoundTextField.textProperty().addListener((obs, oldVal, newVal) -> {
			if (toWoundValidateInput(newVal)) {
				tempWeapon.setToWound(Integer.valueOf(newVal));
			}
		});

		editRendTextField.textProperty().addListener((obs, oldVal, newVal) -> {
			if (rendValidateInput(newVal)) {
				tempWeapon.setRend(Integer.valueOf(newVal));
			}
		});

		editDamageTextField.textProperty().addListener((obs, oldVal, newVal) -> {
			if (damageValidateInput(newVal)) {
				tempWeapon.setDamage(newVal);
			}
		});
		
		editChampionToggleSwitch.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
			tempWeapon.setChampion(isSelected);
		});
		
		editAntiXToggleSwitch.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
			tempWeapon.setAntiX(isSelected);
		});
		
		editChargeToggleSwitch.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
			tempWeapon.setCharge(isSelected);
		});
		
		editCritImpactsToggleSwitch.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
			tempWeapon.setCritImpacts(isSelected);
		});
		
		editCritWoundsToggleSwitch.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
			tempWeapon.setCritWounds(isSelected);
		});
		
		editCritMortalToggleSwitch.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
			tempWeapon.setCritWounds(isSelected);
		});
		
		// FIN eventos para la actualización de datos 
	}

	public void setWeapon(WeaponDataModel weaponData) {
		this.weaponData = weaponData;
		this.tempWeapon = new WeaponDataModel();

		editWeaponNameTextField.setText(weaponData.getWeaponName());
		editModelsTextField.setText(String.valueOf(weaponData.getModels()));
		editAttacksTextField.setText(String.valueOf(weaponData.getAttacks()));
		editToHitTextField.setText(String.valueOf(weaponData.getToHit()));
		editToWoundTextField.setText(String.valueOf(weaponData.getToWound()));
		editRendTextField.setText(String.valueOf(weaponData.getRend()));
		editDamageTextField.setText(weaponData.getDamage());
		
		editChampionToggleSwitch.setSelected(weaponData.isChampion());
		editAntiXToggleSwitch.setSelected(weaponData.isAntiX());
		editChargeToggleSwitch.setSelected(weaponData.isCharge());
		editCritImpactsToggleSwitch.setSelected(weaponData.isCritImpacts());
		editCritWoundsToggleSwitch.setSelected(weaponData.isCritWounds());
		editCritMortalToggleSwitch.setSelected(weaponData.isCritMortal());
		
	}
	
	public void setLabelsToChange(Label weaponNameLabel, Label numberModelsLabel, Label numberAttacksLabel, Label toHitLabel, Label toWoundLabel, Label rendLabel, Label damageLabel) {
		this.weaponNameLabel = weaponNameLabel;
		this.numberModelsLabel = numberModelsLabel;
		this.numberAttacksLabel = numberAttacksLabel;
		this.toHitLabel = toHitLabel;
		this.toWoundLabel = toWoundLabel;
		this.rendLabel = rendLabel;
		this.damageLabel = damageLabel;
	}
	
	public void setStage(Stage stage) {
		this.editStage = stage;
	}

	public void setMainController(WeaponController controller) {
		this.weaponController = controller;
	}

	// validaciones de entrada
	private boolean weaponNameValidateInput(String text) {
		if (text.isEmpty()) {
			weaponNameWarningLabel.setVisible(true);
			editWeaponNameTextField.getStyleClass().removeAll("text-field-valid");
			editWeaponNameTextField.getStyleClass().add("text-field-invalid");
			saveButton.setDisable(true);
			return false;
			
		} else {
			weaponNameWarningLabel.setVisible(false);
			editWeaponNameTextField.getStyleClass().removeAll("text-field-invalid");
			editWeaponNameTextField.getStyleClass().add("text-field-valid");
			saveButton.setDisable(false);
			return true;
		}
	}

	private boolean modelsValidateInput(String text) {
		if (modelsValueValidateInput(text)) {
			modelsWarningLabel.setVisible(false);
			editModelsTextField.getStyleClass().removeAll("text-field-invalid");
			editModelsTextField.getStyleClass().add("text-field-valid");
			saveButton.setDisable(false);
			return true;
			
		} else {
			modelsWarningLabel.setVisible(true);
			editModelsTextField.getStyleClass().removeAll("text-field-valid");
			editModelsTextField.getStyleClass().add("text-field-invalid");
			saveButton.setDisable(true);
			return false;
		}
	}

	private boolean attacksValidateInput(String text) {
		if (diceExpresionValidateInput(text)) {
			attacksWarningLabel.setVisible(false);
			editAttacksTextField.getStyleClass().removeAll("text-field-invalid");
			editAttacksTextField.getStyleClass().add("text-field-valid");
			saveButton.setDisable(false);
			return true;
			
		} else {
			attacksWarningLabel.setVisible(true);
			editAttacksTextField.getStyleClass().removeAll("text-field-valid");
			editAttacksTextField.getStyleClass().add("text-field-invalid");
			saveButton.setDisable(true);
			return false;
		}
	}

	private boolean toHitValidateInput(String text) {
		
		if (validarNumeroEntre2a6(text)) {
			toHitWarningLabel.setVisible(false);
			editToHitTextField.getStyleClass().removeAll("text-field-invalid");
			editToHitTextField.getStyleClass().add("text-field-valid");
			saveButton.setDisable(false);
			return true;	
			
		} else {
			toHitWarningLabel.setVisible(true);
			editToHitTextField.getStyleClass().removeAll("text-field-valid");
			editToHitTextField.getStyleClass().add("text-field-invalid");
			saveButton.setDisable(true);
			return false;
		}
	}

	private boolean toWoundValidateInput(String text) {
		if (validarNumeroEntre2a6(text)) {
			toWoundWarningLabel.setVisible(false);
			editToWoundTextField.getStyleClass().removeAll("text-field-invalid");
			editToWoundTextField.getStyleClass().add("text-field-valid");
			saveButton.setDisable(false);
			return true;
			
		} else {
			toWoundWarningLabel.setVisible(true);
			editToWoundTextField.getStyleClass().removeAll("text-field-valid");
			editToWoundTextField.getStyleClass().add("text-field-invalid");
			saveButton.setDisable(true);
			return false;
		}
	}

	private boolean rendValidateInput(String text) {
		
		if (rendValueValidateInput(text)) {
			rendWarningLabel.setVisible(false);
			editRendTextField.getStyleClass().removeAll("text-field-invalid");
			editRendTextField.getStyleClass().add("text-field-valid");
			saveButton.setDisable(false);
			return true;
			
		} else {
			rendWarningLabel.setVisible(true);
			editRendTextField.getStyleClass().removeAll("text-field-valid");
			editRendTextField.getStyleClass().add("text-field-invalid");
			saveButton.setDisable(true);
			return false;	
		}
	}

	private boolean damageValidateInput(String text) {
		if (diceExpresionValidateInput(text)) {
			damageWarningLabel.setVisible(false);
			editDamageTextField.getStyleClass().removeAll("text-field-invalid");
			editDamageTextField.getStyleClass().add("text-field-valid");
			saveButton.setDisable(false);
			return true;
		} else {
			damageWarningLabel.setVisible(true);
			editDamageTextField.getStyleClass().removeAll("text-field-valid");
			editDamageTextField.getStyleClass().add("text-field-invalid");
			saveButton.setDisable(true);
			return false;
		}
	}
	
	private boolean modelsValueValidateInput(String text) {
		try {
			 int value = Integer.parseInt(text);
			 // Verificar si está dentro del rango
			 if (value < 1) {
				// Si el valor está fuera de rango, se rechaza
				return false;
			 } else {
				// Ocultar el label si la entrada es válida

				return true;
			 }		
		} catch (NumberFormatException  e) {
			return false;
		}
	}
	
	private boolean validarNumeroEntre2a6(String text) {
		try {
			 int value = Integer.parseInt(text);
            
			 // Verificar si está dentro del rango
			 if (value < 2 || value > 6) {
				// Si el valor está fuera de rango, se rechaza
				return false;
			 } else {
				// Ocultar el label si la entrada es válida

				return true;
			 }		
		} catch (NumberFormatException  e) {
			return false;
		}
	}
	
	private boolean rendValueValidateInput(String text) {
		try {
			 int value = Integer.parseInt(text);
			 // Verificar si está dentro del rango
			 if (value < 0) {
				// Si el valor está fuera de rango, se rechaza
				return false;
			 } else {
				// Ocultar el label si la entrada es válida

				return true;
			 }		
		} catch (NumberFormatException  e) {
			return false;
		}
	}
	
	private boolean diceExpresionValidateInput(String text) {
		// un monstruo para validar que el usuario introduzca datos váliudo y 
		
		String dicePattern = "^([+-]?\\s*((\\d*\\s*[dD]\\s*\\d+)|(\\d+))(\\s*[+-]\\s*((\\d*\\s*[dD]\\s*\\d+)|(\\d+)))*)$";
		
		return text.matches(dicePattern);
	}
	// FIN validaciones de entrada
}
