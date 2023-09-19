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

  HeartRate.begin();

  Temperature.begin();

  BLE.begin();
  Serial.println("Characteristics defined!");
}

void loop()
{
  float temperature = Temperature.readTemperatureC();

  HeartRate.update();
  float heartRate = HeartRate.getHeartRate();

  int oxy = 80;

  BLE.update(heartRate, temperature, oxy);

  Serial.print("temperature: ");
  Serial.print(temperature);

  Serial.print(", BPM: ");
  Serial.print(heartRate);

  Serial.print(", SpO2: ");
  Serial.println(oxy);
}
