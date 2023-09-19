#include <WiFi.h>
#include <WiFiClient.h>
#include <SD.h>
#include "ESP32_FTPClient.h"

#define WIFI_SSID "GatewayPi"
#define WIFI_PASS "pi123456789"

// Pin configuration for the SD card module
const int SD_CS_PIN = 5;

char ftp_server[] = "192.168.5.1";
char ftp_user[]   = "ThangTrang";
char ftp_pass[]   = "Thang123";

void setup(){
  Serial.begin(115200);

  WiFi.begin(WIFI_SSID, WIFI_PASS);
  Serial.println("Connecting Wifi...");
  while (WiFi.status() != WL_CONNECTED) { // không connect đc
      delay(500);
      Serial.print(".");
  }
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP()); // IP của esp32

  // Thiết lập thẻ SD
  if (!SD.begin(SD_CS_PIN)) {
    Serial.println("không kết nối được thẻ SD"); 
    while (1);
  }
  Serial.println("Kết nối thẻ SD");

  // Open FTP connection
  ESP32_FTPClient ftp(ftp_server, ftp_user, ftp_pass, 5000, 0); // timeout = 5000, 0: active mode
  ftp.OpenConnection();
  Serial.println("FTP connection opened");
  ftp.ChangeWorkDir("files");

  // UPLOAD FILE FROM SD 
  File file = SD.open("/khoc2.wav");
  if (file){
    ftp.InitFile("Type I"); // Binary mode: truyền nhị phân
    ftp.NewFile("khoc2.wav");
    while(file.available()){
      unsigned char data = file.read();
      ftp.WriteData(&data, 1); // gửi 1 byte đã đọc lên
      Serial.print("dang gui ");
    }
    file.close();
    ftp.CloseFile();
    Serial.println("\nFile upload successful");
  }
  else
    Serial.println("Error opening file");
  
  // Close FTP connection
  ftp.CloseConnection();
  Serial.println("FTP connection closed");
  
}

void loop(){
}
