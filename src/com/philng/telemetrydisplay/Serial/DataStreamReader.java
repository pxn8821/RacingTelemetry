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

public class DataStreamReader implements Runnable 
    {
        Controller controller;
        InputStream in;
        boolean stop = false;
        
        public void stop(){
            stop = true;
        }
        public DataStreamReader ( InputStream in, Controller controller )
        {
            this.in = in;
            this.controller = controller;
        }
        String bufferData = "";
        public void run ()
        {
            byte[] buffer = new byte[1024];
            int len = -1;
            try
            {
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
