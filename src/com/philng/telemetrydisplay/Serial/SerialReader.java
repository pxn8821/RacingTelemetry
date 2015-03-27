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
 * Imeplementation of RXTX reading data from a serial terminal
 * This class handles the connection and disconnection of the serial terminal
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


    private SerialPort serialPort = null;
    private CommPortIdentifier portId = null;
    
    Thread isThread;
    InputStream in;
    DataStreamReader DSR;

    /**
     * Get a list of all the available ports
     * @return
     */
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

    /**
     * Open and connect to the COM port
     * @param portName device name
     * @return
     */
    public boolean connect ( String portName ) {
        try{
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
            if ( portIdentifier.isCurrentlyOwned() ){
                System.out.println("Error: Port is currently in use");
            } else {
                System.out.println("Connect 1/2");


                commPort = portIdentifier.open(this.getClass().getName(),6000);
                serialPort = (SerialPort) commPort;

                if ( commPort instanceof SerialPort )
                {
                    System.out.println("Connect 2/2");

                    System.out.println("BaudRate: " + serialPort.getBaudRate());
                    System.out.println("DataBIts: " + serialPort.getDataBits());
                    System.out.println("StopBits: " + serialPort.getStopBits());
                    System.out.println("Parity: " + serialPort.getParity());
                    System.out.println("FlowControl: " + serialPort.getFlowControlMode());
                    serialPort.setSerialPortParams(57600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                    serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN);
                    System.out.println("BaudRate: " + serialPort.getBaudRate());
                    System.out.println("DataBIts: " + serialPort.getDataBits());
                    System.out.println("StopBits: " + serialPort.getStopBits());
                    System.out.println("Parity: " + serialPort.getParity());
                    System.out.println("FlowControl: " + serialPort.getFlowControlMode());

                    // Once opened, add in the input streams
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

    /**
     * Set the controller reference object
     * @param c
     */
    public void setController(Controller c){
        this.controller = c;
    }

    public static SerialReader getInstance(){
        if(instance == null){
            instance = new SerialReader();
        }
        return instance;
    }

    /**
     * Disconnect the input streams and also the port itself
     */
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
}
