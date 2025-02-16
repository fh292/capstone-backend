#!/bin/bash

# Database configuration
DB_NAME="capstone"
DB_USER="postgres"
BACKUP_DIR="./backups"
DATE=$(date +"%Y%m%d_%H%M%S")
BACKUP_FILE="$BACKUP_DIR/backup_$DATE.sql"

# Create backup directory if it doesn't exist
mkdir -p $BACKUP_DIR

# Create backup
echo "Creating backup of $DB_NAME database..."
PGPASSWORD=$DB_PASSWORD pg_dump -h localhost -U $DB_USER -d $DB_NAME -F p > "$BACKUP_FILE"

if [ $? -eq 0 ]; then
    echo "Backup completed successfully: $BACKUP_FILE"
else
    echo "Backup failed!"
    exit 1
fi