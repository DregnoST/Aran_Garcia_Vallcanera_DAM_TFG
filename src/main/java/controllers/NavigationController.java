package controllers;


import java.io.IOException;

import org.controlsfx.control.ToggleSwitch;
import org.kordamp.ikonli.antdesignicons.AntDesignIconsFilled;
import org.kordamp.ikonli.antdesignicons.AntDesignIconsOutlined;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import dataModels.UnitDataModel;
import dataModels.UnitManager;
import dataModels.WeaponDataModel;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import mongoDB.DatabaseManager;
import mongoDB.DialogMessage;
import simulation.Handler;
import simulation.SimulationMain;
import simulation.TargetData;

public class NavigationController {
	
	// Contenedores Principales
	@FXML
	private AnchorPane homeAnchorPane, simulationsAnchorPane, infoAnchorPane;
	
	@FXML
	private Tooltip homeIconTooltip, simulationsIconTooltip, infoIconTooltip;
	
	// Contenedores secundarios	
	@FXML
	private VBox unitsContainer, averageDamageVBox, simulationScenarioVBox, targetAverageDamageVBox, homeLeftVBox,
			homeRightVBox, simulationsCumulativeLeftVBox, simulationsCumulativeRightVBox,
			simulationsCumulativeCenterVBox, cumulativeLeftCharsVBox, cumulativeRightCharsVBox,
			simulationsDiscreteLeftVBox, simulationsDiscreteRightVBox, simulationsDiscreteCenterVBox, 
			discreteLeftCharsVBox, discreteRightCharsVBox;

	@FXML 
	private HBox homeMainHBox, simulationsCumulativeHBox, cumulativeCharsHBox, simulationsDiscreteHBox, discreteCharsHBox;
	
	@FXML
	private ScrollPane scrollPane;
	
	@FXML
	private Button importUnitButton, addUnitButton;
	
	// Iconos de navegacion y controles
	@FXML
	private FontIcon homeIcon, simulationsIcon, infoIcon, rerunIcon;
	
	// Elementos del Target
	@FXML
	private ToggleSwitch rerollToggleSwitch, rerollOneToggleSwitch, etherealToggleSwitch, antiXTargetToggleSwitch, wardToggleSwitch;
	
	@FXML
	private TextField wardValueTextField;
	
	@FXML
	private Label wardValueWarningLabel, wardValueLabel, simulationPrecisionLabel;
	
