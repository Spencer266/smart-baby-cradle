#include "BLE.hpp"

float beatsPerMinute;
int beatAvg;
int oxy;
char buf[5];

float temperature;

BLEManager::BLEManager()
{
    // Constructor
}

void BLEManager::begin()
{
    setupBLE();
    // Serial.println("Characteristics defined!");
}

void BLEManager::setupBLE()
{

    BLEDevice::init("BLE_Bracelet");

    BLEServer *pServer = BLEDevice::createServer();
    BLEService *pService = pServer->createService(SERVICE_UUID);

    pBPM_Char = pService->createCharacteristic(
        BPM_CHAR_UUID,
        BLECharacteristic::PROPERTY_READ |
            BLECharacteristic::PROPERTY_WRITE);

    pOXY_Char = pService->createCharacteristic(
        OXY_CHAR_UUID,
        BLECharacteristic::PROPERTY_READ |
            BLECharacteristic::PROPERTY_WRITE);

    pTEMP_Char = pService->createCharacteristic(
        TEMP_CHAR_UUID,
        BLECharacteristic::PROPERTY_READ |
            BLECharacteristic::PROPERTY_WRITE);

    pService->start();

    BLEAdvertising *pAdvertising = pServer->getAdvertising();
    pAdvertising->addServiceUUID(SERVICE_UUID);
    pAdvertising->setScanResponse(true);
    pAdvertising->setMinPreferred(0x06);
    pAdvertising->setMinPreferred(0x12);
    pAdvertising->start();

    
}

void BLEManager::update(int beatsPerMinute, float temperature,int oxy)
{
    if (millis() - timeSend > 5000)
    {
        pBPM_Char->setValue(beatsPerMinute);
        pTEMP_Char->setValue(temperature);
        pOXY_Char->setValue(oxy);

        timeSend = millis();
    }
}
