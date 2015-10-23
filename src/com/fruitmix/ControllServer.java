package com.fruitmix;

import com.fruitmix.raspiconnect.RaspiController;
import com.fruitmix.raspiserver.SH;
import org.sparkle.janette.server.ServerConnection;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.File;


/**
 * Created by yasmidrog on 19.10.15.
 */
public class ControllServer{
    public static JFrame frame=new JFrame();
    static JButton open=new JButton("Open"), close=new JButton("Close");
    public static JLabel l=new JLabel();
    public static JTextArea result=new JTextArea();
    public static void main(String[] args) {
        setNatives();
        setGraphics();
    }
    static ServerConnection cc;
    static Thread t= new Thread() {
        public void run() {
            try {
                cc=new ServerConnection(1592, SH.class,200);
                cc.open();
            }catch (Exception e){
                e.printStackTrace();
            }
            System.out.println("Started");
        }

    };


    private static void start(){
        t.start();
    }
    private static void finish(){
        cc.close();
        t.stop();
    }
    private static void setNatives() {
        try {
            System.setProperty("java.library.path", new File("libs/rtxt/Linux").getAbsolutePath());
            java.lang.reflect.Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");

            fieldSysPath.setAccessible(true);
            try {
                fieldSysPath.set(null, null);
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }
    private static void setGraphics(){
        frame.setBounds(100,100,450,150);
        open.setBounds(1,40,70,25);
        close.setBounds(1,71,70,25);
        result.setBounds(75,1,365,130);
        open.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                start();
            }
        });
        close.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                finish();
            }
        });
        frame.addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                if(e.getNewState()==WindowEvent.WINDOW_CLOSED);
                finish();
            }
        });
        frame.add(close);
        frame.add(open);
        frame.add(result);
        frame.add(l);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

