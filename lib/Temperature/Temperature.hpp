#ifndef Temperature_hpp
#define Temperature_hpp

#include <Wire.h>
#include "MAX30105.h"

class TemperatureSensor
{
public:
    TemperatureSensor();
    void begin();
    float readTemperatureC();
    float readTemperatureF();

private:
    MAX30105 particleSensor;
};

#endif
