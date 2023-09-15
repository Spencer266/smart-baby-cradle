import bluetooth_constants
import bluetooth_general
import bluetooth_exceptions
import bluetooth_utils
import sys
import dbus
import dbus.mainloop.glib
from dbus import ByteArray

try:
    from gi.repository import GObject
except ImportError:
    import gobject as GObject

adapter_interface = None
mainloop = None
timer_id = None
devices = {}


def interfaces_added(path, interfaces):
    if not bluetooth_constants.DEVICE_INTERFACE in interfaces:
        return
    properties = interfaces[bluetooth_constants.DEVICE_INTERFACE]
    if path in devices:
        dev = devices[path]
        devices[path] = dict(devices[path].items() + properties.items())
    else:
        devices[path] = properties
    if "Address" in devices[path]:
        address = properties["Address"]
    else:
        address = "<unknown>"


def properties_changed(interface, changed, invalidated, path):
    if interface != bluetooth_constants.DEVICE_INTERFACE:
        return
    if path in devices:
        dev = devices[path]
        devices[path] = dict(devices[path].items())
        devices[path].update(changed.items())
    else:
        devices[path] = changed

    if "Address" in devices[path]:
        address = devices[path]["Address"]
    else:
        address = "<unknown>"


def discovery_timeout():
    global adapter_interface
    global mainloop
    global timer_id
    GObject.source_remove(timer_id)
    mainloop.quit()
    adapter_interface.StopDiscovery()
    bus = dbus.SystemBus()
    bus.remove_signal_receiver(interfaces_added, "InterfacesAdded")
    bus.remove_signal_receiver(properties_changed, "PropertiesChanged")
    return True


def discover_devices(timeout):
    global adapter_interface
    global mainloop
    global timer_id
    adapter_paths = []
    adapter_addresses = []
    selected_adapter_path = ""
    selected_adapter_addr = ""

    selected_adapter_path = bluetooth_constants.BLUEZ_NAMESPACE + \
        bluetooth_constants.ADAPTER_NAME

    # dbus initialisation steps
    dbus.mainloop.glib.DBusGMainLoop(set_as_default=True)
    bus = dbus.SystemBus()
    manager = dbus.Interface(
        bus.get_object(bluetooth_constants.BLUEZ_SERVICE_NAME, '/'),
        bluetooth_constants.DBUS_OM_IFACE)

    # acquire the adapter interface so we can call its methods
    adapter_object = bus.get_object(
        bluetooth_constants.BLUEZ_SERVICE_NAME, selected_adapter_path)
    adapter_interface = dbus.Interface(
        adapter_object, bluetooth_constants.ADAPTER_INTERFACE)

    # register signal handler functions so we can asynchronously report discovered devices
    bus.add_signal_receiver(interfaces_added,
                            dbus_interface=bluetooth_constants.DBUS_OM_IFACE,
                            signal_name="InterfacesAdded")

    bus.add_signal_receiver(properties_changed,
                            dbus_interface=bluetooth_constants.DBUS_PROPERTIES,
                            signal_name="PropertiesChanged",
                            arg0=bluetooth_constants.DEVICE_INTERFACE,
                            path_keyword="path")

    objects = manager.GetManagedObjects()
    for path, interfaces in objects.items():
        if bluetooth_constants.DEVICE_INTERFACE in interfaces:
            interfaces_added(path, interfaces)

    mainloop = GObject.MainLoop()
    timer_id = GObject.timeout_add(timeout, discovery_timeout)
    adapter_interface.StartDiscovery(byte_arrays=True)

    mainloop.run()
    device_list = devices.values()
    discovered_devices = []
    for device in device_list:
        dev = {}
        if 'Address' in device:
            dev['bdaddr'] = bluetooth_utils.dbus_to_python(device['Address'])
        if 'Name' in device:
            dev['name'] = bluetooth_utils.dbus_to_python(device['Name'])
        if 'ServicesResolved' in device:
            dev['services_resolved'] = bluetooth_utils.dbus_to_python(
                device['ServicesResolved'])
        if 'Appearance' in device:
            dev['appearance'] = bluetooth_utils.dbus_to_python(
                device['Appearance'])
        if 'Paired' in device:
            dev['paired'] = bluetooth_utils.dbus_to_python(device['Paired'])
        if 'Connected' in device:
            dev['connected'] = bluetooth_utils.dbus_to_python(
                device['Connected'])
        if 'UUIDs' in device:
            dev['UUIDs'] = bluetooth_utils.dbus_to_python(device['UUIDs'])
        if 'RSSI' in device:
            dev['RSSI'] = bluetooth_utils.dbus_to_python(device['RSSI'])
        if 'AdvertisingFlags' in device:
            dev['ad_flags'] = bluetooth_utils.byteArrayToHexString(
                device['AdvertisingFlags'])
        if 'ManufacturerData' in device:
            dev['ad_manufacturer_data_cid'] = int(
                list(device['ManufacturerData'].keys())[0])
            dev['ad_manufacturer_data'] = bluetooth_utils.byteArrayToHexString(
                list(device['ManufacturerData'].values())[0])
        if 'ServiceData' in device:
            dev['ad_service_data_uuid'] = bluetooth_utils.dbus_to_python(
                list(device['ServiceData'].keys())[0])
            dev['ad_service_data'] = bluetooth_utils.byteArrayToHexString(
                list(device['ServiceData'].values())[0])
        discovered_devices.append(dev)

    return discovered_devices


def connect(bdaddr):
    bus = dbus.SystemBus()
    device_proxy = bluetooth_general.getDeviceProxy(bus, bdaddr)
    if device_proxy == None:
        return bluetooth_constants.RESULT_ERR_NOT_FOUND
    device_path = device_proxy.object_path
    if not bluetooth_general.is_connected(bus, device_path):
        try:
            device_proxy.Connect()
        except Exception as e:
            print(str(e))
            return bluetooth_constants.RESULT_EXCEPTION
        return bluetooth_constants.RESULT_OK
    else:
        return bluetooth_constants.RESULT_OK


def disconnect(bdaddr):
    bus = dbus.SystemBus()
    device_proxy = bluetooth_general.getDeviceProxy(bus, bdaddr)
    device_path = device_proxy.object_path

    if bluetooth_general.is_connected(bus, device_path):
        try:
            device_proxy.Disconnect()
        except Exception as e:
            return bluetooth_constants.RESULT_EXCEPTION
        return bluetooth_constants.RESULT_OK
    else:
        return bluetooth_constants.RESULT_OK
