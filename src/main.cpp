#include <Arduino.h>

#include "Temperature.hpp"
TemperatureSensor Temperature;

#include "HeartRate.hpp"
HeartRateSensor HeartRate;

#include "BLE.hpp"
BLEManager BLE;

void setup()
{
  // put your setup code here, to run once:
  Serial.begin(115200);

  Temperature.begin();
  
  HeartRate.begin();

  BLE.begin();
  Serial.println("Characteristics defined!");
}

void loop()
{
  float temperature = Temperature.readTemperatureC();
  int heartRate = HeartRate.getAverageHeartRate();

  int oxy = 80;

  BLE.update(temperature, heartRate, oxy);
}
