/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.philng.telemetrydisplay.ConnectionManager;

import com.philng.telemetrydisplay.Serial.SerialReader;
import java.util.Date;

/**
 *
 * @author phil
 */
public class ConnectionChecker implements Runnable{

    @Override
    public void run() {
        while(true){
            long lastSeen = ConnectionManager.getInstance().getLastSeen();
            ConnectionManager connMan = ConnectionManager.getInstance();
            
            if(connMan.connectIntent == true){
                if( (System.currentTimeMillis() - lastSeen) > 1000){
                    //Closing serial port
                    SerialReader.getInstance().disconnect();
                    ConnectionManager.getInstance().setIsConnected(false);
                } else {
                    ConnectionManager.getInstance().setIsConnected(true);
                }
                
                
                if(connMan.getIsConnected()){
                    System.out.println("Connected");                    
                } else {
                    connMan.setLabel("Trying to connect");
                    SerialReader.getInstance().connect(connMan.portName);
                }
                
            } else {
                if(connMan.getIsConnected()){
                    SerialReader.getInstance().disconnect();
                    ConnectionManager.getInstance().setIsConnected(false);
                }
            }
            



            try { Thread.sleep(500); } catch(Exception e) {}
        }
    }
    
}
