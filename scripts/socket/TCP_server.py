import socket

# Define host and port
host = '0.0.0.0'  # Listen on all available interfaces
port = 12345  # Choose a suitable port number

# Create a TCP socket
server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# Bind the socket to the host and port
server_socket.bind((host, port))

# Listen for incoming connections
server_socket.listen(1)

print(f"Listening on {host}:{port}")

while True:
    print("Waiting for a client to connect...")
    client_socket, client_address = server_socket.accept()
    print(f"Connected to {client_address}")

    try:
        while True:
            data = client_socket.recv(1024)
            if not data:
                break
            # Process the received data (streaming logic)
            print(f"Received: {data.decode('utf-8')}")
    finally:
        client_socket.close()
        print("Client disconnected")
