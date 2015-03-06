/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.philng.telemetrydisplay.ConnectionManager;

import com.philng.telemetrydisplay.Serial.SerialReader;
import java.util.Date;

/**
 * This class runs as a thread and keeps checking
 * the connection intent, and whether it is connected
 * or not. Here's how it works:
 *
 * If connect intent is true:
 *  Check for time elapsed since last seen
 *  if time is > 1 second, then force disconnect
 *  then, connect to the application if it isn't seen
 *
 * If connect intent is false:
 *  If the connection is still connected, disconnect
 * @author phil
 */
public class ConnectionChecker implements Runnable{

    @Override
    public void run() {
        while(true){
            try {
                long lastSeen = ConnectionManager.getInstance().getLastSeen();
                ConnectionManager connMan = ConnectionManager.getInstance();

                if (connMan.connectIntent == true) {

                    // Check if the elapsed time is too long
                    if ((System.currentTimeMillis() - lastSeen) > 1000) {
                        //Closing serial port
                        SerialReader.getInstance().disconnect();
                        ConnectionManager.getInstance().setIsConnected(false);
                    } else {
                        ConnectionManager.getInstance().setIsConnected(true);
                    }


                    // If the connection is not connected, try to connect to it.
                    if (connMan.getIsConnected()) {

                    } else {
                        connMan.setLabel("Trying to connect");
                        SerialReader.getInstance().connect(connMan.portName);
                    }

                } else {
                    // If the user has requested a disconnect, and it's still connected, disconnect it
                    if (connMan.getIsConnected()) {
                        connMan.setLabel("Disconnecting...");
                        SerialReader.getInstance().disconnect();
                        ConnectionManager.getInstance().setIsConnected(false);
                    }
                }
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                }
            } catch(Exception e){
                System.err.println("Caught exception in ConnectionChecker " + e.getMessage());
            }
        }
    }
    
}
