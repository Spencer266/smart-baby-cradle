#include <WiFi.h>
#include <Arduino.h>
#include <driver/i2s.h>
#include <SD.h>
#include <WiFiClient.h>
#include "ESP32_FTPClient.h"
#include <Adafruit_Sensor.h>

 
#define DHTTYPE DHT22 // Khai báo loại cảm biến
#define DHTPIN 4
#define I2S_WS 25
#define I2S_SD 32
#define I2S_SCK 33
#include "DHT.h"
#define I2S_PORT I2S_NUM_0
#define I2S_SAMPLE_RATE   (16000)
#define I2S_SAMPLE_BITS   (16)
#define I2S_READ_LEN      (16 * 1024)
#define RECORD_TIME       (8) // Seconds
#define I2S_CHANNEL_NUM   (1)
#define FLASH_RECORD_SIZE (I2S_CHANNEL_NUM * I2S_SAMPLE_RATE * I2S_SAMPLE_BITS / 8 * RECORD_TIME)
WiFiClient client;
const char *ssid = "GatewayPi";
const char *password = "pi123456789";

const char *serverIP = "192.168.5.1";
const int serverPort = 6005;  // Specify the port number on the server

const char* ftpUser = "ThangTrang";
const char* ftpPassword = "Thang123";

char ftp_server[] = "192.168.5.1";
char ftp_user[]   = "ThangTrang";
char ftp_pass[]   = "Thang123";

ESP32_FTPClient ftp(ftp_server, ftp_user, ftp_pass, 5000, 0); // timeout = 5000, 0: active mode
DHT dht(DHTPIN, DHTTYPE);

File file;
const char filename[] = "/hehehehehehehe.wav";
const int headerSize = 44;

float temperature;
float humidity;

bool recording = false;




/*---------------Sound related functions------------------ */
void SDInit() {
  if (!SD.begin(5)) {
    Serial.println("Card Mount Failed");
    return;
  }
  Serial.println("kết nối SD xong");
  
  if (SD.exists(filename)) {
    SD.remove(filename);
  }

  file = SD.open(filename, FILE_WRITE);
  if (!file) {
    Serial.println("File is not available!");
    return;
  }

  byte header[headerSize];
  wavHeader(header, FLASH_RECORD_SIZE);

  file.write(header, headerSize);
  Serial.println("ghi xong");
}

void i2sInit(){
  i2s_config_t i2s_config = {
    .mode = (i2s_mode_t)(I2S_MODE_MASTER | I2S_MODE_RX),
    .sample_rate = I2S_SAMPLE_RATE,
    .bits_per_sample = i2s_bits_per_sample_t(I2S_SAMPLE_BITS),
    .channel_format = I2S_CHANNEL_FMT_ONLY_LEFT,
    .communication_format = i2s_comm_format_t(I2S_COMM_FORMAT_I2S | I2S_COMM_FORMAT_I2S_MSB),
    .intr_alloc_flags = 0,
    .dma_buf_count = 32,
    .dma_buf_len = 512,
    .use_apll = 1
  };

  i2s_driver_install(I2S_PORT, &i2s_config, 0, NULL);

  const i2s_pin_config_t pin_config = {
    .bck_io_num = I2S_SCK,
    .ws_io_num = I2S_WS,
    .data_out_num = -1,
    .data_in_num = I2S_SD
  };

  i2s_set_pin(I2S_PORT, &pin_config);
}

void i2s_adc_data_scale(uint8_t * d_buff, uint8_t* s_buff, uint32_t len) {
  uint32_t j = 0;
  uint32_t dac_value = 0;
  for (int i = 0; i < len; i += 2) {
    dac_value = ((((uint16_t) (s_buff[i + 1] & 0xf) << 8) | ((s_buff[i + 0]))));
    d_buff[j++] = 0;
    d_buff[j++] = dac_value * 256 / 2048;
  }
}

void i2s_adc(void *arg) {
  int i2s_read_len = I2S_READ_LEN;
  int flash_wr_size = 0;
  size_t bytes_read;

  char* i2s_read_buff = (char*) calloc(i2s_read_len, sizeof(char));
  uint8_t* flash_write_buff = (uint8_t*) calloc(i2s_read_len, sizeof(char));

  i2s_read(I2S_PORT, (void*) i2s_read_buff, i2s_read_len, &bytes_read, portMAX_DELAY);
  i2s_read(I2S_PORT, (void*) i2s_read_buff, i2s_read_len, &bytes_read, portMAX_DELAY);

  Serial.println(" *** Recording Start *** ");
  while (flash_wr_size < FLASH_RECORD_SIZE) {
    i2s_read(I2S_PORT, (void*) i2s_read_buff, i2s_read_len, &bytes_read, portMAX_DELAY);
    i2s_adc_data_scale(flash_write_buff, (uint8_t*)i2s_read_buff, i2s_read_len);
    file.write((const byte*) flash_write_buff, i2s_read_len);
    flash_wr_size += i2s_read_len;
    ets_printf("Sound recording %u%%\n", flash_wr_size * 100 / FLASH_RECORD_SIZE);
    ets_printf("Never Used Stack Size: %u\n", uxTaskGetStackHighWaterMark(NULL));
  }
  file.close();

  free(i2s_read_buff);
  i2s_read_buff = NULL;
  free(flash_write_buff);
  flash_write_buff = NULL;

  //vTaskDelete(NULL);
}

