/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.philng.telemetrydisplay.controller;

import com.philng.telemetrydisplay.ConnectionManager.ConnectionManager;
import com.philng.telemetrydisplay.GUI;
import com.philng.telemetrydisplay.Serial.SerialReader;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesDataItem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

    /**
     * Save the file as a CSV
     * @param file
     */
    public void saveDataAsCSV(File file){
        try {
            FileWriter writer = new FileWriter(file);
            writer.write("time,voltage,current\n");

            TimeSeries voltage = ui.graphDisplay.getVoltageDataset();
            TimeSeries current = ui.graphDisplay.getCurrentDataset();

            for(int i = 0; i<voltage.getItemCount(); i++){
                TimeSeriesDataItem currVoltage = voltage.getDataItem(i);
                TimeSeriesDataItem currCurrent = current.getDataItem(i);

                writer.write(currVoltage.getPeriod().toString() + ',');

                writer.write(currVoltage.getValue().toString() + ',');
                writer.write(currCurrent.getValue().toString());

                writer.write('\n');
            }

            writer.close();
        } catch(IOException e){
            System.err.println("Error saving file:" + e.getMessage());
        }

    }
}
