/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.philng.telemetrydisplay.Serial;

import com.philng.telemetrydisplay.ConnectionManager.ConnectionManager;
import com.philng.telemetrydisplay.controller.Controller;
import java.io.IOException;
import java.io.InputStream;

/**
 * Handles the intake and processing of data from the serial terminal
 */
public class DataStreamReader implements Runnable {
    Controller controller;
    InputStream in;
    boolean stop = false;
    String bufferData = "";

    /**
     * Send a stop command
     */
    public void stop(){
        stop = true;
    }


    /**
     * Add in the controller and InputStream reference
     * @param in
     * @param controller
     */
    public DataStreamReader ( InputStream in, Controller controller ) {
        this.in = in;
        this.controller = controller;
    }

    /**
     * Thread that runs to process data
     */
    public void run ()
    {
        byte[] buffer = new byte[1024];
        int len = -1;
        try
        {
            // Read the data. This adds in data into a string until
            // it finds a newline character. Once it does that, it parses the string
            // for voltage and current information
            while ( ( len = this.in.read(buffer)) > -1  && !stop)
            {
                String data = new String(buffer,0,len);
                bufferData = bufferData + data;
                if(bufferData.contains("\n")){
                    bufferData.replace("\n", "");
                    try{
                        String [] split = bufferData.split(",");
                        float voltage = Float.parseFloat( split[0] );
                        float current = Float.parseFloat( split[1] );
                        controller.addData(voltage, current);
                        ConnectionManager.getInstance().setLastSeen(System.currentTimeMillis());
                    } catch(Exception e){

                    } finally {
                        bufferData = "";
                    }
                }
            }
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }
    }
