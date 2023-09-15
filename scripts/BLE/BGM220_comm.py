import bluetooth_exceptions
import bluetooth_utils
import bluetooth_gap
import bluetooth_constants
import bluetooth_gatt
import sys
import time
import json

sys.path.insert(0, '../bluetooth')

DEV_ADDR = "60:A4:23:C9:7B:02"
SERVICE_UUID = "de8a5aac-a99b-c315-0c80-60d4cbb51224"
LED_CHAR_UUID = "5b026510-4088-c297-46d8-be6c736a087a"
BTN_CHAR_UUID = "61a885a4-41c3-60d0-9a53-6d652a70d29c"


def BTN_NotifyCallback(path, value):
    print("Button: " + str(bluetooth_utils.byteArrayToHexString(value)))


status = bluetooth_constants.RESULT_EXCEPTION
while status != bluetooth_constants.RESULT_OK:
    status = bluetooth_gap.connect(DEV_ADDR)
    # print("repeat " + str(status))
    time.sleep(0.5)

print("BGM Connected: " + str(status))

# Get service
services = bluetooth_gatt.get_services(DEV_ADDR)
primary = [service for service in services if service["UUID"] == SERVICE_UUID][0]

# Get characteristics
chars = bluetooth_gatt.get_characteristics(DEV_ADDR, primary["path"])
led_char = [char for char in chars if char["UUID"] == LED_CHAR_UUID][0]
btn_char = [char for char in chars if char["UUID"] == BTN_CHAR_UUID][0]

# Operations
pack = 0

# Enable notification on btn_char
try:
    bluetooth_gatt.enable_notifications(
        DEV_ADDR, btn_char['path'], BTN_NotifyCallback)
    print("Button notification enabled")
except bluetooth_exceptions.StateError as e:
    print(e.args[0])
except bluetooth_exceptions.UnsupportedError as e:
    print(e.args[0])


while True:
    # value = bluetooth_gatt.read_characteristic(DEV_ADDR, btn_char['path'])
    # print(value)

    pack = pack ^ 1
    bluetooth_gatt.write_characteristic(
        DEV_ADDR, led_char['path'], '0' + str(pack))
    time.sleep(1)
