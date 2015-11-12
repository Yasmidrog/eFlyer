package com.fruitmix.raspiconnect;

import com.fruitmix.ControllServer;
import org.sparkle.jcfg.JCFG;
import org.sparkle.jcfg.Writer;
import sun.misc.Queue;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Created by yasmidrog on 22.10.15.
 */
public class RaspiController {

    private  Queue<JCFG> commands;
    private  ArduinoConnector arduinoConn;
    private  GPSConnector gpsConn;
    public  void addCfg(JCFG conf){
        commands.enqueue(conf);
    }
    private   Timer control=new Timer(100, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if(!commands.isEmpty()&&arduinoConn.hasCompletedLastCommand()) {
                    JCFG cfg = commands.dequeue();
                    ControllServer.result.setText(Writer.writeToString(cfg));
                    arduinoConn.setCommand(createCommand(cfg));
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    });
    private Thread conThread;
    public RaspiController(final String arduinoport,final String gpsport){
        try {
            commands = new Queue<JCFG>();
            conThread=new Thread(new Runnable() {
                @Override
                public void run() {
                    arduinoConn =new ArduinoConnector();
                    arduinoConn.initialize(arduinoport);
                    gpsConn=new GPSConnector();
                    gpsConn.initialize(gpsport);
                }
            });
            conThread.start();
            control.start();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public  void stopControl(){
        control.stop();
        arduinoConn.close();
        conThread.stop();
    }
    private byte[] createCommand(JCFG cfg){
        //trying to create command to send to a raspi
        return "Test".getBytes();
    }
}
