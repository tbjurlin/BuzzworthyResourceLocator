
package com.buzzword;

import com.mongodb.client.MongoClients;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.connection.ConnectionPoolSettings;

import java.io.IOException;



public class DatabaseConnectionPool {

    /**
     * The current instance of the connection pool singleton.
     */
    private static DatabaseConnectionPool instance;

    private final DatabaseConfiguration config = new DatabaseConfigurationImpl(ConfigurationManagerImpl.getInstance());

    private final Logger logger = LoggerFactory.getEventLogger();

    private MongoClient client;

    /**
     * Gets an a reference to the database connection pool.
     * @return a refernece to the database connection pool singleton instance
     * @throws IllegalArgumentException
     * @throws IOException
     */
    public static DatabaseConnectionPool getInstance() throws IllegalArgumentException, IOException {
        if (instance == null) {
            instance = new DatabaseConnectionPool();
        }
        return instance;
    }

    /**
     * Constructs a mongo database client using settings contained in the configuration file.
     * @throws IllegalArgumentException when config field is invalid.
     * @throws IOException when config file does not exist.
     */
    private DatabaseConnectionPool() throws IllegalArgumentException, IOException {

        ConnectionPoolSettings poolSettings = ConnectionPoolSettings.builder()
            .maxSize(config.getMaxDatabaseConnections())
            .minSize(config.getMinDatabaseConnections())
            .build();

        logger.debug("Configured connection pool.");

        MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(new ConnectionString(config.getDatabaseConnectionString()))
            .applyToConnectionPoolSettings(builder -> builder.applySettings(poolSettings))
            .build();

        logger.debug("Configured Mongo client.");

        client = MongoClients.create(settings);
        logger.info("Acquired database connection pool.");
    };

    /**
     * Gets a database connection from the Mongo connection pool.
     * @return a database connection
     */
    public MongoDatabase getDatabaseConnection() {
        MongoDatabase db = client.getDatabase(config.getDatabaseName());
        logger.info(String.format("Acquired database %s", config.getDatabaseName()));
        return db;
    }
    
}
