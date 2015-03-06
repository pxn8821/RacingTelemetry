/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.philng.telemetrydisplay.ConnectionManager;

import com.philng.telemetrydisplay.Serial.SerialReader;
import com.philng.telemetrydisplay.controller.Controller;

/**
 * Manages the serial bluetooth connection
 * @author phil
 */
public class ConnectionManager {
    // Singleton reference
    private static ConnectionManager instance = null;

    // Reference to the controller object
    private Controller controller;

    // Connection checker thread
    private ConnectionChecker checker = new ConnectionChecker();

    // Whether the connection is successful or not
    private boolean isConnected = false;

    // The state that the user intents to be in
    public boolean connectIntent = false;

    // Wheather monitoring thread is started or not
    public boolean threadStarted = false;

    // Serial terminal port name
    public String portName;

    // Last seen data
    private long lastSeen;


    public long getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(long lastSeen) {
        this.lastSeen = lastSeen;
    }

    protected ConnectionManager() {}

    /**
     * Get the ConnectionManager instance
     * @return
     */
    public static ConnectionManager getInstance() {
      if(instance == null) {
         instance = new ConnectionManager();
      }
      return instance;
    }

    /**
     * Set the controller's reference object
     * @param c
     */
    public void setController(Controller c){
       this.controller = c;
       SerialReader.getInstance().setController(c);
    }

    /**
     * Set the connection intent to be true,
     * also starts up the thread checker if not already started
     * @return
     */
    public boolean connect(){
        if(!threadStarted){
            (new Thread(checker)).start();
        }
        threadStarted = true;
        connectIntent = true;
        return true;
    }

    /**
     * Sets whether the connection is actually connected or not
     * @param isConnected
     */
    public void setIsConnected(boolean isConnected){
       this.isConnected = isConnected;
       if(isConnected){
           controller.ui.connectSerialButton.setText("Disconnect");
           controller.ui.statusLabel.setText("Connected");
       } else {
           controller.ui.connectSerialButton.setText("Connect");
           controller.ui.statusLabel.setText("Disconnected");
       }
    }

    /**
     * Set the UI Label
     * @param label
     */
    public void setLabel(String label){
       controller.ui.statusLabel.setText(label);
    }

    public boolean getIsConnected(){
       return this.isConnected;
    }
   
   
}
