module com.arangarciavallcanera.numhammer {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
	requires javafx.graphics;
	requires transitive org.controlsfx.controls;
	requires atlantafx.base;
	//requires mongo.java.driver;
	requires org.mongodb.driver.sync.client;
	requires transitive org.mongodb.bson;
	requires org.mongodb.driver.core;
	requires org.kordamp.ikonli.javafx;
	requires org.kordamp.ikonli.antdesignicons;
	requires org.kordamp.ikonli.bootstrapicons;
	

	// paquete de controladores
    opens controllers to javafx.fxml;
    exports controllers;

    // Apaquete que contiene el metodo main
    opens main to javafx.fxml;
    exports main;
    
    opens dataModels to javafx.base;
    exports dataModels;


    //opens com.arangarciavallcanera.numhammer to javafx.fxml;
    //exports com.arangarciavallcanera.numhammer;
}
