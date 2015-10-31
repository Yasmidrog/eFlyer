package com.fruitmix.raspiconnect;

import gnu.io.SerialPortEvent;
import org.sparkle.jcfg.JCFG;

import java.util.Scanner;

/**
 * Created by yasmidrog on 31.10.15.
 */
public class GPSConnector extends Connector {
    public static JCFG gpsData=new JCFG();

    @Override
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        try {
                String gps= input.readLine();
                if(gps.contains("$GPGLL")){

                }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
