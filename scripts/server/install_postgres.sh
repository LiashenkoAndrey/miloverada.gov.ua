#!/bin/bash

isPostgresInstalled() {
  # Check if PostgreSQL is installed
  if command -v psql &> /dev/null; then
    # Get the PostgreSQL version
    pg_version=$(psql --version | awk '{print $3}')
    # Extract the major version number
    pg_major_version=$(echo $pg_version | awk -F. '{print $1}')

    # You can modify this to check for a specific version if needed
    echo "PostgreSQL is installed, version: $pg_version"
    return 0  # Success, PostgreSQL is installed
  else
    echo "PostgreSQL is not installed"
    return 1  # PostgreSQL is not installed
  fi
}

installPostgres() {
  sudo apt update && sudo apt upgrade
  sudo apt install postgresql postgresql-contrib
  sudo systemctl start postgresql
}

if ! isPostgresInstalled; then
  echo "Installing Postgres"

else
  echo "PostgreSQL is already installed"
fi