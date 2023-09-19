#ifndef HeartRate_hpp
#define HeartRate_hpp

#include <Wire.h>
#include "MAX30105.h"

#define RATE_SIZE  4

class HeartRateSensor
{
public:
    HeartRateSensor();
    void begin();
    void update();
    int getHeartRate();
    int getAverageHeartRate();

private:
    MAX30105 particleSensor;
    // const byte RATE_SIZE = 4;
    byte rates[RATE_SIZE];
    byte rateSpot;
    long lastBeat;
    float beatsPerMinute;
    int beatAvg;
    bool checkForBeat(long irValue);
};

#endif
