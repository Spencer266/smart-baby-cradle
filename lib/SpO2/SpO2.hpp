#ifndef SpO2_hpp
#define SpO2_hpp

#include <Arduino.h>
#include <Wire.h>
#include "MAX30105.h"
#include "spo2_algorithm.h"

class SpO2Sensor
{
public:
    SpO2Sensor();
    void begin();
    void update();

    int getSpO2();
    float getHeartRate();
    // int getAverageHeartRate();
    float getTemperatureC();

private:
    MAX30105 particleSensor;
    uint32_t irBuffer[100];
    uint32_t redBuffer[100];
    int32_t bufferLength;
    int32_t spo2;
    int8_t validSPO2;
    int32_t heartRate;
    int8_t validHeartRate;
};

#endif
