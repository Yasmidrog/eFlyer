package com.fruitmix.raspiserver;
import com.fruitmix.ControllServer;
import org.sparkle.janette.server.ServerHandler;
import org.sparkle.jbind.JBinD;
import org.iu.gps.*;
import org.sparkle.jbind.Part;
import org.sparkle.jcfg.JCFG;
import org.sparkle.jcfg.Parameter;


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
    }
    public JBinD out(){
        JCFG conf = new JCFG();
        JBinD b = new JBinD();
        try {
            conf.add(new Parameter("Long", j.longitude));
            conf.add(new Parameter("Lat", j.latitude));
            conf.add(new Parameter("Height", j.height));
            b.addPart(new Part("GPS", conf));
        }catch (Exception e){
            e.printStackTrace();
        }
        return b;
    }
}
