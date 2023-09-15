#!/usr/bin/python3
import bluetooth_utils
import bluetooth_general
import bluetooth_exceptions
import bluetooth_constants
import dbus
import dbus.mainloop.glib
from dbus import ByteArray
import threading
from sys import stdin, stdout
import time
import codecs
from operator import itemgetter, attrgetter

try:
  from gi.repository import GObject
except ImportError:
  import gobject as GObject
  
adapter_interface = None
mainloop = None
thread = None
notifications_callback = None
   
# must set main loop before acquiring SystemBus object
dbus.mainloop.glib.DBusGMainLoop(set_as_default=True)
bus = dbus.SystemBus()
manager = dbus.Interface(bus.get_object(bluetooth_constants.BLUEZ_SERVICE_NAME, "/"), bluetooth_constants.DBUS_OM_IFACE)

def get_owning_uuids(bdaddr, descriptor_path):
    device_proxy = bluetooth_general.getDeviceProxy(bus,bdaddr)
    device_path = device_proxy.object_path

    if not device_proxy.ServicesResolved:
        raise bluetooth_exceptions.StateError(bluetooth_constants.RESULT_ERR_SERVICES_NOT_RESOLVED)
    
    gatt_object = bus.get_object(bluetooth_constants.BLUEZ_SERVICE_NAME,descriptor_path)
    properties_iface = dbus.Interface(gatt_object, bluetooth_constants.DBUS_PROPERTIES)   
    characteristic_path = properties_iface.Get(bluetooth_constants.GATT_DESCRIPTOR_INTERFACE, "Characteristic")
    characteristic_uuid = get_characteristic_uuid(bdaddr, characteristic_path)
    
    gatt_object = bus.get_object(bluetooth_constants.BLUEZ_SERVICE_NAME,characteristic_path)
    properties_iface = dbus.Interface(gatt_object, bluetooth_constants.DBUS_PROPERTIES)   
    service_path = properties_iface.Get(bluetooth_constants.GATT_CHARACTERISTIC_INTERFACE, "Service")
    service_uuid = get_service_uuid(bdaddr, service_path)
    return (service_uuid, characteristic_uuid)
    
    
def get_service_uuid(bdaddr, service_path):
    device_proxy = bluetooth_general.getDeviceProxy(bus,bdaddr)
    device_path = device_proxy.object_path

    if not device_proxy.ServicesResolved:
        raise bluetooth_exceptions.StateError(bluetooth_constants.RESULT_ERR_SERVICES_NOT_RESOLVED)
    
    gatt_object = bus.get_object(bluetooth_constants.BLUEZ_SERVICE_NAME,service_path)
    properties_iface = dbus.Interface(gatt_object, bluetooth_constants.DBUS_PROPERTIES)   
    uuid = properties_iface.Get(bluetooth_constants.GATT_SERVICE_INTERFACE, "UUID")
    return uuid

def get_characteristic_uuid(bdaddr, characteristic_path):
    device_proxy = bluetooth_general.getDeviceProxy(bus,bdaddr)
    device_path = device_proxy.object_path

    if not device_proxy.ServicesResolved:
        raise bluetooth_exceptions.StateError(bluetooth_constants.RESULT_ERR_SERVICES_NOT_RESOLVED)
    
    gatt_object = bus.get_object(bluetooth_constants.BLUEZ_SERVICE_NAME,characteristic_path)
    properties_iface = dbus.Interface(gatt_object, bluetooth_constants.DBUS_PROPERTIES)   
    uuid = properties_iface.Get(bluetooth_constants.GATT_CHARACTERISTIC_INTERFACE, "UUID")
    return uuid
    
def get_descriptor_uuid(bdaddr, descriptor_path):
    device_proxy = bluetooth_general.getDeviceProxy(bus,bdaddr)
    device_path = device_proxy.object_path

    if not device_proxy.ServicesResolved:
        raise bluetooth_exceptions.StateError(bluetooth_constants.RESULT_ERR_SERVICES_NOT_RESOLVED)
    
    gatt_object = bus.get_object(bluetooth_constants.BLUEZ_SERVICE_NAME,descriptor_path)
    properties_iface = dbus.Interface(gatt_object, bluetooth_constants.DBUS_PROPERTIES)   
    uuid = properties_iface.Get(bluetooth_constants.GATT_DESCRIPTOR_INTERFACE, "UUID")
    return uuid    

