import sys
sys.path.insert(0, '../bluetooth')
import bluetooth_gatt
import bluetooth_constants

mac = sys.argv[1]

services = bluetooth_gatt.get_services(mac)
print(services)
