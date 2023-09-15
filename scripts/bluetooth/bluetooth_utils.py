#!/usr/bin/python3
import dbus
import sys
from sys import stdin, stdout

fo = open("log.txt", "a")

def log(line):
    fo.write(line)
    fo.flush()
    
def byteArrayToHexString(bytes):
    hex_string = ""
    for byte in bytes:
        hex_byte = '%02X' % byte
        hex_string = hex_string + hex_byte
    return hex_string

def dbus_to_python(data):
    if isinstance(data, dbus.String):
        data = str(data)
    if isinstance(data, dbus.ObjectPath):
        data = str(data)
    elif isinstance(data, dbus.Boolean):
        data = bool(data)
    elif isinstance(data, dbus.Int64):
        data = int(data)
    elif isinstance(data, dbus.Int32):
        data = int(data)
    elif isinstance(data, dbus.Int16):
        data = int(data)
    elif isinstance(data, dbus.UInt16):
        data = int(data)
    elif isinstance(data, dbus.Byte):
        data = int(data)
    elif isinstance(data, dbus.Double):
        data = float(data)
    elif isinstance(data, dbus.Array):
        data = [dbus_to_python(value) for value in data]
    elif isinstance(data, dbus.Dictionary):
        new_data = dict()
        for key in data.keys():
            new_data[key] = dbus_to_python(data[key])
        data = new_data
    return data
