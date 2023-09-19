#include <Arduino.h>

#include "Temperature.hpp"
TemperatureSensor Temperature;

#include "HeartRate.hpp"
HeartRateSensor HeartRate;

#include "BLE.hpp"
BLEManager BLE;

#include "SpO2.hpp"
SpO2Sensor SpO2;

void setup()
{
  // put your setup code here, to run once:
  Serial.begin(115200);

  HeartRate.begin();

  Temperature.begin();

  // SpO2.begin();

  BLE.begin();
  Serial.println("Characteristics defined!");
}

void loop()
{
  // float temperature = Temperature.readTemperatureC();

  // HeartRate.update();
  // float heartRate = HeartRate.getHeartRate();
  // int spO2 = 80;


  SpO2.update();
  float temperature = SpO2.getTemperatureC();
  float heartRate = SpO2.getHeartRate();
  int spO2 = SpO2.getSpO2();



  BLE.update(heartRate, temperature, spO2);

  Serial.print("temperature: ");
  Serial.print(temperature);

  Serial.print(", BPM: ");
  Serial.print(heartRate);

  Serial.print(", SpO2: ");
  Serial.println(spO2);
}
