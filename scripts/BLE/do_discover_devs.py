# import libraries
import json
import sys
sys.path.insert(0, '../bluetooth')

import bluetooth_gap

devs = bluetooth_gap.discover_devices(2000)
print(json.dumps(devs))

# if 'REQUEST_METHOD' in os.environ:
# result = {}
# if os.environ['REQUEST_METHOD'] != 'GET':
# print('Status 405: Method Not Allowed')
# print()
# print("Status-Line: HTTP/1.0 405 Method Not Allowed")
# print()
# else:
# print("Content-Type: application/json;charst=utf-8")
# print()
# querystring = cgi.FieldStorage()
# if not "scantime" in querystring:
# result['result'] = bluetooth_constants.RESULT_ERR_BAD_ARGS
# print(json.JSONEncoder().encode(result))

# else:
# scantime = querystring.getfirst("scantime", "2000")
# devs = bluetooth_gap.discover_devices(int(scantime))
# print(json.JSONEncoder().encode(devs))

# else:
# print("ERROR: Not called by HTTP")

# 22:22:57:D2:CF:77 Redmi
# 60:A4:23:C9:7B:02 Blinky