def get_owning_service_uuid(bdaddr, characteristic_path):
    device_proxy = bluetooth_general.getDeviceProxy(bus,bdaddr)
    device_path = device_proxy.object_path

    if not device_proxy.ServicesResolved:
        raise bluetooth_exceptions.StateError(bluetooth_constants.RESULT_ERR_SERVICES_NOT_RESOLVED)
    
    gatt_object = bus.get_object(bluetooth_constants.BLUEZ_SERVICE_NAME,characteristic_path)
    properties_iface = dbus.Interface(gatt_object, bluetooth_constants.DBUS_PROPERTIES)   
    service_path = properties_iface.Get(bluetooth_constants.GATT_CHARACTERISTIC_INTERFACE, "Service")
    uuid = get_service_uuid(bdaddr, service_path)
    return uuid

def waitForServiceDiscovery(properties_iface):
    services_resolved = properties_iface.Get(bluetooth_constants.DEVICE_INTERFACE, "ServicesResolved")
    while not services_resolved:
        time.sleep(0.5)
        services_resolved = properties_iface.Get(bluetooth_constants.DEVICE_INTERFACE, "ServicesResolved")
    
def get_services(bdaddr):
    bus = dbus.SystemBus()
    device_proxy = bluetooth_general.getDeviceProxy(bus,bdaddr)
    device_path = device_proxy.object_path

    if not bluetooth_general.is_connected(bus,device_path):
        raise bluetooth_exceptions.StateError(bluetooth_constants.RESULT_ERR_NOT_CONNECTED)

    properties_iface = dbus.Interface(device_proxy, bluetooth_constants.DBUS_PROPERTIES)
    services_resolved = properties_iface.Get(bluetooth_constants.DEVICE_INTERFACE, "ServicesResolved")

    if not services_resolved:
        waitForServiceDiscovery(properties_iface)
                    
    services = []
    objects=manager.GetManagedObjects()
    for path, ifaces in objects.items():
        # gatt_service will contain a dictionary of properties relating to that service
        gatt_service = ifaces.get(bluetooth_constants.GATT_SERVICE_INTERFACE)
        if gatt_service is None:
            continue
        elif gatt_service['Device'] == device_path:
            service = {}
            service["path"] = bluetooth_utils.dbus_to_python(path)
            service["UUID"] = bluetooth_utils.dbus_to_python(gatt_service['UUID'])
            services.append(service)
    sorted_services = sorted(services, key=itemgetter('UUID'))            
    return sorted_services

def get_characteristics(bdaddr, service_path):
    bus = dbus.SystemBus()
    device_proxy = bluetooth_general.getDeviceProxy(bus,bdaddr)
    device_path = device_proxy.object_path

    if not bluetooth_general.is_connected(bus,device_path):
        return bluetooth_constants.RESULT_ERR_NOT_CONNECTED
                    
    characteristics = []
    objects=manager.GetManagedObjects()
    for path, ifaces in objects.items():
        gatt_characteristic = ifaces.get(bluetooth_constants.GATT_CHARACTERISTIC_INTERFACE)
        if gatt_characteristic is None:
            continue
        elif gatt_characteristic['Service'] == service_path:
            characteristic = {}
            characteristic["path"] = bluetooth_utils.dbus_to_python(path)
            characteristic["UUID"] = bluetooth_utils.dbus_to_python(gatt_characteristic['UUID'])
            characteristic["service_path"] = bluetooth_utils.dbus_to_python(gatt_characteristic['Service'])
            if 'Notifying' in gatt_characteristic:
                characteristic["notifying"] = bluetooth_utils.dbus_to_python(gatt_characteristic['Notifying'])
            characteristic["properties"] = bluetooth_utils.dbus_to_python(gatt_characteristic['Flags'])
            characteristics.append(characteristic);
    return characteristics

