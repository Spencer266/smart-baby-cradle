# importing libraries
import sys
sys.path.insert(0, '../bluetooth')

from multiprocessing.connection import Client
import bluetooth_gatt
import bluetooth_constants
import bluetooth_gap
import bluetooth_utils
import bluetooth_exceptions
import time
import json
import struct

# Defines constants
DEV_ADDR = "E0:5A:1B:C8:36:CE"
SERVICE_UUID = "de8a5aac-a99b-c315-0c80-60d4cbb51224"
BPM_CHAR_UUID = "5b026510-4088-c297-46d8-be6c736a087a"
OXY_CHAR_UUID = "61a885a4-41c3-60d0-9a53-6d652a70d29c"
TEMP_CHAR_UUID = "d73e07fd-6061-4c37-80dd-b54224c437e6"
IPC_PORT = 6000

# Connect BLE
BLE_status = bluetooth_constants.RESULT_EXCEPTION
while BLE_status != bluetooth_constants.RESULT_OK:
    BLE_status = bluetooth_gap.connect(DEV_ADDR)
    print("repeat " + str(BLE_status))
    time.sleep(1)
print("Bracelet Connected: " + str(BLE_status))

# Initialize IPC
proc_addr = ('localhost', IPC_PORT)
proc_conn = Client(proc_addr, authkey=b'pass')

# Getting service and characteristics
services = bluetooth_gatt.get_services(DEV_ADDR)
primary = [service for service in services if service["UUID"] == SERVICE_UUID][0]

chars = bluetooth_gatt.get_characteristics(DEV_ADDR, primary["path"])
bpm_char = [char for char in chars if char["UUID"] == BPM_CHAR_UUID][0]
oxy_char = [char for char in chars if char["UUID"] == OXY_CHAR_UUID][0]
temp_char = [char for char in chars if char["UUID"] == TEMP_CHAR_UUID][0]


data = {
    "bpm": 90,
    "oxy": 80,
    "temp": 36.8
}

while True:
    raw = bytes(bluetooth_gatt.read_characteristic(
        DEV_ADDR, bpm_char["path"]))
    data["bpm"] = struct.unpack('<f', raw)[0]

    raw = bluetooth_gatt.read_characteristic(
        DEV_ADDR, oxy_char["path"])
    data["oxy"] = int.from_bytes(raw, byteorder='little', signed=False)

    raw = bytes(bluetooth_gatt.read_characteristic(
        DEV_ADDR, temp_char["path"]))
    data["temp"] = struct.unpack('<f', raw)[0]

    try:
        pack = json.dumps(data)
        proc_conn.send(pack)
        time.sleep(2)
    except KeyboardInterrupt:
        proc_conn.close()
        bluetooth_gap.disconnect(DEV_ADDR)
        break
