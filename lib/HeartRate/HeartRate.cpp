#include "HeartRate.hpp"

HeartRateSensor::HeartRateSensor()
{
    // Constructor
}

void HeartRateSensor::begin()
{
    // Initialize sensor
    if (!particleSensor.begin(Wire, I2C_SPEED_FAST))
    {
        Serial.println("MAX30102 was not found. Please check wiring/power. ");
        while (1)
        {
        }
    }
    Serial.println("Place your index finger on the sensor with steady pressure.");

    particleSensor.setup();
    particleSensor.setPulseAmplitudeRed(0x0A);
    particleSensor.setPulseAmplitudeGreen(0);
}

void HeartRateSensor::update()
{
    long irValue = particleSensor.getIR();

    if (checkForBeat(irValue))
    {
        long delta = millis() - lastBeat;
        lastBeat = millis();

        beatsPerMinute = 60 / (delta / 1000.0);

        if (beatsPerMinute < 255 && beatsPerMinute > 20)
        {
            rates[rateSpot++] = (byte)beatsPerMinute;
            rateSpot %= RATE_SIZE;

            beatAvg = 0;
            for (byte x = 0; x < RATE_SIZE; x++)
                beatAvg += rates[x];
            beatAvg /= RATE_SIZE;
        }
    }
}

int HeartRateSensor::getHeartRate()
{
    HeartRateSensor::update();
    
    return (int)beatsPerMinute;
}

int HeartRateSensor::getAverageHeartRate()
{
    HeartRateSensor::update();
    return beatAvg;
}

bool HeartRateSensor::checkForBeat(long irValue)
{
    if (irValue < 50000)
    {
        Serial.println(" No finger?");
        return false;
    }

    return true;
}
