
#include <SoftwareSerial.h>  

int bluetoothTx = 8;  // TX-O pin of bluetooth mate, Arduino D2
int bluetoothRx = 7;  // RX-I pin of bluetooth mate, Arduino D3
int VRaw; //This will store our raw ADC data
int IRaw;
float VFinal; //This will store the converted data
float IFinal;
SoftwareSerial bluetooth(bluetoothTx, bluetoothRx);

void setup() {
  // put your setup code here, to run once:
  bluetooth.begin(115200);  // The Bluetooth Mate defaults to 115200bps
  bluetooth.print("$");  // Print three times individually
  bluetooth.print("$");
  bluetooth.print("$");  // Enter command mode
  delay(100);  // Short delay, wait for the Mate to send back CMD
  bluetooth.println("U,9600,N");  // Temporarily Change the baudrate to 9600, no parity
  // 115200 can be too fast at times for NewSoftSerial to relay the data reliably
  bluetooth.begin(9600);  // Start bluetooth serial at 9600
  
}

void loop() {
  // put your main code here, to run repeatedly:
  VRaw = analogRead(A0);
  IRaw = analogRead(A1);
  
  VFinal = VRaw/12.99; //180 Amp board  
  IFinal = IRaw/3.7; //180 Amp board
  
  bluetooth.print(VFinal);
  bluetooth.print(',');
  bluetooth.print(IFinal);
  bluetooth.print('\n');
  delay(100);              // wait for a second

 
}
