package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mongoDB.DatabaseManager;
import mongoDB.DialogMessage;
import simulation.Handler;

import java.io.IOException;


/**
 * JavaFX App
 */

public class App extends Application {

    private static Scene scene;
    public static DatabaseManager dbManager;

    @Override
    public void start(Stage stage) throws IOException {
    	
    	FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/navigation.fxml"));
    	
    	scene = new Scene(fxmlLoader.load(), 1280, 720);
    	stage.setTitle("Numhammer");
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {    	
    	try {
    		dbManager = DatabaseManager.getInstance();    				
		} catch (Exception e) {
			DialogMessage.showAlert("Connection error", "Database connection fail. Database functions will be disabled.");
		}
    	
        launch();
    }
    
    @Override
    public void stop() {
    	try {
			super.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
        Handler.shutdownDebouncers();
        dbManager.close();
        System.out.println("Aplicación detenida. Recursos liberados.");
    }

}