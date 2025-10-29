## MongoDB Community Edition Installation — Linux


### Go to MongoDB.com and find the download in the MongoDB Community Edition Server Download section. Select version 8.2.1 for the Ubuntu 22.04 x64 platform
https://www.mongodb.com/try/download/community

### Move to the folder with the MongoDB file in it. It is most likely in the downloads folder

`cd ~/Downloads`

### Install the program

`sudo dpkg -i mongodb-org-server_8.2.1_amd64.deb`

### Start the server

`sudo systemctl start mongod`

### Check the status to ensure server is up and running

 `sudo systemctl status mongod`

---

### Directly access records via MongoDB shell (requires additional download)

`mongosh`