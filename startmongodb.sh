#!/bin/bash

# Load environment variables
if [ -f .env ]; then
    source .env
else
    echo "Error: .env file not found"
    exit 1
fi

# Create data directory if it doesn't exist
mkdir -p "${MONGO_DATA_DIR:-./data/db}"

# Start MongoDB with network listening
mongod --bind_ip_all \
    --port "${MONGO_PORT:-27017}" \
    --dbpath "${MONGO_DATA_DIR:-./data/db}" \
    --logpath "${MONGO_LOG_DIR:-./data/logs}/mongodb.log" \
    --fork

# Check if MongoDB started successfully
if [ $? -eq 0 ]; then
    echo "MongoDB started successfully on port ${MONGO_PORT:-27017}"
    echo "Listening on all network interfaces"
else
    echo "Failed to start MongoDB"
    exit 1
fi