#!/bin/bash

# Load environment variables
source .env

# Test local connection
echo "Testing local MongoDB connection..."
mongosh --eval "db.runCommand({ ping: 1 })"

# Get current IP address
CURRENT_IP=$(hostname -I | awk '{print $1}')

echo -e "\nMongoDB is running on: ${CURRENT_IP}:${MONGO_PORT:-27017}"
echo "To connect from other VMs, use:"
echo "mongosh \"mongodb://${CURRENT_IP}:${MONGO_PORT:-27017}\""
echo -e "\nTo test from other VMs, run:"
echo "mongosh \"mongodb://${CURRENT_IP}:${MONGO_PORT:-27017}\" --eval \"db.runCommand({ ping: 1 })\""