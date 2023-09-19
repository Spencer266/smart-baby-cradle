#include "Temperature.hpp"

TemperatureSensor::TemperatureSensor()
{
    // Constructor
}

void TemperatureSensor::begin()
{

    // Initialize sensor
    if (particleSensor.begin(Wire, I2C_SPEED_FAST) == false)
    { // Use default I2C port, 400kHz speed
        Serial.println("MAX30102 was not found. Please check wiring/power. ");
        while (1)
        {
        }
    }

    // The LEDs are very low power and won't affect the temp reading much but
    // you may want to turn off the LEDs to avoid any local heating
    particleSensor.setup(0); // Configure sensor. Turn off LEDs

    particleSensor.enableDIETEMPRDY(); // Enable the temp ready interrupt. This is required.
}

float TemperatureSensor::readTemperatureC()
{
    return particleSensor.readTemperature();
}

float TemperatureSensor::readTemperatureF()
{
    return particleSensor.readTemperatureF();
}
