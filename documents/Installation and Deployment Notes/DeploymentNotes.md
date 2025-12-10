# Table of Contents
- [Logging Information](#logging-information)
   * [Security Logs](#security-logs)
   * [Event Logs](#event-logs)
   * [Database Logs](#database-logs)
- [List of Dependencies used in this software](#list-of-dependencies-used-in-this-software)
- [Database Table Names](#database-table-names)
- [Starting and connecting to the BRL Database](#starting-and-connecting-to-the-brl-database)
- [Installing MongoDB Community Edition](#installing-mongodb-community-edition)
- [Installing MongoDB Shell (Mongosh)](#installing-mongodb-shell-mongosh)
- [Starting and Connecting to the BRL Database](#starting-and-connecting-to-the-brl-database)
- [Connecting to Mongosh](#connecting-to-mongosh)

## Logging Information
### Security Logs
Located in the root of the project in the log folder. Security logs may be found in "Security.log"

### Event Logs
Located in the root of the project in the log folder. Event logs may be found in "Application.log"

### Database Logs
Database logs contianing logged information from MongoDB is found within the logs folder located within the data folder at the root of the project.

## List of Dependencies used in this software

| Dependencies and Frameworks  |      Version      |
|----------|:-------------:|
| Maven | 3.9.11 |
| Maven JSoup |  1.21.2 |
| Springframework | 3.5.7 |
| Spring Boot | 3.5.7 |
| Java LTS | 25.0.1 |
| MongoDB Community Edition | 8.2.1 |
| MongoDB Shell (Mongosh) | 2.5.9|
| Spring Boot | 3.5.7 |
| Junit | 5.11.0 |
| Apache Commons Lang3 | 3.18.0 |
| Apache Commons Text | 1.10.0 |
| Apache Log4J | 2.25.2 |
| Mockserver Netty | 5.15.0 |

## Database Table Names
- comments
- upvotes
- resources
- flags


## Starting and connecting to the BRL Database
Start the MongoDB server:

`systemctl start mongod`

To ensure the server is running:

`systemctl status mongod`

To stop the server:

`systemctl stop mongod`



## Connecting to Mongosh

Open an additional terminal and log onto the MongoDB shell, Mongosh, using admin privileges to montior the database:

`mongosh --username <username>`

You will be prompted for your password. You must be logged in with admin priviledges to allow other users to connect to your database.