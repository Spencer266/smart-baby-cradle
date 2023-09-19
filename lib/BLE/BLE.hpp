#ifndef BLE_hpp
#define BLE_hpp

#include <Wire.h>

#include <BLEDevice.h>
#include <BLEUtils.h>
#include <BLEServer.h>

#define SERVICE_UUID "de8a5aac-a99b-c315-0c80-60d4cbb51224"
#define BPM_CHAR_UUID "5b026510-4088-c297-46d8-be6c736a087a"
#define OXY_CHAR_UUID "61a885a4-41c3-60d0-9a53-6d652a70d29c"
#define TEMP_CHAR_UUID "d73e07fd-6061-4c37-80dd-b54224c437e6"

class BLEManager
{
public:
    BLEManager();
    void begin();
    void update(float beatsPerMinute, float temperature,int oxy);

private:
    BLECharacteristic *pBPM_Char;
    BLECharacteristic *pOXY_Char;
    BLECharacteristic *pTEMP_Char;
    long timeSend;
    void setupBLE();
};

#endif
