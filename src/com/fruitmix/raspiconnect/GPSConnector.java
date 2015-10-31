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
                    String[] need = gps.split(",");
                    double n = Double.parseDouble(need[1])/100, e = Double.parseDouble(need[3])/100, a = Double.parseDouble(need[5])/1000;
                    String o = n + "N "+ e+"E"+ "\n" + "Altitude:"+a;
                    System.out.println(o);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
