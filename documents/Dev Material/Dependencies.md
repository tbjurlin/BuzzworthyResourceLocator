# List of Dependencies used in this software

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
Go to MongoDB.com under community downloads
https://www.mongodb.com/try/download/community

Select the correct version. We're using MongoDB version 8.2.1 on the Debian 22.04 x64 platform.

Change to the folder with the MongoDB file in it:

`Cd ~/Downloads`

Install the program:

`sudo dpkg -i mongodb-org-server_8.2.1_amd64.deb`

To start a new server instance locally:

`sudo systemctl start mongod`



