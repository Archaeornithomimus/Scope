#include <Arduino_HTS221.h> // temperature and humidity

#include <Arduino_LPS22HB.h> // pressure

#include <Arduino_APDS9960.h> // light Ambient sensor

#include <Arduino_LSM9DS1.h> // gyroscope

#include <math.h>





/*
  BLE_Peripheral.ino

  This program uses the ArduinoBLE library to set-up an Arduino Nano 33 BLE 
  as a peripheral device and specifies a service and a characteristic. Depending 
  of the value of the specified characteristic, an on-board LED gets on. 

  The circuit:
  - Arduino Nano 33 BLE. 

  This example code is in the public domain.
*/

#include <ArduinoBLE.h>

float pressure = 0;
float ambientLight = 0;
float temperature = 0;
float humidity = 0;

float pressureNew = 0;
int ambientLightNew = 0;
float temperatureNew = 0;
float humidityNew = 0;

int a,r,g,b;
/*enum {
  GESTURE_NONE  = -1,
  GESTURE_UP    = 0,
  GESTURE_DOWN  = 1,
  GESTURE_LEFT  = 2,
  GESTURE_RIGHT = 3
};*/

const char* deviceServiceScopeUuid = "19b10000-e8f2-537e-4f6c-d104768a1214";
const char* deviceServiceScopeBatterySaverUuid = "785e42d2-6b22-45eb-9eb3-a9ef223b2dc7";

const char* temperatureCharacteristicUuid = "9909114d-c725-43a0-bec8-6a3f24dc4821";
const char* humidityCharacteristicUuid = "5cc78f66-9b56-457a-b939-94c1f9a3cb74";
const char* orientationCharacteristicUuid = "5a43d045-9de2-4ae6-a9d7-2253b1f1cb5c";
const char* trackerCharacteristicUuid = "d1324be7-4635-439c-99de-8068140a65a0";
const char* ambientLightCharacteristicUuid = "36fcdd66-54ee-473b-bad2-2739bdab983c";
const char* batteryStatusCharacteristicUuid = "df9a5b68-3484-444e-8b35-a9b6b5e8bb06";

//int gesture = -1;
String tracker;
String orientation;

float currentAzimuth = 0;
float batteryStatus = 0;
float xSensorCompass, ySensorCompass, zSensorCompass;
float heading = 0;

//BLEService gestureService(deviceServiceUuid);
BLEService scopeService(deviceServiceScopeUuid);
BLEService scopeBatterySaverService(deviceServiceScopeUuid);

BLEFloatCharacteristic temperatureCharacteristic(temperatureCharacteristicUuid, BLENotify);
BLEFloatCharacteristic humidityCharacteristic(humidityCharacteristicUuid, BLENotify);
BLEStringCharacteristic orientationCharacteristic(orientationCharacteristicUuid, BLENotify,20);
BLEStringCharacteristic trackerCharacteristic(trackerCharacteristicUuid, BLERead | BLEWrite,20); //peut etre juste Write
BLEIntCharacteristic ambientLightCharacteristic(ambientLightCharacteristicUuid, BLENotify);
BLEIntCharacteristic batteryStatusCharacteristic(batteryStatusCharacteristicUuid, BLENotify);

void setup() {
  Serial.begin(9600);
  while (!Serial);  
  
  pinMode(LEDR, OUTPUT);
  pinMode(LEDG, OUTPUT);
  pinMode(LEDB, OUTPUT);
  pinMode(LED_BUILTIN, OUTPUT);
  
  digitalWrite(LEDR, LOW);
  digitalWrite(LEDG, HIGH);
  digitalWrite(LEDB, HIGH);
  digitalWrite(LED_BUILTIN, LOW);

  
  if (!BLE.begin()) {
    Serial.println("- Starting Bluetooth® Low Energy module failed!");
    while (1);
  }

  if (!APDS.begin()) {
    Serial.println("Error initializing APDS-9960 sensor.");
  }

  APDS.setGestureSensitivity(100);

  if (!BARO.begin()) {
    Serial.println("Error initializing LPS-22HB sensor.");
  }

  if (!HTS.begin()) {
    Serial.println("Error initializing HTS221 sensor.");
  }

  while (!IMU.begin()) {
    Serial.println("Error initializing IMU sensor.");
    delay(500);
  }

  temperatureCharacteristic.writeValue(0);
  humidityCharacteristic.writeValue(0);
  orientationCharacteristic.writeValue("0,0");
  trackerCharacteristic.writeValue("0,0");
  ambientLightCharacteristic.writeValue(0);
  batteryStatusCharacteristic.writeValue(0);

  temperature = HTS.readTemperature();
  humidity = HTS.readHumidity();
  //pressure = BARO.readPressure();
  

  BLE.setLocalName("ScopeTrackerBLE");
  BLE.setAdvertisedService(scopeService);
  scopeService.addCharacteristic(temperatureCharacteristic);
  scopeService.addCharacteristic(humidityCharacteristic);
  scopeService.addCharacteristic(orientationCharacteristic);
  scopeService.addCharacteristic(trackerCharacteristic);
  scopeService.addCharacteristic(ambientLightCharacteristic);
  scopeService.addCharacteristic(batteryStatusCharacteristic);
  BLE.addService(scopeService);
  BLE.advertise();

  Serial.println("ScopeTrackerBLE");
  Serial.println(" ");
}

