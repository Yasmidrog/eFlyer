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
    protected byte[] bytes=new byte[]{};
    protected boolean completed=true;
    @Override
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        try {
            if(oEvent.getEventType()==SerialPortEvent.DATA_AVAILABLE) {
                if(input.readLine().equals("completed"));
                  completed=true;
            }
            if(oEvent.getEventType()==SerialPortEvent.OUTPUT_BUFFER_EMPTY)
                output.write("Test".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void setCommand(byte[] b){
        bytes=b;
        completed=false;
    }
    public boolean hasCompletedLastCommand(){
        return completed;
    }
}
