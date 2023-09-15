import sys
sys.path.insert(0, '../bluetooth')
import bluetooth_gap
import bluetooth_constants
import time

mac = sys.argv[1]

result = 7
while result != 0:
    result = bluetooth_gap.connect(mac)
    print("repeat " + str(result))
    time.sleep(1)
print(result)
