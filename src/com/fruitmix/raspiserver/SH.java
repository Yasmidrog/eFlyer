package com.fruitmix.raspiserver;
import com.fruitmix.ControllServer;
import com.fruitmix.raspiconnect.GPSConnector;
import org.sparkle.janette.server.ServerHandler;
import org.sparkle.jbind.JBinD;
import org.iu.gps.*;
import org.sparkle.jbind.Part;
import org.sparkle.jcfg.JCFG;
import org.sparkle.jcfg.Parameter;

import java.util.Random;


/**
 * Created by yasmidrog on 18.10.15.
 */
public class SH extends ServerHandler {
    GPSInfo j=new GPSInfo();
    public SH() {
    }
    public void in(JBinD data){
        JCFG s= data.getPart("sensors").getDataAsJCFG();
        ControllServer.raspicont.addCfg(s);
        //добавить в очередь данных акселлерометра, которые будут обрабатываться
    }
    public JBinD out(){
        //послать GPS
        JBinD b = new JBinD();
        try {
            b.addPart(new Part("GPS", GPSConnector.getGpsData()));
        }catch (Exception e){
            e.printStackTrace();
        }
        return b;
    }
}
