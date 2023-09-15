import sys
sys.path.insert(0, '../bluetooth')
import bluetooth_gatt
import bluetooth_constants

baddr = "60:A4:23:C9:7B:02"
service_path = "/org/bluez/hci0/dev_60_A4_23_C9_7B_02/service0019"
char_UUID = "5b026510-4088-c297-46d8-be6c736a087a"



chars = bluetooth_gatt.get_characteristics(baddr, service_path)
print(chars)
print()

for char in chars:
	if char["UUID"] == char_UUID:
		value = bluetooth_gatt.read_characteristic(baddr, char["path"])
		print(value)