def get_descriptors(bdaddr, characteristic_path):
    bus = dbus.SystemBus()
    device_proxy = bluetooth_general.getDeviceProxy(bus,bdaddr)
    device_path = device_proxy.object_path

    if not bluetooth_general.is_connected(bus,device_path):
        return bluetooth_constants.RESULT_ERR_NOT_CONNECTED
                    
    descriptors = []
    objects=manager.GetManagedObjects()
    for path, ifaces in objects.items():
        gatt_descriptor = ifaces.get(bluetooth_constants.GATT_DESCRIPTOR_INTERFACE)
        descriptor = {}
        if gatt_descriptor is None:
            continue
        elif gatt_descriptor['Characteristic'] == characteristic_path:
            descriptor["path"] = bluetooth_utils.dbus_to_python(path)
            descriptor["UUID"] = bluetooth_utils.dbus_to_python(gatt_descriptor['UUID'])
            descriptor["characteristic_path"] = bluetooth_utils.dbus_to_python(gatt_descriptor['Characteristic'])
            descriptors.append(descriptor);
    return descriptors
    
def read_characteristic(bdaddr, characteristic_path):
    device_proxy = bluetooth_general.getDeviceProxy(bus,bdaddr)
    device_path = device_proxy.object_path

    if not bluetooth_general.is_connected(bus,device_path):
        raise bluetooth_exceptions.StateError(bluetooth_constants.RESULT_ERR_NOT_CONNECTED)
        
    if not device_proxy.ServicesResolved:
        raise bluetooth_exceptions.StateError(bluetooth_constants.RESULT_ERR_SERVICES_NOT_RESOLVED)
    
    characteristic_object = bus.get_object(bluetooth_constants.BLUEZ_SERVICE_NAME,characteristic_path)
    characteristic_iface = dbus.Interface(characteristic_object, bluetooth_constants.GATT_CHARACTERISTIC_INTERFACE) 
    properties_iface = dbus.Interface(characteristic_object, bluetooth_constants.DBUS_PROPERTIES)
    
    characteristic_properties = properties_iface.Get(bluetooth_constants.GATT_CHARACTERISTIC_INTERFACE, "Flags")
    
    if not 'read' in characteristic_properties:
        raise bluetooth_exceptions.UnsupportedError(bluetooth_constants.RESULT_ERR_NOT_SUPPORTED)
  
    raw_value = characteristic_iface.ReadValue(dbus.Array())
    return bluetooth_utils.dbus_to_python(raw_value)

def write_characteristic(bdaddr, characteristic_path, value):
    device_proxy = bluetooth_general.getDeviceProxy(bus,bdaddr)
    device_path = device_proxy.object_path

    if not bluetooth_general.is_connected(bus,device_path):
        raise bluetooth_exceptions.StateError(bluetooth_constants.RESULT_ERR_NOT_CONNECTED)
        
    if not device_proxy.ServicesResolved:
        raise bluetooth_exceptions.StateError(bluetooth_constants.RESULT_ERR_SERVICES_NOT_RESOLVED)
    
    characteristic_object = bus.get_object(bluetooth_constants.BLUEZ_SERVICE_NAME,characteristic_path)
    characteristic_iface = dbus.Interface(characteristic_object, bluetooth_constants.GATT_CHARACTERISTIC_INTERFACE) 
    properties_iface = dbus.Interface(characteristic_object, bluetooth_constants.DBUS_PROPERTIES)
    
    characteristic_properties = properties_iface.Get(bluetooth_constants.GATT_CHARACTERISTIC_INTERFACE, "Flags")
    
    if (not 'write' in characteristic_properties) and (not 'write-without-response' in characteristic_properties):
        raise bluetooth_exceptions.UnsupportedError(bluetooth_constants.RESULT_ERR_NOT_SUPPORTED)
  
    byte_values = codecs.decode(value, 'hex')
    characteristic_iface.WriteValue(byte_values,dbus.Array())
    return bluetooth_constants.RESULT_OK

def properties_changed(interface, changed, invalidated, path):
    if interface != "org.bluez.GattCharacteristic1":
        return

    value = []
    value = changed.get('Value')
    if not value:
        return
    if notifications_callback:
        notifications_callback(path,value)
    stdout.flush()

