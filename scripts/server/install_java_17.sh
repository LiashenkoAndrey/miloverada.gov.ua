#!/bin/bash

isJava17Installed() {
  if command -v java &> /dev/null; then
      echo "Java is installed."

      # Get the Java version
      version=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')

      # Check if the version is above 17
      if [[ "$version" > "17" ]]; then
          echo "Java version is above 17: $version"
          return 0
          # Proceed with your next commands here
      else
          echo "Java version is $version, which is not above 17."
          return 0
      fi
  else
      echo "Java is not installed."
      return 0
  fi
}

installJava17() {
 if isJava17Installed; then
    echo 'Java 17 is installed'
    else
      sudo apt install openjdk-17-jdk
  fi
}
