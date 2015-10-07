import java.io.File;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

public class Connecter
{
    public Connecter()
    {
        super();
    }

    void connect ( String portName ) throws Exception
    {
        System.out.print(portName+"\n");
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Error: Port is currently in use");
        }
        else
        {
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);

            if ( commPort instanceof SerialPort )
            {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(57600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);

                InputStream in = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();

                (new Thread(new SerialReader(in))).start();
                (new Thread(new SerialWriter(out))).start();
            }
            else
            {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }
    }
     void getList(){
        Enumeration ports = CommPortIdentifier.getPortIdentifiers();
        while (ports.hasMoreElements()) {
            CommPortIdentifier port = (CommPortIdentifier) ports.nextElement();
            String type;
            switch (port.getPortType()) {
                case CommPortIdentifier.PORT_PARALLEL:
                    type = "Parallel";
                    break;
                case CommPortIdentifier.PORT_SERIAL:
                    type = "Serial";
                    break;
                default: /// Shouldn't happen
                    type = "Unknown";
                    break;
            }
            System.out.println(port.getName() + ": " + type);
        }
    }

    /** */
    public static class SerialReader implements Runnable
    {
        InputStream in;

        public SerialReader ( InputStream in )
        {
            this.in = in;
        }

        public void run ()
        {
            byte[] buffer = new byte[1024];
            int len = -1;
            try
            {
                while ( ( len = this.in.read(buffer)) > -1 )
                {
                    System.out.print(new String(buffer,0,len));
                }
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }
        }
    }

    /** */
    public static class SerialWriter implements Runnable
    {
        OutputStream out;

        public SerialWriter ( OutputStream out )
        {
            this.out = out;
        }

        public void run ()
        {
            try
            {
                int c = 0;
                while ( ( c = System.in.read()) > -1 )
                {
                    this.out.write(c);
                }
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }
        }
    }

    public static void main ( String[] args )
    {
        try
        {

            String str;
            try {
                str = args[0];
            }catch (Exception ignored){
                str="/dev/ttyUSB0";
            }
            setNatives();

            Connecter c=new Connecter();
            c.getList();
            c.connect(str);
        }
        catch ( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private static void setNatives() {
        //set path to the natives
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
}



