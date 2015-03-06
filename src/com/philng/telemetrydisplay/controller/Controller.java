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
            
    public void Controller(){}
    
    public void setUI(GUI ui){
        this.ui = ui;
    }

    /**
     * Connects  to a terminal port
     * @param portName
     */
    public void connectPort(String portName){
        ConnectionManager.getInstance().setController(this);
        ConnectionManager.getInstance().portName = portName;
        ConnectionManager.getInstance().connectIntent = true;
        ConnectionManager.getInstance().connect();
    }

    /**
     * Disconnect from a port
     */
    public void disconnectPort(){
        ConnectionManager.getInstance().connectIntent = false;
    }

    /**
     * Add data to the chart
     * @param v voltage
     * @param c current
     */
    public void addData(float v, float c){
        ui.graphDisplay.addData(v, c);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy:HH:mm:SS");
        ui.statusLabel.setText("Data Received: " + sdf.format(new Date()));
    }

    /**
     * Clears data in the chart
     */
    public void clearData(){
        ui.graphDisplay.resetData();
    }
}
