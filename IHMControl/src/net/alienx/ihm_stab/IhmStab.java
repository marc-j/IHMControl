package net.alienx.ihm_stab;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.alienx.ihm_stab.protocol.Protocol;
import net.alienx.ihm_stab.protocol.msg.ProtocolMsgCommand;
import net.alienx.ihm_stab.ui.UIConfig;
import net.alienx.ihm_stab.ui.UIControl;
import net.alienx.ihm_stab.ui.UISerial;
import net.alienx.ihm_stab.ui.UITelemetry;
import net.alienx.ihm_stab.ui.components.Led;
import net.alienx.ihm_stab.ui.components.PercentBar;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import com.centralnexus.input.Joystick;
import com.centralnexus.input.JoystickListener;

public class IhmStab extends JFrame 
	implements WindowListener , Runnable{

	

	
	
	private boolean init = false;
	private Serial serial;
	
	private int Capteurs[] = new int[50];
	
	private static final short	ACC_X = 0,
								ACC_Y = 1,
								ACC_Z = 2,
								ACC_PITCH=3,
								ACC_ROLL=4;
	
	private Led onFlightLed;
	private Led serialOnLed;
	private Led joyStickConnectedLed;
	
	private Thread thread;

	private static final long serialVersionUID = 1L;

	public IhmStab(){
		JFrame.setDefaultLookAndFeelDecorated(true);
		this.setTitle("IHM Adav Stab V1.0 by Alienx");
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) (dim.getWidth()-(dim.getWidth()*17/100));
		int height = (int) (dim.getHeight()-(dim.getWidth()*10/100));
		this.setSize(width,height);
		Protocol.setPort("/dev/ttyUSB0");
		
		drawUI();
		this.setVisible(true);
		this.addWindowListener(this);
		
		
		JoyControl.getInstance().init(20, 0.1);
		
		thread = new Thread(this);
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();

	}

	
	private void drawUI(){
		this.getContentPane().setBackground(Color.white);
		JTabbedPane tabs = new JTabbedPane();
		tabs.setOpaque(true);
		tabs.addTab("Control", new UIControl());
		tabs.addTab("Configuration", new UIConfig());
		tabs.addTab("Telemetry/Graphs", new UITelemetry());
		tabs.addTab("Consol", new UISerial());
		
		JPanel pStatus = new JPanel();
		pStatus.setBackground(Color.white);
		pStatus.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		pStatus.add(new JLabel("OnFlight: "));
		onFlightLed = new Led();
		pStatus.add(onFlightLed);
		
		pStatus.add(new JLabel(" | Serial: "));
		serialOnLed = new Led();
		pStatus.add(serialOnLed);
		
		pStatus.add(new JLabel(" | JoyStick: "));
		joyStickConnectedLed = new Led();
		pStatus.add(joyStickConnectedLed);		
		
		
		getContentPane().setLayout(new BorderLayout());
		add(pStatus,BorderLayout.NORTH);
		add(tabs,BorderLayout.CENTER);
		
	}
	
	@Override
	public void windowActivated(WindowEvent e) {}
	@Override
	public void windowClosed(WindowEvent e) {
		System.exit(0);
	}
	@Override
	public void windowClosing(WindowEvent e) {
		dispose();
	}
	@Override
	public void windowDeactivated(WindowEvent e) {}
	@Override
	public void windowDeiconified(WindowEvent e) {}
	@Override
	public void windowIconified(WindowEvent e) {}
	@Override
	public void windowOpened(WindowEvent e) {}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new IhmStab();
	}


	@Override
	public void run() {
		
		try {
			
			while(true){
				serialOnLed.setStatus(Protocol.getInstance().getStatus());
				joyStickConnectedLed.setStatus(JoyControl.getInstance().getStatus());
				onFlightLed.setStatus(JoyControl.getInstance().getButton(JoyControl.JOY_BUT1));
				
				ProtocolMsgCommand msg = new ProtocolMsgCommand();
				msg.setRoll(JoyControl.getInstance().getAxis(JoyControl.JOY_X));
				msg.setPitch(JoyControl.getInstance().getAxis(JoyControl.JOY_Y));
				msg.setYaw(JoyControl.getInstance().getAxis(JoyControl.JOY_T));
				msg.setThrottle(JoyControl.getInstance().getAxis(JoyControl.JOY_Z));
				msg.setFlightMode(JoyControl.getInstance().getButton(JoyControl.JOY_BUT1) ? 1 : 0);
				Protocol.getInstance().sendMsg(msg);
				
				Thread.sleep(20);
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