void wavHeader(byte* header, int wavSize){
  header[0] = 'R';
  header[1] = 'I';
  header[2] = 'F';
  header[3] = 'F';
  unsigned int fileSize = wavSize + headerSize - 8;
  header[4] = (byte)(fileSize & 0xFF);
  header[5] = (byte)((fileSize >> 8) & 0xFF);
  header[6] = (byte)((fileSize >> 16) & 0xFF);
  header[7] = (byte)((fileSize >> 24) & 0xFF);
  header[8] = 'W';
  header[9] = 'A';
  header[10] = 'V';
  header[11] = 'E';
  header[12] = 'f';
  header[13] = 'm';
  header[14] = 't';
  header[15] = ' ';
  header[16] = 0x10;
  header[17] = 0x00;
  header[18] = 0x00;
  header[19] = 0x00;
  header[20] = 0x01;
  header[21] = 0x00;
  header[22] = 0x01;
  header[23] = 0x00;
  header[24] = 0x80;
  header[25] = 0x3E;
  header[26] = 0x00;
  header[27] = 0x00;
  header[28] = 0x00;
  header[29] = 0x7D;
  header[30] = 0x00;
  header[31] = 0x00;
  header[32] = 0x02;
  header[33] = 0x00;
  header[34] = 0x10;
  header[35] = 0x00;
  header[36] = 'd';
  header[37] = 'a';
  header[38] = 't';
  header[39] = 'a';
  header[40] = (byte)(wavSize & 0xFF);
  header[41] = (byte)((wavSize >> 8) & 0xFF);
  header[42] = (byte)((wavSize >> 16) & 0xFF);
  header[43] = (byte)((wavSize >> 24) & 0xFF);
}

/*-----------------------Threading------------------------ */


void ftpClientTask(void *param) {

  while (1) {
    int sample = analogRead(35);
    Serial.print("sample: ");
    Serial.println(sample);
    delay(500);
    String buf;
    
    if(sample > 30 && !recording){
      recording = true;
      i2s_adc(NULL); // ghi âm
      recording = false; // ghi âm xong
      // Truyền file
      ftp.OpenConnection();
      Serial.print("connection ftp");
      ftp.ChangeWorkDir("files");
      File file = SD.open("/hehehehehehehe.wav");
      if (file){
        ftp.InitFile("Type I"); // Binary mode: truyền nhị phân
        ftp.NewFile("hehehehehehehe.wav");
        const int bufferSize = 1024; // Kích thước bộ đệm, có thể điều chỉnh
        uint8_t buffer[bufferSize];
        while(file.available()){
          int bytesRead = file.read(buffer, bufferSize);
          if (bytesRead > 0) {
            ftp.WriteData(buffer, bytesRead);
            Serial.print("Đang gửi ");
            Serial.println(bytesRead);
          }
        }
        file.close();
        ftp.CloseFile();
        Serial.println("\nFile upload successful");
      }
      else
        Serial.println("Error opening file");
        
      delay(20000);
      }  
  }
}


void tcpClientTask(void *param) {
  while (1) {
    String buf;

    float humidity = dht.readHumidity();
    float temperature = dht.readTemperature();
    
    Serial.print("Nhiet do: ");
    Serial.println(temperature); //Xuất nhiệt độ
    Serial.print("Do am: ");
    Serial.println(humidity); //Xuất độ ẩm
 
    if (client.connected())
    {
      buf = String(temperature, 2);
      client.print(buf);
      client.readStringUntil('\0');
      
      buf = String(humidity, 2);
      client.print(buf);
      client.readStringUntil('\0');

      Serial.println("Send DHT22 completed");

      delay(5000);
    } else {
      Serial.println("Connection broken");
      client.stop();
    }
  }
}


void setup() {
  Serial.begin(115200);
  delay(100);

  SDInit();
  i2sInit();
  dht.begin();
  // Connect to Wi-Fi
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.println("Connecting to WiFi...");
  }
  Serial.println("Connected to WiFi");


  // Connect to the TCP server
  if (client.connect(serverIP, serverPort)) {
    Serial.println("Connected to TCP server");
  } else {
    Serial.println("Connection to server failed");
  }
 
  
  // Connect to FTP server
//  if (ftp.OpenConnection()) {
//    Serial.println("Connected to FTP server");
//  } 
//  else 
//    Serial.println("FTP connection failed");
  ftp.OpenConnection();
  Serial.print("FTP connection opened");
  xTaskCreate(tcpClientTask, "TCP Client Task", 10000, NULL, 1, NULL);
  xTaskCreate(ftpClientTask, "FTP Client Task", 10000, NULL, 1, NULL);
}

void loop() {

}
