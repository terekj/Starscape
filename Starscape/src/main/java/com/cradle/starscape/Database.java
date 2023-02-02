package com.cradle.starscape;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Database {

    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection players;

    public final String CONNECTION_URI = "mongodb+srv://orthos:blackflame@data.2ptjm.mongodb.net/myFirstDatabase?retryWrites=true&w=majority";
    public final String DATABASE = "abidan-archive";
    public final String COLLECTION = "sage-players";

    public void connect() {
        client = new MongoClient(new MongoClientURI(CONNECTION_URI));
        database = client.getDatabase(DATABASE);
        players = database.getCollection(COLLECTION);
    }
    public boolean isConnected() {return players != null;}
    public MongoCollection getPlayers() {
        if (isConnected()) {
            return players;
        }
        return null;
    }
    public void disconnect() {
        if (isConnected()) {
            try {
                client.close();
            } catch (MongoException e) {
                e.printStackTrace();
            }
        }
    }

}