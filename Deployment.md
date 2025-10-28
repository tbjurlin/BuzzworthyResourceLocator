### MongoDB Community Edition Installation

<b>Go to MongoDB.com under community downloads<b>
https://www.mongodb.com/try/download/community

<b> Select the correct version. We're using MongoDB version 8.2.1 on the Ubuntu 22.04 x64 platform. <b>

<b>Change to the folder with the MongoDB file in it<b>
- Cd ~/Downloads

<b>Install the program<b>
- sudo dpkg -i mongodb-org-server_8.2.1_amd64.deb

<b>Start the server<b>
- sudo systemctl start mongod

<b>Check the status to ensure server is up and running<b>
- sudo systemctl status mongod

<b>Directly access records via MongoDB shell (requires additional download)<b>
- mongosh