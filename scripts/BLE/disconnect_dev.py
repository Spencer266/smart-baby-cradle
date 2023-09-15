import sys
sys.path.insert(0, '../bluetooth')
import bluetooth_gap
import bluetooth_constants

mac = sys.argv[1]

result = bluetooth_gap.disconnect(mac)
print(result)
