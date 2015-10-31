package com.fruitmix.raspiserver;
import com.fruitmix.ControllServer;
import org.sparkle.janette.server.ServerHandler;
import org.sparkle.jbind.JBinD;
import org.iu.gps.*;
import org.sparkle.jcfg.JCFG;


/**
 * Created by yasmidrog on 18.10.15.
 */
public class SH extends ServerHandler {
    public SH() {
    }
    public void in(JBinD data){
        JCFG s= data.getPart("sensors").getDataAsJCFG();
        ControllServer.raspicont.addCfg(s);
    }
    public JBinD out(){
        GPSInfo j=new GPSInfo();
        System.out.print(j.latitude+ " "+j.longitude);
        JBinD bind=new JBinD();
        return bind;
    }
}
