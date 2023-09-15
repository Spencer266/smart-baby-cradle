#!/usr/bin/python3
import bluetooth_constants
import sys
import dbus

adapter_interface = None
mainloop = None
   
def get_adapters():
    # get all objects in the bluez service
    manager_obj = manager.GetManagedObjects()
    # iterate through them
    for path, ifaces in manager_obj.items():
        # if the org.bluez.Adapter1 interface is supported by this object, store its address and path
        if bluetooth_constants.ADAPTER_INTERFACE in ifaces:
            adapter_paths.append(path)
            adapter_addresses.append(
                manager_obj[path][bluetooth_constants.ADAPTER_INTERFACE]['Address'])
                
    return (adapter_paths , adapter_addresses)

def getDeviceProxy(bus,bdaddr):
    manager = dbus.Interface(bus.get_object(bluetooth_constants.BLUEZ_SERVICE_NAME, "/"), bluetooth_constants.DBUS_OM_IFACE)
    objects=manager.GetManagedObjects()
    for path, ifaces in objects.items():
        device = ifaces.get(bluetooth_constants.DEVICE_INTERFACE)
        if device is None:
            continue
        else:
            if device['Address'] == bdaddr:
                device_object = bus.get_object(bluetooth_constants.BLUEZ_SERVICE_NAME, path)
                return dbus.Interface(device_object, bluetooth_constants.DEVICE_INTERFACE)


def is_connected(bus,device_path):
    props = dbus.Interface(bus.get_object(bluetooth_constants.BLUEZ_SERVICE_NAME, device_path),bluetooth_constants.DBUS_PROPERTIES)
    connected = props.Get(bluetooth_constants.DEVICE_INTERFACE, "Connected")
    return connected    
