/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.philng.telemetrydisplay.controller;

import com.philng.telemetrydisplay.ConnectionManager.ConnectionManager;
import com.philng.telemetrydisplay.GUI;
import com.philng.telemetrydisplay.Serial.SerialReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;

/**
 *
 * @author phil
 */
public class Controller {
    public GUI ui;
            
    public void Controller(){
    }
    
    public void setUI(GUI ui){
        this.ui = ui;
    }
    
    public void connectPort(String portName){
        ConnectionManager.getInstance().setController(this);
        ConnectionManager.getInstance().portName = portName;
        ConnectionManager.getInstance().connectIntent = true;
        ConnectionManager.getInstance().connect();
    }
    
    public void disconnectPort(){
        ConnectionManager.getInstance().connectIntent = false;
    }

    public void addData(float v, float c){
        ui.graphDisplay.addData(v, c);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy:HH:mm:SS");
        ui.statusLabel.setText("Data Received: " + sdf.format(new Date()));
    }
    
    public void clearData(){
        ui.graphDisplay.resetData();
    }
}
