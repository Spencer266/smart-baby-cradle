import json
import os
import socket
import sys

sys.path.append('scripts/ML')
from predict import Predictor
from multiprocessing.connection import Client

FILE_PATH = 'data/FTP/files/record.wav'

print("-----Node Socket-----")

# Set up TCP socket
host = ('0.0.0.0', 6005)

server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)       # Create a TCP socket
server_socket.bind(host)                                                # Bind the socket to the host and port
server_socket.listen(1)                                                 # Listen for incoming connections

print("Waiting for TCP client...")
client_socket, client_address = server_socket.accept()
print(f"Connected to {client_address}")
print()

# Initialize IPC
cloud_proc_addr = ('localhost', 6001)
cloud_proc_conn = Client(cloud_proc_addr, authkey=b'pass')


# Cry machine learning model setup
predictor = Predictor()


while True:
    try:
        data = client_socket.recv(256)
        env_temp = float(data.decode('utf-8'))
        client_socket.send(bytes('\n', "utf-8"))

        data = client_socket.recv(256)
        humidity = float(data.decode('utf-8'))
        client_socket.send(bytes('\n', "utf-8"))

        if os.path.exists(FILE_PATH):
            cry = 'crying' if predictor.predict() == 1 else 'noises'
            os.remove(FILE_PATH)
        else:
            cry = "no record"

        data = {
            "env-temp": env_temp,
            "humidity": humidity,
            "cry": cry
        }

        print(data)

        pack = json.dumps(data)
        cloud_proc_conn.send(pack)

    except KeyboardInterrupt:
        cloud_proc_conn.close()
        client_socket.close()