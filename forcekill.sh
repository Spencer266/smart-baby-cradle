#!/bin/bash

# List of process names you want to send Ctrl+C to
process_names=("node_bracelet_ble.py" "node_cradle_socket.py" "aws_iot_mqtt.py")

# Iterate through the list of process names
for process_name in "${process_names[@]}"; do
  # Use pgrep to find the PIDs of processes matching the name
  pids=$(pgrep -f "$process_name")
  
  # Check if any PIDs were found
  if [ -z "$pids" ]; then
    echo "No processes matching '$process_name' found."
  else
    # Send Ctrl+C signal to each PID
    for pid in $pids; do
      kill -9 "$pid"
      echo "Sent Ctrl+C signal to process $pid ($process_name)."
    done
  fi
done

