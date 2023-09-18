import os
import time
import json
from awsiot import mqtt_connection_builder
from awscrt import mqtt
from multiprocessing.connection import Listener

arg_endpoint = os.environ['AWS_DEVICE_ENDPOINT']
arg_cert = os.environ['AWS_DEVICE_CERTIFICATE_PATH']
arg_pri_key = os.environ['AWS_PRIVATE_KEY_PATH']
arg_ca_file = os.environ['AWS_CA_FILE_PATH']
arg_client_id = os.environ['DEVICE_ID']


# Callback when connection is accidentally lost.
def on_connection_interrupted(connection, error, **kwargs):
    print("Connection interrupted. error: {}".format(error))


# Callback when an interrupted connection is re-established.
def on_connection_resumed(connection, return_code, session_present, **kwargs):
    print("Connection resumed. return_code: {} session_present: {}".format(
        return_code, session_present))


# Callback when the connection successfully connects
def on_connection_success(connection, callback_data):
    print("Connection Successful with return code: {} session present: {}".format(
        callback_data.return_code, callback_data.session_present))


# Callback when a connection attempt fails
def on_connection_failure(connection, callback_data):
    print("Connection failed with error code: {}".format(callback_data.error))


# Callback when disconnect
def on_connection_disconnect(connection, callback_data):
    print("Disconnect from cloud")


# Subscribed received message callback
def on_message_received(topic, payload, **kwargs):
    data = json.loads(payload)
    print("Received data: ")
    for key in data:
        print(f"{key}: {data[key]}")
    print()


mqtt_connection = mqtt_connection_builder.mtls_from_path(
    endpoint=arg_endpoint,
    cert_filepath=arg_cert,
    pri_key_filepath=arg_pri_key,
    ca_filepath=arg_ca_file,
    on_connection_interrupted=on_connection_interrupted,
    on_connection_resumed=on_connection_resumed,
    on_connection_success=on_connection_success,
    on_connection_failure=on_connection_failure,
    on_connection_disconnect=on_connection_disconnect,
    client_id=arg_client_id,
    clean_session=False,
    keep_alive_secs=10
)

print("-----Cloud-----")

# Connecting to IoT Core
print("Connecting... ", end='')
mqtt_connection.connect().result()
print("Done!")


# Initialize IPC
bracelet_addr = ('localhost', 6000)
cradle_addr = ('localhost', 6001)

bracelet_lis = Listener(bracelet_addr, authkey=b'pass')
cradle_lis = Listener(cradle_addr, authkey=b'pass')

cradle_conn = cradle_lis.accept()
print("Cradle process accepted")
bracelet_conn = bracelet_lis.accept()
print("Bracelet process accepted")


message = {
    "bracelet": {
        "heart-beats": 90,
        "oxygen": 80,
        "temperature": 36.8
    },
    "cradle": {
        "env-temp": 29.3,
        "humidity": 74.6,
        "cry": "crying"
    }
}

# Publishing data
while True:
    try:
        # Get data from bracelet process
        if bracelet_conn.closed:
            print('Reconnecting to bracelet process...')
            bracelet_conn = bracelet_lis.accept()
            print("Bracelet process accepted")
        while not bracelet_conn.readable:
            pass
        bracelet_msg = bracelet_conn.recv()


        # Get data from cradle process
        if cradle_conn.closed:
            print('Reconnecting to cradle process...')
            cradle_conn = cradle_lis.accept()
            print("Cradle process accepted")
        while not cradle_conn.readable:
            pass
        cradle_msg = cradle_conn.recv()


        # Prepare data for publishing
        message["bracelet"] = json.loads(bracelet_msg)
        message["cradle"] = json.loads(cradle_msg)

        message_json = json.dumps(message)


        # Publish
        mqtt_connection.publish(
            topic="pi/data",
            payload=message_json,
            qos=mqtt.QoS.AT_LEAST_ONCE
        )
        print(f"Message published")

        time.sleep(5)

    except Exception as e:
        print(str(e))

    except KeyboardInterrupt:
        pass

    finally:
        bracelet_conn.close()
        bracelet_lis.close()

        cradle_conn.close()
        cradle_lis.close()

        print("\nDisconnecting... ", end='')
        mqtt_connection.disconnect().result()
        print("Done!")
        break