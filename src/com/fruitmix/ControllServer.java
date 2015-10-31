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
    private static ServerConnection server;
    private static  boolean started=false;
    public static RaspiController raspicont;
    public static JFrame frame=new JFrame("eFlyer Control Server");
    static JButton open=new JButton("Open"), close=new JButton("Close");
    public static JLabel l=new JLabel();
    public static JTextArea result=new JTextArea();
    public static JTextField ardport =new JTextField(),gpsport=new JTextField();
    public static void main(String[] args) {
        try {
            setNatives();
            setGraphics();
            server = new ServerConnection(1592, SH.class, 150);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private static void start(){
      finish();
      started=true;
      try {
          server.startServer();
          raspicont = new RaspiController(ardport.getText(),gpsport.getText());
      }catch (Exception e) {
         e.printStackTrace();
      }
        System.out.println("Started");


    }
    private static void finish() {
        if (started) {
            server.close();
            raspicont.stopControl();
        }
        started = false;
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
        frame.setBounds(100,100,475,160);
        open.setBounds(1,40,90,25);
        close.setBounds(1,71,90,25);
        result.setBounds(95,1,370,130);
        ardport.setBounds(1, 107, 93, 25);
        ardport.setText("/dev/ttyACM0");
        gpsport.setBounds(1, 1, 93, 25);
        gpsport.setText("/dev/ttyACM1");
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
                if (e.getNewState() == WindowEvent.WINDOW_CLOSED) ;
                finish();
            }
        });
        frame.add(close);
        frame.add(open);
        frame.add(result);
        frame.add(ardport);
        frame.add(gpsport);
        frame.add(l);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