def stop_handler():
    mainloop.quit()
        
def start_notifications(characteristic_iface):

    bus.add_signal_receiver(properties_changed, bus_name=bluetooth_constants.BLUEZ_SERVICE_NAME,
		dbus_interface=bluetooth_constants.DBUS_PROPERTIES,
		signal_name="PropertiesChanged",
		path_keyword="path")

    bus.add_signal_receiver(stop_handler, "StopNotifications")
    
    characteristic_iface.StartNotify()
    mainloop = GObject.MainLoop()
    mainloop.run()

    
def enable_notifications(bdaddr, characteristic_path, callback):
    global notifications_callback
    global thread
    notifications_callback = callback
    device_proxy = bluetooth_general.getDeviceProxy(bus,bdaddr)
    device_path = device_proxy.object_path

    if not bluetooth_general.is_connected(bus,device_path):
        raise bluetooth_exceptions.StateError(bluetooth_constants.RESULT_ERR_NOT_CONNECTED)
        
    if not device_proxy.ServicesResolved:
        raise bluetooth_exceptions.StateError(bluetooth_constants.RESULT_ERR_SERVICES_NOT_RESOLVED)
    
    characteristic_object = bus.get_object(bluetooth_constants.BLUEZ_SERVICE_NAME,characteristic_path)
    characteristic_iface = dbus.Interface(characteristic_object, bluetooth_constants.GATT_CHARACTERISTIC_INTERFACE) 
    properties_iface = dbus.Interface(characteristic_object, bluetooth_constants.DBUS_PROPERTIES)
    
    characteristic_properties = properties_iface.Get(bluetooth_constants.GATT_CHARACTERISTIC_INTERFACE, "Flags")
    
    if not 'notify' in characteristic_properties and not 'indicate' in characteristic_properties:
        raise bluetooth_exceptions.UnsupportedError(bluetooth_constants.RESULT_ERR_NOT_SUPPORTED)
        
    notifying = properties_iface.Get(bluetooth_constants.GATT_CHARACTERISTIC_INTERFACE, "Notifying")
    if notifying == True:
        raise bluetooth_exceptions.StateError(bluetooth_constants.RESULT_ERR_WRONG_STATE)


    thread = threading.Thread(target=start_notifications, args=(characteristic_iface, ))
    thread.daemon = True
    thread.start()
   
 
def disable_notifications(bdaddr, characteristic_path):
    bluetooth_utils.log("bluetooth_gatt.disable_notifications\n");
    device_proxy = bluetooth_general.getDeviceProxy(bus,bdaddr)
    device_path = device_proxy.object_path

    if not bluetooth_general.is_connected(bus,device_path):
        raise bluetooth_exceptions.StateError(bluetooth_constants.RESULT_ERR_NOT_CONNECTED)
        
    if not device_proxy.ServicesResolved:
        raise bluetooth_exceptions.StateError(bluetooth_constants.RESULT_ERR_SERVICES_NOT_RESOLVED)
    
    characteristic_object = bus.get_object(bluetooth_constants.BLUEZ_SERVICE_NAME,characteristic_path)
    characteristic_iface = dbus.Interface(characteristic_object, bluetooth_constants.GATT_CHARACTERISTIC_INTERFACE) 
    properties_iface = dbus.Interface(characteristic_object, bluetooth_constants.DBUS_PROPERTIES)
    
    characteristic_properties = properties_iface.Get(bluetooth_constants.GATT_CHARACTERISTIC_INTERFACE, "Flags")
    
    if not 'notify' in characteristic_properties and not 'indicate' in characteristic_properties:
        raise bluetooth_exceptions.UnsupportedError(bluetooth_constants.RESULT_ERR_NOT_SUPPORTED)

    notifying = properties_iface.Get(bluetooth_constants.GATT_CHARACTERISTIC_INTERFACE, "Notifying")
    if notifying == False:
        raise bluetooth_exceptions.StateError(bluetooth_constants.RESULT_ERR_WRONG_STATE)

    bluetooth_utils.log("calling StopNotify\n");        
    characteristic_iface.StopNotify()
   
 