void loop() {
  BLEDevice central = BLE.central();
  Serial.println("- Discovering central device...");
  delay(500);

  if (central) {
    
    Serial.println("* Connected to central device!");
    Serial.print("* Device MAC address: ");
    Serial.println(central.address());
    Serial.println(" ");

    digitalWrite(LEDR, LOW);
    digitalWrite(LEDG, LOW);
    digitalWrite(LEDB, HIGH);
    digitalWrite(LED_BUILTIN, LOW);

    while (central.connected()) {

      if (IMU.magneticFieldAvailable()) {
        IMU.readMagneticField(xSensorCompass, ySensorCompass, zSensorCompass);
        heading = (atan2(ySensorCompass,xSensorCompass) * 180) / M_PI;
        Serial.println("Current Azimuth :\n");
        Serial.println(heading);
      }

      if(!APDS.colorAvailable()) {
        APDS.readColor(r, g, b, a);
        if (inRange(a,ambientLight-1,ambientLight+1)){
          ambientLight=a;
          ambientLightCharacteristic.writeValue(a);
          Serial.println(a);
        }
      }
      
      temperatureNew = HTS.readTemperature();
      if (inRange(temperatureNew,temperatureNew-1,temperatureNew+1)){
        temperature = temperatureNew;
        temperatureCharacteristic.writeValue(temperature);
      }

      /*humidityNew = HTS.readHumidity();
      Serial.println(humidityNew);
      if (inRange(humidityNew,humidityNew-1,humidityNew+1)){
        humidity = humidityNew;
        humidityCharacteristic.writeValue(humidity);
        Serial.println(humidity);
      }*/
      //pressure = BARO.readPressure();

      if (trackerCharacteristic.written()) {
         tracker = trackerCharacteristic.value();
         Serial.println(tracker);
         adjustSight(tracker);
         //writeGesture(gesture);
       }

       /*if (temperatureCharacteristic.written()) {
         temperature = temperatureCharacteristic.value();
         Serial.println(temperature);
         //writeGesture(gesture);
       }

      if (humidityCharacteristic.written()) {
         humidity = humidityCharacteristic.value();
         Serial.println(humidity);
         //writeGesture(gesture);
       }*/

    }
    Serial.println("* Actual value: unconnected");
    digitalWrite(LEDR, LOW);
    digitalWrite(LEDG, HIGH);
    digitalWrite(LEDB, HIGH);
    digitalWrite(LED_BUILTIN, LOW);
    
    Serial.println("* Disconnected to central device!");
  }
}

void adjustSight(String tracker){
  Serial.println("- Characteristic <Tracker> has changed!");
  Serial.println(tracker);
}

/*void writeGesture(int gesture) {
  Serial.println("- Characteristic <gesture_type> has changed!");
  
   switch (gesture) {
      case GESTURE_UP:
        Serial.println("* Actual value: UP (red LED on)");
        Serial.println(" ");
        digitalWrite(LEDR, LOW);
        digitalWrite(LEDG, HIGH);
        digitalWrite(LEDB, HIGH);
        digitalWrite(LED_BUILTIN, LOW);
        break;
      case GESTURE_DOWN:
        Serial.println("* Actual value: DOWN (green LED on)");
        Serial.println(" ");
        digitalWrite(LEDR, LOW);
        digitalWrite(LEDG, LOW);
        digitalWrite(LEDB, HIGH);
        digitalWrite(LED_BUILTIN, LOW);
        break;
      case GESTURE_LEFT:
        Serial.println("* Actual value: LEFT (blue LED on)");
        Serial.println(" ");
        digitalWrite(LEDR, HIGH);
        digitalWrite(LEDG, HIGH);
        digitalWrite(LEDB, LOW);
        digitalWrite(LED_BUILTIN, LOW);
        break;
      case GESTURE_RIGHT:
        Serial.println("* Actual value: RIGHT (built-in LED on)");
        Serial.println(" ");
        digitalWrite(LEDR, HIGH);
        digitalWrite(LEDG, HIGH);
        digitalWrite(LEDB, HIGH);
        digitalWrite(LED_BUILTIN, HIGH);
        break;
      default:
        digitalWrite(LEDR, HIGH);
        digitalWrite(LEDG, HIGH);
        digitalWrite(LEDB, HIGH);
        digitalWrite(LED_BUILTIN, LOW);
        break;
    }      
}*/

bool inRange(int val, int minimum, int maximum)
{
  return ((minimum <= val) && (val <= maximum));
}