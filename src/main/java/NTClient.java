import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.JFrame;

import edu.wpi.first.cscore.CameraServerJNI;
import edu.wpi.first.math.WPIMathJNI;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTablesJNI;
import edu.wpi.first.util.CombinedRuntimeLoader;
import edu.wpi.first.util.WPIUtilJNI;

public class NTClient implements KeyListener
{
    private static final double FPS = 60;
    private DoubleSubscriber trow, tcol;
    private DoublePublisher wrow, wcol;
    public static void main(String[] args) throws IOException
    {
        NetworkTablesJNI.Helper.setExtractOnStaticLoad(false);
        WPIUtilJNI.Helper.setExtractOnStaticLoad(false);
        WPIMathJNI.Helper.setExtractOnStaticLoad(false);
        CameraServerJNI.Helper.setExtractOnStaticLoad(false);
        CombinedRuntimeLoader.loadLibraries(NTClient.class, "wpiutiljni", "wpimathjni", "ntcorejni",
                "cscorejnicvstatic");
        new NTClient().run();
    }
    private void run() {
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        inst.startClient4("piece selector client");
        inst.setServer("localhost");
        inst.startDSClient();
        NetworkTable table = inst.getTable("pieceSelector");
        trow = table.getDoubleTopic("row").subscribe(0.0);
        tcol = table.getDoubleTopic("col").subscribe(0.0);
        wrow = table.getDoubleTopic("row").publish();
        wcol = table.getDoubleTopic("col").publish();
        JFrame frame = new JFrame("Northern Force's Piece Display Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setSize(900, 500);
        frame.addKeyListener(this);
        Graphics g = frame.getGraphics();
        try
        {
            Thread.sleep(500);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        while (true)
        {
            long startTimeMillis = System.currentTimeMillis();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, 900, 500);
            g.setColor(Color.GREEN);
            g.fillRect((int)(tcol.get() * 100), (int)(trow.get() * 500 / 3), 100, 500 / 3);
            long endTimeMillis = System.currentTimeMillis();
            long diff = endTimeMillis - startTimeMillis;
            if (diff < (1000.0 / FPS))
            {
                try
                {
                    Thread.sleep((long)(1000.0 / FPS - diff));
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public void keyPressed(KeyEvent arg0) {
        switch (arg0.getKeyChar())
        {
        case '1':
            wcol.set(0);
            break;
        case '2':
            wcol.set(1);
            break;
        case '3':
            wcol.set(2);
            break;
        case '4':
            wcol.set(3);
            break;
        case '5':
            wcol.set(4);
            break;
        case '6':
            wcol.set(5);
            break;
        case '7':
            wcol.set(6);
            break;
        case '8':
            wcol.set(7);
            break;
        case '9':
            wcol.set(8);
            break;
        case 'q':
            wrow.set(0);
            break;
        case 'w':
            wrow.set(0);
            break;
        case 'e':
            wrow.set(0);
            break;
        case 'r':
            wrow.set(0);
            break;
        case 't':
            wrow.set(0);
            break;
        case 'y':
            wrow.set(0);
            break;
        case 'u':
            wrow.set(0);
            break;
        case 'i':
            wrow.set(0);
            break;
        case 'o':
            wrow.set(0);
            break;
        case 'p':
            wrow.set(0);
            break;
        case 'a':
            wrow.set(1);
            break;
        case 's':
            wrow.set(1);
            break;
        case 'd':
            wrow.set(1);
            break;
        case 'f':
            wrow.set(1);
            break;
        case 'g':
            wrow.set(1);
            break;
        case 'h':
            wrow.set(1);
            break;
        case 'j':
            wrow.set(1);
            break;
        case 'k':
            wrow.set(1);
            break;
        case 'l':
            wrow.set(1);
            break;
        case 'z':
            wrow.set(2);
            break;
        case 'x':
            wrow.set(2);
            break;
        case 'c':
            wrow.set(2);
            break;
        case 'v':
            wrow.set(2);
            break;
        case 'b':
            wrow.set(2);
            break;
        case 'n':
            wrow.set(2);
            break;
        case 'm':
            wrow.set(2);
            break;
        }
    }
    @Override
    public void keyReleased(KeyEvent arg0) {
    }
    @Override
    public void keyTyped(KeyEvent arg0) {
    }
};