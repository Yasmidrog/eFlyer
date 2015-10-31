package com.fruitmix.raspiconnect;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Created by yasmidrog on 31.10.15.
 */
public class ArduinoConnector extends Connector {
    SerialPort serialPort;

    protected byte[] bytes=new byte[]{};

    protected BufferedReader input;

    protected OutputStream output;
    /**
     * Milliseconds to block while waiting for port open
     */
    private static final int TIME_OUT = 2000;
    /**
     * Default bits per second for COM port.
     */
    private static final int DATA_RATE = 9600;

    @Override
    public synchronized void serialEvent(SerialPortEvent oEvent) {

        System.out.println("----");
        try {
            if(oEvent.getEventType()==SerialPortEvent.DATA_AVAILABLE)
                System.out.println(input.readLine());
            if(oEvent.getEventType()==SerialPortEvent.OUTPUT_BUFFER_EMPTY)
                output.write("Test".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void setCommand(byte[] b){
        bytes=b;
    }
}
