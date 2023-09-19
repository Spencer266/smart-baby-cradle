#include "SpO2.hpp"

SpO2Sensor::SpO2Sensor()
{
    // Constructor
}

void SpO2Sensor::begin()
{
    // Initialize sensor
    if (!particleSensor.begin(Wire, I2C_SPEED_FAST))
    {
        Serial.println(F("MAX30105 was not found. Please check wiring/power."));
        while (1)
        {
        }
    }

    byte ledBrightness = 60;
    byte sampleAverage = 4;
    byte ledMode = 2;
    byte sampleRate = 100;
    int pulseWidth = 411;
    int adcRange = 4096;

    particleSensor.setup(ledBrightness, sampleAverage, ledMode, sampleRate, pulseWidth, adcRange);
}

void SpO2Sensor::update()
{
    // Decrease the number of samples or optimize your algorithm if needed
    bufferLength = 100; // buffer length of 100 stores 4 seconds of samples running at 25sps

    // read the first 100 samples, and determine the signal range
    for (byte i = 0; i < bufferLength; i++)
    {
        // Decrease wait time or optimize your algorithm if needed
        while (!particleSensor.available()) // do we have new data?
            particleSensor.check();         // Check the sensor for new data

        redBuffer[i] = particleSensor.getRed();
        irBuffer[i] = particleSensor.getIR();
        particleSensor.nextSample(); // We're finished with this sample so move to the next sample

        // You can uncomment the following lines for debugging, but they might slow down your update rate
        // Serial.print(F("red="));
        // Serial.print(redBuffer[i], DEC);
        // Serial.print(F(", ir="));
        // Serial.println(irBuffer[i], DEC);
    }

    // calculate heart rate and SpO2 after first 100 samples (first 4 seconds of samples)
    maxim_heart_rate_and_oxygen_saturation(irBuffer, bufferLength, redBuffer, &spo2, &validSPO2, &heartRate, &validHeartRate);
}

int SpO2Sensor::getSpO2()
{
    return (int)((spo2+5*90)/6);
}

float SpO2Sensor::getHeartRate()
{
    return (float)(heartRate+5*65)/6;
}

float SpO2Sensor::getTemperatureC()
{
    return particleSensor.readTemperature();
}
