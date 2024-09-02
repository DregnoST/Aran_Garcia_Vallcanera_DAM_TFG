package mongoDB;

import com.mongodb.client.MongoClients;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import dataModels.UnitDataModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import simulation.Handler;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bson.Document;
import org.bson.conversions.Bson;

public class DatabaseManager {
	
	private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "numhammer";
    private static final String COLLECTION_NAME = "units";
    private static DatabaseManager instance;
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    
    // Constructor privado para evitar la instanciación externa
    private DatabaseManager() {
    	try {
    		System.err.println("Conectando...");
    		
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(CONNECTION_STRING))
                    .applyToSocketSettings(builder -> builder
                        .connectTimeout(5, TimeUnit.SECONDS)) // Establece un tiempo de espera de conexión corto
                    .applyToClusterSettings(builder -> builder
                        .serverSelectionTimeout(5, TimeUnit.SECONDS)) // Establece un tiempo de espera de selección de servidor corto
                    .build();
    		
    		mongoClient = MongoClients.create(settings);
    		database = mongoClient.getDatabase(DATABASE_NAME);
    		testConnection(); // comprobacion proactiva de la conexion		
		} catch (MongoException e) {
			// Manejo de la excepción si no se puede conectar a la base de datos
            System.err.println("No se pudo conectar a la base de datos. Por favor, verifica que el servicio esté activo.");
		}
    	
    }
    
    // constructor con parametros para cuando estos no son los conocidos (EN UN FUTURO)
    private DatabaseManager(String uri, String dbName) {
    	try {
    		this.mongoClient = MongoClients.create(uri);
    		this.database = mongoClient.getDatabase(dbName);
			
		} catch (MongoException e) {
			// Manejo de la excepción si no se puede conectar a la base de datos
            System.err.println("No se pudo conectar a la base de datos. Por favor, verifica que el servicio esté activo.");
		}
    }
    
    // Método estático para obtener la instancia con parametros para cuando estos no son los conocidos (EN UN FUTURO)
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    // Método estático para obtener la instancia con parametros para cuando estos no son los conocidos (EN UN FUTURO)
    public static synchronized DatabaseManager getInstance(String uri, String dbName) {
        if (instance == null) {
            instance = new DatabaseManager(uri, dbName);
        }
        return instance;
    }
    
    private void testConnection() {
    	try {
		    mongoClient.listDatabaseNames().first(); // Esta línea lanza una excepción si la conexión falla
		    System.out.println("Conexión exitosa a MongoDB.");
		    Handler.setConnected(true);
		} catch (Exception e) {
			Handler.setConnected(false);			
		    System.err.println("MongoDB no está disponible: " + e.getMessage());
		    e.printStackTrace();
		}
    }
    
    // guardar uno
    public void insertUnit(UnitDataModel unit) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        collection.insertOne(unit.toDocument());
    }
    
    // cargar uno
    public UnitDataModel loadUnit(String id) {
    	MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
    	//Document doc = collection.find(new Document("_id", "Warrior")).first();
    	Document doc = collection.find(new Document("_id", id)).first();
        return doc != null ? UnitDataModel.fromDocument(doc) : null;
    }
    
    // cargar todo
    public ObservableList<UnitDataModel> loadAllUnits() {
    	ObservableList<UnitDataModel> units = FXCollections.observableArrayList();
    	units.clear();
    	MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
    	
    	// No se aplica ningún filtro, por lo que se recuperan todos los documentos
    	FindIterable<Document> documents = collection.find();
        for (Document doc : documents) {
            units.add(UnitDataModel.fromDocument(doc)); // Convertir cada documento a UnitDataModel
        }
        
        return units;
    }
    
    // eliminar uno
    public void deleteUnit(String id) {
    	MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
    	DeleteResult result = collection.deleteOne(new Document("_id", id));
    }
    
    // eliminar todo
    public void deleteAllUnits() {
    	MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
    	
    	// No se aplica ningún filtro, por lo que se eliminan todos los documentos  	
    	DeleteResult result = collection.deleteMany(new Document());

    }
    
    public boolean existsById(String id) {
    	MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
    	long count = collection.countDocuments(Filters.eq("_id", id));
    	return count > 0;
    }
    
    public boolean updateUnit(String id, UnitDataModel unit) {
    	MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
    	Document doc = unit.toDocument();
    	UpdateResult result = collection.replaceOne(Filters.eq("_id", id), doc);
    	
    	if (result.getModifiedCount() > 0) {
			return true;
		} else {
			
			DialogMessage.showAlert("Save alert", "No changes required or connection error.");
            return false;
		}
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

}
