import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.JFrame;

import edu.wpi.first.cscore.CameraServerJNI;
import edu.wpi.first.math.WPIMathJNI;
import edu.wpi.first.networktables.DoubleArrayPublisher;
import edu.wpi.first.networktables.DoubleArraySubscriber;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTablesJNI;
import edu.wpi.first.util.CombinedRuntimeLoader;
import edu.wpi.first.util.WPIUtilJNI;

public class Program implements KeyListener
{
    private static final double FPS = 60;
    private DoubleSubscriber trow, tcol;
    private DoublePublisher wrow, wcol;
    private DoubleArrayPublisher parray;
    private DoubleArraySubscriber sarray;
    public static void main(String[] args) throws IOException
    {
        NetworkTablesJNI.Helper.setExtractOnStaticLoad(false);
        WPIUtilJNI.Helper.setExtractOnStaticLoad(false);
        WPIMathJNI.Helper.setExtractOnStaticLoad(false);
        CameraServerJNI.Helper.setExtractOnStaticLoad(false);
        CombinedRuntimeLoader.loadLibraries(Program.class, "wpiutiljni", "wpimathjni", "ntcorejni",
                "cscorejnicvstatic");
        new Program().run();
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
        sarray = table.getDoubleArrayTopic("array").subscribe(new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        parray = table.getDoubleArrayTopic("array").publish();
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
            double[] arr = sarray.get();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, 900, 500);
            g.setColor(Color.GREEN);
            g.fillRect((int)(tcol.get() * 100), (int)(trow.get() * 500 / 3), 100, 500 / 3);
            for (int i = 0; i < 9; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    if (arr[j * 9 + i] == 1)
                    {
                        g.setColor(Color.BLUE);
                        g.fillRect((int)(i * 100), (int)(j * 500 / 3), 100, 500 / 3);
                    }
                    if (j != 2)
                    {
                        if (i == 0 || i == 2 || i == 3 || i == 5 || i == 6 || i == 8)
                        {
                            g.setColor(Color.BLACK);
                            g.fillRect(i * 100 + 40, (int)(j * 500.0 / 3) + 40, 20, (int)(500.0 / 3 - 40));
                        }
                        else
                        {
                            g.setColor(new Color(0xFF, 0, 0xFF));
                            g.fillRect(i * 100 + 20, (int)(j * 500.0 / 3 + 53.33), 60, 60);
                        }
                    }
                }
            }
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
        case 'w':
            wrow.set(Math.max(trow.get() - 1, 0));
            break;
        case 'a':
            wcol.set(Math.max(tcol.get() - 1, 0));
            break;
        case 's':
            wrow.set(Math.min(trow.get() + 1, 2));
            break;
        case 'd':
            wcol.set(Math.min(tcol.get() + 1, 8));
            break;
        case 'q':
            double[] arr = sarray.get();
            arr[(int)(trow.get() * 9 + tcol.get())] = 1 - arr[(int)(trow.get() * 9 + tcol.get())];
            parray.set(arr);
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