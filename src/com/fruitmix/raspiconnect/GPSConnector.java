package com.fruitmix.raspiconnect;

import gnu.io.SerialPortEvent;

/**
 * Created by yasmidrog on 31.10.15.
 */
public class GPSConnector extends Connector {

    /**
     * Milliseconds to block while waiting for ardport open
     */
    private static final int TIME_OUT = 2000;
    /**
     * Default bits per second for COM ardport.
     */
    private static final int DATA_RATE = 9600;

    @Override
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        System.out.println("----");
        try {
            if(oEvent.getEventType()==SerialPortEvent.DATA_AVAILABLE)
                System.out.println(input.readLine());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
