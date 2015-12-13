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

    private  Queue<JCFG> accelData;//очередь данных с телефона
    private  ArduinoConnector arduinoConn;
    private  GPSConnector gpsConn;
    public  void addCfg(JCFG conf){
        accelData.enqueue(conf);
    }
    private   Timer control=new Timer(100, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if(!accelData.isEmpty()&&arduinoConn.hasCompletedLastCommand()) {
                    JCFG cfg = accelData.dequeue();
                    ControllServer.result.setText(Writer.writeToString(cfg));
                    arduinoConn.setCommand(createCommand(cfg));
                    //проверить очередь и сфоомировать новую команду для Ардуино
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    });
    private Thread conThread;
    public RaspiController(final String arduinoport,final String gpsport){
        try {
            accelData = new Queue<JCFG>();
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
        //сделать команду на основе данных акселлерометра и полслать на Ардуино
        return "Test".getBytes();
    }
}
