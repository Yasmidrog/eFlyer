package com.fruitmix.raspiserver;
import com.fruitmix.ControllServer;
import org.sparkle.janette.server.ServerHandler;
import org.sparkle.jbind.JBinD;


/**
 * Created by yasmidrog on 18.10.15.
 */
public class SH extends ServerHandler {
    public SH() {
    }
    public void in(JBinD data){
        String s= data.getPart("sensors").getDataAsString();
        System.out.println(s);
        ControllServer.result.setText(s);
    }
    public JBinD out(){return new JBinD();}
}
