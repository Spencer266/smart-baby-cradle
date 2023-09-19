#!/bin/bash

# List of Python scripts to run in parallel
scripts=("scripts/cloud/aws/aws_iot_mqtt.py" 
	 "scripts/socket/node_cradle_socket.py" 
	 "scripts/BLE/node_bracelet_ble.py")

# Function to run a Python script in the background
run_script() {
  python3 $1 &
  echo "Started $1"
  sleep 5
}

# Loop through the list of scripts and run them
for script in "${scripts[@]}"; do
  run_script "$script"
done

# Wait for all background processes to finish
wait

echo "All scripts have finished."
