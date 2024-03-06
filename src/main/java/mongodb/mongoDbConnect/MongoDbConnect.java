package mongodb.mongoDbConnect;

import com.mongodb.*;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import mongodb.mongoDbConfig.MongoDbConfig;
import org.bson.Document;


public class MongoDbConnect implements MongoDbConfig {
    public static MongoDatabase getConnection() {
        MongoDatabase con = null;
        MongoClientURI uri = new MongoClientURI(MONGO_URI);
        try (MongoClient mongoClient = new MongoClient(uri)) {
            con = mongoClient.getDatabase(DB_NAME);
        } catch (Exception e) {
            System.err.println("Connection Failed! Check output console");
            System.err.println(e.getMessage());
            return con;
        }
        return con;
    }

    public static MongoCollection<Document> getCollections(String collectionName) {
        MongoClientURI uri = new MongoClientURI(MONGO_URI);
        try (MongoClient mongoClient = new MongoClient(uri)) {
            MongoDatabase database = mongoClient.getDatabase(DB_NAME);
            return database.getCollection(collectionName);
        }
    }

    public static void main(String[] args) {
        /** Kết nối database */
        MongoClientURI uri = new MongoClientURI(MONGO_URI);
        try (MongoClient mongoClient = new MongoClient(uri)) {
            /** Lấy ra một MongoDatabase từ kết nối */
            MongoDatabase database = mongoClient.getDatabase(DB_NAME);

            /** Lấy ra một MongoCollection từ database */
            MongoCollection<Document> collection = database.getCollection("course");
            FindIterable<Document> documents = collection.find();

            try (MongoCursor<Document> cursor = documents.iterator()) {
                while (cursor.hasNext()) {
                    Document document = cursor.next();
                    System.out.println(document.toJson());
                }
            }
        }
    }
}