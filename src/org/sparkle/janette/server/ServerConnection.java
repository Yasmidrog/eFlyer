package org.sparkle.janette.server;

import org.sparkle.jbind.JBinD;
import org.sparkle.jbind.Reader;
import org.sparkle.jbind.Writer;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yew_mentzaki
 */
public final class ServerConnection {
    Timer timer=new Timer();
    private class clientConnection {

        Socket client;
        ServerHandler serverhandler;

        public clientConnection(Socket client) {
            try {
                this.client = client;
                serverhandler = (ServerHandler) handler.newInstance();
                get.start();
            } catch (InstantiationException ex) {
                Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public void send() {
            try {
                JBinD b = serverhandler.out();
                if (b == null) {
                    return;
                }
                byte[] bytes = Writer.write(b);
                if (client.isClosed()) {
                    connections.remove(this);
                    return;
                }
                DataOutputStream dos = new DataOutputStream(client.getOutputStream());
                try {
                    dos.writeInt(bytes.length);
                    dos.write(bytes, 0, bytes.length);
                } catch (SocketException ignored){}
            }
                catch(IOException ex) {
                Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    client.close();
                } catch (IOException ex1) {
                    Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex1);
                }
                remove();
                get.stop();
            }

        }
        Thread get = new Thread() {

            @Override
            public void run() {
                InputStream inputStream;
                try {
                    inputStream = client.getInputStream();
                    DataInputStream dis = new DataInputStream(inputStream);
                    while (true) {
                        int length = dis.readInt();
                        if (length <= 0) {
                            System.out.println("Got empty message...");
                            continue;
                        }
                        byte[] message = new byte[length];
                        dis.read(message);
                        JBinD bind = Reader.read(message);
                        serverhandler.in(bind);
                    }
                }catch (SocketException ignored){}
                catch (IOException ex) {
                    Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
                    remove();
                    get.stop();
                } finally {

                }
            }

        };

        public void remove() {
            connections.remove(this);
        }

        public void close() {
            get.stop();
        }
    }

    private ArrayList<clientConnection> connections = new ArrayList<clientConnection>();

    int port;
    Class handler;
    int sendingDelay = 350;

    public ServerConnection(int port, Class handler, int sendingDelay) {
        this.port = port;
        this.handler = handler;
        this.sendingDelay = sendingDelay;
    }

    @Override
    protected void finalize() throws Throwable {
        close();
    }

    public void close() {
        try{
            socket.close();
            newClients.stop();
            timer.cancel();
            timer.purge();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (clientConnection c : connections) {
            c.close();
        }
    }

    Thread newClients;
    ServerSocket socket;

    public void startServer() throws IOException {
        socket = new ServerSocket(port);
        startTimer();
        System.gc();
    }
    private void startTimer(){
        newClients= new Thread() {
            @Override
            public void run() {
                acceptClients();
            }
        };
        timer=new Timer();
        timer.schedule( new TimerTask() {
            public void run() {
              sendToClients();
            }
        },0, sendingDelay);
        newClients.start();
    }
    private void acceptClients(){
        while (true) {
            try {
                Socket client = socket.accept();
                connections.add(new clientConnection(client));
            } catch (IOException ex) {
                Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
            finally {

            }
        }
    }
    private void sendToClients(){
        try {
            for (clientConnection c : connections) {
                c.send();
            }
        } catch (Exception ex) {
             ex.printStackTrace();
        }
    }
}
