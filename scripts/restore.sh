#!/bin/bash

# Check if backup file is provided
if [ -z "$1" ]; then
    echo "Please provide the backup file path"
    echo "Usage: ./restore.sh <backup_file>"
    exit 1
fi

BACKUP_FILE=$1

# Database configuration
DB_NAME="capstone"
DB_USER="postgres"

# Check if backup file exists
if [ ! -f "$BACKUP_FILE" ]; then
    echo "Backup file not found: $BACKUP_FILE"
    exit 1
fi

# Restore backup
echo "Restoring backup to $DB_NAME database..."
echo "This will overwrite the existing database. Are you sure? (y/n)"
read -r confirm

if [ "$confirm" = "y" ]; then
    # Drop and recreate database
    PGPASSWORD=$DB_PASSWORD psql -h localhost -U $DB_USER -d postgres -c "DROP DATABASE IF EXISTS $DB_NAME;"
    PGPASSWORD=$DB_PASSWORD psql -h localhost -U $DB_USER -d postgres -c "CREATE DATABASE $DB_NAME;"

    # Restore from backup
    PGPASSWORD=$DB_PASSWORD psql -h localhost -U $DB_USER -d $DB_NAME < "$BACKUP_FILE"

    if [ $? -eq 0 ]; then
        echo "Restore completed successfully!"
    else
        echo "Restore failed!"
        exit 1
    fi
else
    echo "Restore cancelled"
    exit 1
fi