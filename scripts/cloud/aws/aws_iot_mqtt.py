import os
import time
import json
from awsiot import mqtt_connection_builder
from awscrt import mqtt

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


# Connecting to IoT Core
print("Connecting... ", end='')
mqtt_connection.connect().result()
print("Done!")


# Subscribing to topics
subscribe_future, packet_id = mqtt_connection.subscribe(
    topic="device/+/data",
    qos=mqtt.QoS.AT_LEAST_ONCE,
    callback=on_message_received
)
subscribe_future.result()
print("Subscribed to topic device/+/data")


subscribe_future, packet_id = mqtt_connection.subscribe(
    topic="device/data/temp",
    qos=mqtt.QoS.AT_LEAST_ONCE,
    callback=on_message_received
)
subscribe_future.result
print("Subscribed to topic device/data/temp")
print("\nListening...")


# Publishing to topics
for i in range(0, 1):
    message = {
        "temperature": 38,
        "humidity": 80,
        "barometer": 1013,
        "wind": {
            "velocity": 22,
            "bearing": 255
        }
    }
    message_json = json.dumps(message)

    mqtt_connection.publish(
        topic="device/32/data",
        payload=message_json,
        qos=mqtt.QoS.AT_LEAST_ONCE
    )

    print(f"Message {i} published")

    time.sleep(1)
try:
    while True:
        pass

except KeyboardInterrupt:
    print("\nDisconnecting... ", end='')
    mqtt_connection.disconnect().result()
    print("Done!")
