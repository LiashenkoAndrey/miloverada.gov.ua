#!/bin/bash

# Define the service file path
SERVICE_FILE="/etc/systemd/system/miloverada.service"

# Create the service file with the specified content
cat <<EOL > $SERVICE_FILE
[Unit]
Description=miloverada.gov.ua backend server
After=network.target
StartLimitIntervalSec=0

[Service]
Type=simple
WorkingDirectory=/root/miloverada.gov.ua
User=root
ExecStart=sudo /root/miloverada.gov.ua/mvnw org.springframework.boot:spring-boot-maven-plugin:run -Dspring-boot.run.profiles=prod

[Install]
WantedBy=multi-user.target
EOL

# Optionally, enable and start the service
systemctl enable miloverada.service
systemctl start miloverada.service

echo "Service file created at $SERVICE_FILE"
