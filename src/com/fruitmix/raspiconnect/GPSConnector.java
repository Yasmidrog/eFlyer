package com.fruitmix.raspiconnect;
import gnu.io.SerialPortEvent;
import org.sparkle.jcfg.JCFG;



/**
 * Created by yasmidrog on 31.10.15.
 */
public class GPSConnector extends Connector {
    private static JCFG gpsData=new JCFG();
    @Override
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        try {
                String gps= input.readLine();
                if(gps.contains("$GPGLL")){
                    String[] need = gps.split(",");
                    double n = Double.parseDouble(need[1])/100, e = Double.parseDouble(need[3])/100,
                            a = Double.parseDouble(need[5])/1000;
                    String.format("%.2f",1.12);
                    gpsData.set("Latitude", round(n,2) + "N");
                    gpsData.set("Longitude",  round(e,2)+"E");
                    gpsData.set("Altitude",  round(a,2));
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static float round(double number, int scale) {
        int pow = 10;
        for (int i = 1; i < scale; i++)
            pow *= 10;
        double tmp = number * pow;
        return (float) (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) / pow;
    }
    public static JCFG getGpsData() {
        return gpsData;
    }
}
