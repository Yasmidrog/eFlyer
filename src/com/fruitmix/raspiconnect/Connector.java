package com.fruitmix.raspiconnect;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;



public class Connector implements SerialPortEventListener {
    SerialPort serialPort;

    protected byte[] bytes=new byte[]{};

    protected BufferedReader input;

    protected OutputStream output;
    /**
     * Milliseconds to block while waiting for port open
     */
    private static final int TIME_OUT = 2000;
    /**
     * Default bits per second for COM port.
     */
    private static final int DATA_RATE = 9600;
    public void initialize(String port) {

        System.setProperty("gnu.io.rxtx.SerialPorts", port);

        CommPortIdentifier portId = null;
        try {
            portId = CommPortIdentifier.getPortIdentifier(port);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (portId == null) {
            System.out.println("Could not find COM port.");
            return;
        }

        try {
            serialPort = (SerialPort) portId.open(this.getClass().getName(),
                    TIME_OUT);

            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            output = serialPort.getOutputStream();

            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void close() {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }


    public synchronized void serialEvent(SerialPortEvent oEvent) {

        System.out.println("----");
        try {
            if(oEvent.getEventType()==SerialPortEvent.DATA_AVAILABLE)
                System.out.println(input.readLine());
            if(oEvent.getEventType()==SerialPortEvent.OUTPUT_BUFFER_EMPTY)
                output.write("Test".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
   public void setCommand(byte[] b){
       bytes=b;
   }
}
