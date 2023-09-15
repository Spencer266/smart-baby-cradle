import time
import json
from multiprocessing.connection import Client

proc_addr = ('localhost', 6001)

proc_conn = Client(proc_addr, authkey=b'pass')

while True:
    try:
        data = {
            "env-temp": 27.3,
            "humidity": 74.6,
            "cry": "loud"
        }

        pack = json.dumps(data)
        proc_conn.send(pack)
        time.sleep(2)
    except KeyboardInterrupt:
        proc_conn.send("cclose")
        proc_conn.close()
        break