package com.buzzword;

/*
 * This is free and unencumbered software released into the public domain.
 * Anyone is free to copy, modify, publish, use, compile, sell, or distribute this software,
 * either in source code form or as a compiled binary, for any purpose, commercial or
 * non-commercial, and by any means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors of this
 * software dedicate any and all copyright interest in the software to the public domain.
 * We make this dedication for the benefit of the public at large and to the detriment of
 * our heirs and successors. We intend this dedication to be an overt act of relinquishment in
 * perpetuity of all present and future rights to this software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES
 * OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to: https://unlicense.org/
*/

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
     * Gets a reference to the database connection pool.
     * @return a reference to the database connection pool singleton instance
     * @throws IllegalArgumentException if a configuration field is invalid
     * @throws IOException if the configuration file does not exist
     */
    public static DatabaseConnectionPool getInstance() throws IllegalArgumentException, IOException {
        if (instance == null) {
            instance = new DatabaseConnectionPool();
        }
        return instance;
    }

    /**
     * Constructs a Mongo database client using settings contained in the configuration file.
     * @throws IllegalArgumentException when a config field is invalid
     * @throws IOException when the config file does not exist
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

    /**
     * Closes the MongoDB client and releases all connections.
     * Should be called on application shutdown.
     */
    public void close() {
        if (client != null) {
            client.close();
            logger.info("MongoDB client closed and connections released.");
        }
    }
}