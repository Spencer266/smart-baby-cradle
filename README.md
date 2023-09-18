# Gateway Source code
Hardware: Raspberry Pi 4 Model B 4GB RAM
OS: Raspberry Pi OS 64 bit

Main script categories:
1. BLE
- node_bracelet_ble.py: BLE communication with bracelet end device and send data to IPC pipe
- Other scripts for testing purpose

2. Socket
- node_cradle_ble.py: Communication with cradle end device using TCP socket and send data to IPC pipe
- Other script for testing purpose

3. ML (Machine Learning)
- predict.py: module for machine learning using trained KNN model to predict baby cry from record file sent by end device

4. Cloud
- aws_iot_mqtt.py: Connect to AWS IoT Core, open IPC pipe to receive data from node processes, pack up data and publish to the cloud using MQTT protocol