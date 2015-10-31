package com.fruitmix.raspiconnect;

import gnu.io.SerialPortEvent;
import org.sparkle.jcfg.JCFG;

import java.util.Scanner;

/**
 * Created by yasmidrog on 31.10.15.
 */
public class GPSConnector extends Connector {
    public static JCFG gpsData=new JCFG();
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
        try {
                String gps= input.readLine();
                if(gps.contains("$GPGLL")){
                   Scanner s=new Scanner(gps);


                            System.out.println( s.next()+ s.next()+ s.next());
                }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
