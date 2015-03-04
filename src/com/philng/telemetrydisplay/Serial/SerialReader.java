/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.philng.telemetrydisplay.Serial;

import com.philng.telemetrydisplay.controller.Controller;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * Created by phil on 3/3/15.
 */
public class SerialReader {
    private static SerialReader instance = null;
    private CommPort commPort;
    private Controller controller;
    private static final String PORT_NAMES[] = {
            "/dev/tty.usbmodem", // Mac OS X
//        "/dev/usbdev", // Linux
//        "/dev/tty", // Linux
//        "/dev/serial", // Linux
//        "COM3", // Windows
    };

    private BufferedReader input;
    private OutputStream output;

    private static final int TIME_OUT = 1000; // Port open timeout
    private static final int DATA_RATE = 9600; // Arduino serial port

    private SerialPort serialPort = null;
    private CommPortIdentifier portId = null;
    
    Thread isThread;
    InputStream in;
    DataStreamReader DSR;
            
    public ArrayList<String> getAvailablePorts(){
        ArrayList<String> returnList = new ArrayList<String>();
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        // Enumerate system ports and try connecting to Arduino over each
        //
        while (portEnum.hasMoreElements()) {
            // Iterate through your host computer's serial port IDs
            //
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            returnList.add( currPortId.getName() );
        }
        
        return returnList;
    }
    
    public boolean connect ( String portName ) {
        try{
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
            if ( portIdentifier.isCurrentlyOwned() ){
                System.out.println("Error: Port is currently in use");
            } else {
                System.out.println("Connect 1/2");
                commPort = portIdentifier.open(this.getClass().getName(),6000);

                if ( commPort instanceof SerialPort )
                {
                    System.out.println("Connect 2/2");
                    serialPort = (SerialPort) commPort;
                    System.out.println("BaudRate: " + serialPort.getBaudRate());
                    System.out.println("DataBIts: " + serialPort.getDataBits());
                    System.out.println("StopBits: " + serialPort.getStopBits());
                    System.out.println("Parity: " + serialPort.getParity());
                    System.out.println("FlowControl: " + serialPort.getFlowControlMode());
                    serialPort.setSerialPortParams(4800,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_ODD);
                    serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN);
                    System.out.println("BaudRate: " + serialPort.getBaudRate());
                    System.out.println("DataBIts: " + serialPort.getDataBits());
                    System.out.println("StopBits: " + serialPort.getStopBits());
                    System.out.println("Parity: " + serialPort.getParity());
                    System.out.println("FlowControl: " + serialPort.getFlowControlMode());
                    
                    in = serialPort.getInputStream();
                    DSR = new DataStreamReader(in, controller);
                    isThread = (new Thread(DSR));
                    isThread.start();
                            
                    return true;
                    
                    
                } else {
                    System.out.println("Error: Only serial ports are handled by this example.");
                }
            }    
        } catch(Exception e){
            System.out.println("Could not connect");
            System.out.println(e.getMessage());
        }
        
        return false;
    }
    
    public SerialReader(){
        
    }
    
    public void setController(Controller c){
        this.controller = c;
    }
    
    public static SerialReader getInstance(){
        if(instance == null){
            instance = new SerialReader();
        }
        return instance;
    }
    
    public void disconnect(){
        try{
            in.close();
            Thread.sleep(1000);
            DSR.stop();
            serialPort.close();
            commPort.close();
        } catch(Exception e){
            
        }
    }
    
    public void Serial(){

    }
}
