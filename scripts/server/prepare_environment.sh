#!/bin/bash

source ./install_postgres.sh
source ./install_java_17.sh
source ./create_and_configure_miloverada_service.sh

createService
installJava17
installPostgres