	// Pestaña Simulations
	@FXML
	private Slider simulationPrecisionSlider;
	
	
	
	
	// public static boolean wardValueIsValid = true;
	private boolean isUpdateScheduled = false;

	
	public void initialize() {
		// Iconos de navegacion
		// Icono Home
		homeIcon.setIconCode(AntDesignIconsFilled.HOME);
		homeIcon.setIconColor(Color.WHITE);
		homeIcon.setIconSize(45); 
		homeIconTooltip.setShowDelay(Duration.millis(200));
		
		// Icono Simulations
		simulationsIcon.setIconCode(AntDesignIconsFilled.FUND);
		simulationsIcon.setIconColor(Color.WHITE);
		simulationsIcon.setIconSize(45);
		simulationsIconTooltip.setShowDelay(Duration.millis(200));
		
		// Icono Info
		infoIcon.setIconCode(AntDesignIconsFilled.INFO_CIRCLE);
		infoIcon.setIconColor(Color.WHITE);
		infoIcon.setIconSize(45);
		infoIconTooltip.setShowDelay(Duration.millis(200));
		
		// Icono Rerun Simulation
		rerunIcon.setIconCode(AntDesignIconsOutlined.RELOAD);
		rerunIcon.setIconSize(28);
		
		// establecer las proporciones de tamaño de las columnas
		// Home
		homeMainHBox.widthProperty().addListener((obs, oldVal, newVal) -> {
            
			double width = newVal.doubleValue();
            homeLeftVBox.setPrefWidth(width * 0.05);  // 5% del ancho del HBox
            homeRightVBox.setPrefWidth(width * 0.05);  // 5% del ancho del HBox
            simulationScenarioVBox.setPrefWidth(width * 0.45);  // 45% del ancho del HBox
            targetAverageDamageVBox.setPrefWidth(width * 0.45);  // 45% del ancho del HBox
        });
		
		// Pestaña Cumulative dentro de simulations
		simulationsCumulativeHBox.widthProperty().addListener((obs, oldVal, newVal) -> {
            
			double width = newVal.doubleValue();
			simulationsCumulativeLeftVBox.setPrefWidth(width * 0.1);  // 10% del ancho del HBox
			simulationsCumulativeRightVBox.setPrefWidth(width * 0.1);  // 10% del ancho del HBox
			simulationsCumulativeCenterVBox.setPrefWidth(width * 0.8);  // 80% del ancho del HBox
        });
		
		cumulativeCharsHBox.widthProperty().addListener((obs, oldVal, newVal) -> {
            
			double width = newVal.doubleValue();
			cumulativeLeftCharsVBox.setPrefWidth(width * 0.5);
			cumulativeRightCharsVBox.setPrefWidth(width * 0.5);
        });
		
		// Pestaña Discrete dentro de simulations
		simulationsDiscreteHBox.widthProperty().addListener((obs, oldVal, newVal) -> {
            
			double width = newVal.doubleValue();
			simulationsDiscreteLeftVBox.setPrefWidth(width * 0.1);  // 10% del ancho del HBox
			simulationsDiscreteRightVBox.setPrefWidth(width * 0.1);  // 10% del ancho del HBox
			simulationsDiscreteCenterVBox.setPrefWidth(width * 0.8);  // 80% del ancho del HBox
        });
		
		discreteCharsHBox.widthProperty().addListener((obs, oldVal, newVal) -> {
            
			double width = newVal.doubleValue();
			discreteLeftCharsVBox.setPrefWidth(width * 0.5);
			discreteRightCharsVBox.setPrefWidth(width * 0.5);
        });
		
		// SECCION TARGET
		
		wardValueWarningLabel.setVisible(false);
		
		// Vincular el estado del ToggleSwitch que habilita o desabilita el TextField del Ward Value y sus elementos relacionados
		wardValueTextField.disableProperty().bind(Bindings.not(wardToggleSwitch.selectedProperty())); //wardValueTextField.disableProperty().bind(wardToggleSwitch.selectedProperty());
		wardValueWarningLabel.disableProperty().bind(Bindings.not(wardToggleSwitch.selectedProperty()));
		wardValueLabel.disableProperty().bind(Bindings.not(wardToggleSwitch.selectedProperty()));
		
		// Asegurarse de que rerollOneToggleSwitch y rerollOneToggleSwitch no estén activos al mismo tiempo
		// Tambien añadir un listener a la propiedad 'selected' de los ToggleSwitch para actualizar el objeto Target en TargetData
		rerollToggleSwitch.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
			
			if (isSelected && rerollOneToggleSwitch.isSelected()) {
				rerollOneToggleSwitch.setSelected(false);
			} 
			TargetData.target.setRerollSave(isSelected);	
			scheduleSimulation();

        });

		// Asegurarse de que rerollOneToggleSwitch y rerollOneToggleSwitch no estén activos al mismo tiempo
		// Tambien añadir un listener a la propiedad 'selected' de los ToggleSwitch para actualizar el objeto Target en TargetData
		rerollOneToggleSwitch.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
						
			if (isSelected && rerollToggleSwitch.isSelected()) {
            	rerollToggleSwitch.setSelected(false);
            }
			
			TargetData.target.setRerollSaveOfOne(isSelected);	
			scheduleSimulation();

        });
		
		// Añadir un listener a la propiedad 'selected' de los ToggleSwitch para actualizar el objeto Target en TargetData
		etherealToggleSwitch.selectedProperty().addListener((observable, oldValue, newValue) -> {
			
			TargetData.target.setEthereal(newValue);
			Handler.runSimulation();
        });
		
		antiXTargetToggleSwitch.selectedProperty().addListener((observable, oldValue, newValue) -> {
			
			TargetData.target.setAntiXTarget(newValue);
			Handler.runSimulation();
        });
		
		wardToggleSwitch.selectedProperty().addListener((observable, oldValue, newValue) -> {
            
			TargetData.target.setWard(newValue);
            Handler.runSimulation();
        });
				
        // Añadir un listener para validar la entrada cada vez que el texto cambie
		wardValueTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			TargetData.target.setWardValueValidation(wardValueValidateInput(newValue));
			if (TargetData.target.isWardValueValidation()) {
				TargetData.target.setWardValue(Integer.valueOf(newValue));
			}
			Handler.runSimulation();
        });		
		
		
		// SECCION AVERAGE DAMAGE
		
		// añadir tabla de average damage
		addElementToAverageDamageVBox("/fxml/averageDamageTable.fxml");
		
		// añadir grafica de average damage
		addElementToAverageDamageVBox("/fxml/grafica.fxml");
		
		// alade una unidad al escenario de simulacion
		handleAddNewUnit();
		
		
		// SECCION SIMULATIONS
		
		// configurar el slider que controla la precision de la simulacion
		simulationPrecisionSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				simulationPrecisionLabel.setText("" + newValue.intValue());
				SimulationMain.simulaciones = newValue.intValue();
			}
		});
		
		// añadir elementos a la vista
		// cumulative
		addElementToCumulativeLeftChartsVBox("/fxml/cumulativeChartSave2.fxml");
		addElementToCumulativeLeftChartsVBox("/fxml/cumulativeChartSave4.fxml");
		addElementToCumulativeLeftChartsVBox("/fxml/cumulativeChartSave6.fxml");
		
		addElementToCumulativeRightChartsVBox("/fxml/cumulativeChartSave3.fxml");
		addElementToCumulativeRightChartsVBox("/fxml/cumulativeChartSave5.fxml");
		addElementToCumulativeRightChartsVBox("/fxml/cumulativeChartSave7.fxml");
		
		// discrete
		addElementToDiscreteLeftChartsVBox("/fxml/discreteChartSave2.fxml");
		addElementToDiscreteLeftChartsVBox("/fxml/discreteChartSave4.fxml");
		addElementToDiscreteLeftChartsVBox("/fxml/discreteChartSave6.fxml");
		
		addElementToDiscreteRightChartsVBox("/fxml/discreteChartSave3.fxml");
		addElementToDiscreteRightChartsVBox("/fxml/discreteChartSave5.fxml");
		addElementToDiscreteRightChartsVBox("/fxml/discreteChartSave7.fxml");

		
		// boton importar unidad
		FontIcon importIcon = new FontIcon(BootstrapIcons.ARROW_DOWN_SQUARE_FILL);
		importIcon.setIconSize(18);
		importIcon.setIconColor(Color.WHITE);
		importUnitButton.setGraphic(importIcon);

		if (!Handler.isConnected()) {
			importUnitButton.setDisable(true);
			DialogMessage.showAlert("Connection error", "The connection to the database has been lost.\nCheck connection and try again.");
		}
		
		// boton agregar unidad
		FontIcon addIcon = new FontIcon(BootstrapIcons.PLUS_SQUARE_FILL);
		addIcon.setIconSize(18);
		addIcon.setIconColor(Color.WHITE);
		addUnitButton.setGraphic(addIcon);
	}

	@FXML
	public void showHomePane() {
		homeAnchorPane.setVisible(true);
		simulationsAnchorPane.setVisible(false);
		infoAnchorPane.setVisible(false);
	}
	
	@FXML
	public void showSimulationsPane() {
		homeAnchorPane.setVisible(false);
		simulationsAnchorPane.setVisible(true);
		infoAnchorPane.setVisible(false);
		
		Handler.runAdvancedSimulation();
	}
	
	@FXML
	public void showInfoPane() {
		homeAnchorPane.setVisible(false);
		simulationsAnchorPane.setVisible(false);
		infoAnchorPane.setVisible(true);
	}
	
	@FXML
	public void rerunSimulation() {
		Handler.runAdvancedSimulation();
	}
	
	
	@FXML
	private void handleAddNewUnit() {
		// Crear una nueva instancia de UnitDataModel con los datos por defecto
		ObservableList<WeaponDataModel> newWeaponsList = FXCollections.observableArrayList();
		UnitDataModel newUnit = new UnitDataModel("New Unit", newWeaponsList, false);
	    
		// Añade la unidad al modelo de datos centralizado
		UnitManager.addUnit(newUnit);
		
		// Crea la vista de la unidad
		createUnitView(newUnit);
	}
	
	@FXML
	private void importUnit() {
		Handler.allUnitsLoaded.clear();
		
		DatabaseManager dbManager = DatabaseManager.getInstance();  
		
		try {
			Handler.setAllUnitsLoaded(dbManager.loadAllUnits());
		} catch (Exception e) {
			DialogMessage.showAlert("Connection error", "The connection to the database has been lost.\nCheck connection and try again.");
		}
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/loadUnitFrame.fxml"));
			Parent rootImportUnit = loader.load();
			ImportUnitController importController = loader.getController();
			importController.setMainController(this); // Establece la referencia al controlador principal
			
			Stage stage = new Stage();
			stage.setScene(new Scene(rootImportUnit));
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Import Unit");
			importController.setStage(stage); // para poder cerrar la ventana al pulsar un botón
			importController.setMainController(this);
			stage.showAndWait();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadItem(UnitDataModel unit) {
	    
		// Añade la unidad al modelo de datos centralizado
		UnitManager.addUnit(unit);
				
		// Crea la vista de la unidad
		createLoadedUnitView(unit);
	}

	private boolean wardValueValidateInput(String text) {
		
		if (text.isEmpty()) {
			// Mostrar advertencia si el campo está vacío
			wardValueWarningLabel.setVisible(true);
			// wardValueIsValid = false;
			wardValueTextField.getStyleClass().removeAll("text-field-valid");
			wardValueTextField.getStyleClass().add("text-field-invalid");
			TargetData.target.setWardValueValidation(false);
			return false;
		} else {
			try {
				 int value = Integer.parseInt(text);
	             
				 // Verificar si está dentro del rango
				 if (value < 2 || value > 6) {
					// Si el valor está fuera de rango, se rechaza
					wardValueWarningLabel.setVisible(true); // Mostrar el label de advertencia
					wardValueTextField.getStyleClass().removeAll("text-field-valid");
					wardValueTextField.getStyleClass().add("text-field-invalid");
					TargetData.target.setWardValueValidation(false);
					return false;
				 } else {
					// Ocultar el label si la entrada es válida
					wardValueWarningLabel.setVisible(false);
					wardValueTextField.getStyleClass().removeAll("text-field-invalid");
					wardValueTextField.getStyleClass().add("text-field-valid");
					TargetData.target.setWardValueValidation(true);
					return true;
				 }
				
			} catch (NumberFormatException  e) {
				// Si no es un número válido, mostrar la advertencia
				wardValueWarningLabel.setVisible(true); // Mostrar el label de advertencia
				wardValueTextField.getStyleClass().removeAll("text-field-valid");
				wardValueTextField.getStyleClass().add("text-field-invalid");
				TargetData.target.setWardValueValidation(false);
				return false;
			}
		}
	}
	
	// añade la tabla y graficas a la vista principal
	private void addElementToAverageDamageVBox(String source) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(source));
		Node element = null;
		try {
			element = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		averageDamageVBox.getChildren().add(element);
	}
	
	// añade graficas a la columna izquierda de la petaña cumulative de la vista principal simulations
	private void addElementToCumulativeLeftChartsVBox(String source) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(source));
		Node element = null;
		try {
			element = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		cumulativeLeftCharsVBox.getChildren().add(element);
	}
	
	// añade graficas a la columna derecha de la petaña cumulative de la vista principal simulations
	private void addElementToCumulativeRightChartsVBox(String source) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(source));
		Node element = null;
		try {
			element = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		cumulativeRightCharsVBox.getChildren().add(element);
	}
	
	// añade graficas a la columna izquierda de la petaña discrete de la vista principal simulations
	private void addElementToDiscreteLeftChartsVBox(String source) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(source));
		Node element = null;
		try {
			element = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		discreteLeftCharsVBox.getChildren().add(element);
	}
	
	// añade graficas a la columna derecha de la petaña discrete de la vista principal simulations
	private void addElementToDiscreteRightChartsVBox(String source) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(source));
		Node element = null;
		try {
			element = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		discreteRightCharsVBox.getChildren().add(element);
	}
	
	private void createUnitView(UnitDataModel unitData) {
		try {
			// Cargar el FXML para la ficha de unidad
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/unitFrame.fxml"));
			Node unitCard = loader.load(); // Aquí se crea la vista a partir del FXML
			
			// Obtener el controlador y pasarle los datos a la unidad
			UnitController controller = loader.getController();
			controller.setUnitData(unitData); // Configurar los datos de la unidad en el controlador
			
			// Añadir la ficha de unidad a la interfaz de usuario
			unitsContainer.getChildren().add(unitCard);		
			
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	private void createLoadedUnitView(UnitDataModel unitData) {
		try {
			// Cargar el FXML para la ficha de unidad
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/unitFrame.fxml"));
			Node unitCard = loader.load(); // Aquí se crea la vista a partir del FXML
			
			// Obtener el controlador y pasarle los datos a la unidad
			UnitController controller = loader.getController();
			controller.setLoadedUnitData(unitData); // Configurar los datos de la unidad en el controlador
			
			// Añadir la ficha de unidad a la interfaz de usuario
			unitsContainer.getChildren().add(unitCard);		
			
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	// para evitar que Handler.runSimulation() se llame 2 veces segudas de forma innecesarea
	private void scheduleSimulation() {
	    if (!isUpdateScheduled) {
	        isUpdateScheduled = true;

	        Platform.runLater(() -> {
	            Handler.runSimulation();
	            isUpdateScheduled = false;
	        });
	    }
	}
}
