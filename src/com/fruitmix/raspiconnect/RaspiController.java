package com.fruitmix.raspiconnect;

import org.sparkle.jcfg.JCFG;
import sun.misc.Queue;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;

/**
 * Created by yasmidrog on 22.10.15.
 */
public class RaspiController {

    private static Queue<JCFG> commands=new Queue<JCFG>();
    private static Connector con=new Connector();
    public static void addCfg(JCFG conf){
        commands.enqueue(conf);
    }
    private static  Timer control=new Timer(100, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if(!commands.isEmpty()) {
                    System.out.print("Command\n");
                    JCFG cfg = commands.dequeue();
                    con.setCommand(createCommand(cfg));
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    });
    public RaspiController(){
        control.start();
    }
    public static  void stopControl(){
        control.stop();
    }
    public static void startControl(){
        control.start();
    }
    private static byte[] createCommand(JCFG cfg){
        //trying to create command to send to a raspi
        return "Test".getBytes();
    }
}
