/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.philng.telemetrydisplay.ConnectionManager;

import com.philng.telemetrydisplay.Serial.SerialReader;
import com.philng.telemetrydisplay.controller.Controller;

/**
 *
 * @author phil
 */
public class ConnectionManager {
   private static ConnectionManager instance = null;
   private Controller controller;
   private ConnectionChecker checker = new ConnectionChecker();
   
   private boolean isConnected = false;
   public boolean connectIntent = false;
   public boolean threadStarted = false;
   public String portName;
   
   private long lastSeen;

    public ConnectionChecker getChecker() {
        return checker;
    }

    public void setChecker(ConnectionChecker checker) {
        this.checker = checker;
    }

    public long getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(long lastSeen) {
        this.lastSeen = lastSeen;
    }
   
   protected ConnectionManager() {
       
   }
   public static ConnectionManager getInstance() {
      if(instance == null) {
         instance = new ConnectionManager();
      }
      return instance;
   }
    
   public void setController(Controller c){
       this.controller = c;
       SerialReader.getInstance().setController(c);
   }

    public boolean connect(){
        if(!threadStarted){
            (new Thread(checker)).start();
        }
        threadStarted = true;
        connectIntent = true;
        return true;
    }
   
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
   
   public void setLabel(String label){
       controller.ui.statusLabel.setText(label);
   }
   public boolean getIsConnected(){
       return this.isConnected;
   }
   
   
}
