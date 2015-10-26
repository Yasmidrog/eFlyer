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
    private  Connector con;
    public  void addCfg(JCFG conf){
        commands.enqueue(conf);
    }
    private   Timer control=new Timer(100, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if(!commands.isEmpty()) {
                    JCFG cfg = commands.dequeue();
                    ControllServer.result.setText(Writer.writeToString(cfg));
                    con.setCommand(createCommand(cfg));
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    });
    public RaspiController(){
        try {
            commands = new Queue<JCFG>();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    con=new Connector();
                    con.initialize();
                }
            }).start();
            control.start();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public  void stopControl(){
        control.stop();
    }
    public  void startControl(){
        control.start();
    }
    private byte[] createCommand(JCFG cfg){
        //trying to create command to send to a raspi
        return "Test".getBytes();
    }
}
