# Table of Conents
- [Deployment Notes](#deployment-notes)
- [Starting and connecting to the BRL Database](#starting-and-connecting-to-the-brl-database)
   * [Database Tables](#database-tables)
   * [List of Dependencies used in this software](#list-of-dependencies-used-in-this-software)
   * [Installing MongoDB Community Edition](#installing-mongodb-community-edition)
   * [Installing MongoDB Shell (Mongosh)](#installing-mongodb-shell-mongosh)
   * [Logging Information](#logging-information)
      + [Security Logs](#security-logs)
      + [Event Logs](#event-logs)
      + [Database Logs](#database-logs)

## Starting and connecting to the BRL Database
Start the server by using:

`sudo ./startmongodb.sh`

Open an additional terminal and log onto the MongoDB shell, Mongosh, using admin privileges to montior the database:

`mongosh --username <username>`

You will be prompted for your password. You must be logged in with admin priviledges to allow other users to connect to your database.


## Database Tables
- comments
- upvotes
- resources


## List of Dependencies used in this software

| Dependency   |      Version      |
|----------|:-------------:|
| JSoup |  1.21.2 |
| Springframework | 3.5.7 |
| Spring Boot | 3.5.7 |
| Maven | 3.9.11 |
| Java | 25.0.1 |
| MongoDB Community Edition | 8.2.1 |
| MongoDB Shell (Mongosh) | 2.5.9|
| Spring Boot | 3.5.7 |
| Junit | 5.11.0 |
| Apache Commons Lang3 | 3.18.0 |
| Apache Commons Text | 1.10.0 |
| Mockserver Netty | 5.15.0 |

## Installing MongoDB Community Edition
Select the correct version of MongoDB. We're using MongoDB version 8.2.1 on the Debian 22.04 x64 platform, server package.

https://repo.mongodb.org/apt/debian/dists/bookworm/mongodb-org/8.2/main/binary-amd64/mongodb-org-server_8.2.1_amd64.deb

Change to the folder with the MongoDB file in it:

`Cd ~/Downloads`

Install the program:

`sudo dpkg -i mongodb-org-server_8.2.1_amd64.deb`

To start a new server instance locally:

`sudo systemctl start mongod`

## Installing MongoDB Shell (Mongosh)

Follow the provided link to MongoDB and find the MongoDB Shell download. We will be using version 2.5.9 for Debian/Ubuntu machines.

https://downloads.mongodb.com/compass/mongodb-mongosh_2.5.9_amd64.deb

Following this link will download the file to your system. Double click the folder you downloaded. It will most likely have a name similar to

Double click the folder you downloaded to bring up the software installation window. Select "install software" and continue.

Ensure Mongosh is installed by running the following command in your terminal:

`mongosh --version`

## Logging Information
### Security Logs
* Located in the root of the project in the log folder. Security logs may be found in "Security.log"

### Event Logs
* Located in the root of the project in the log folder. Event logs may be found in "Application.log"

### Database Logs
* Database logs contianing logged information from MongoDB is found within the logs folder located within the data folder at the root of the project.