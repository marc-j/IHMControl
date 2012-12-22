package net.alienx.ihm_stab.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.alienx.ihm_stab.IHMPanel;
import net.alienx.ihm_stab.Config;
import net.alienx.ihm_stab.JoyControl;
import net.alienx.ihm_stab.JoyControlListener;
import net.alienx.ihm_stab.Utils;
import net.alienx.ihm_stab.protocol.ProtocolListener;
import net.alienx.ihm_stab.protocol.ProtocolMessage;
import net.alienx.ihm_stab.protocol.msg.ProtocolMsgCapteurs;
import net.alienx.ihm_stab.protocol.msg.ProtocolMsgMotor;
import net.alienx.ihm_stab.protocol.msg.ProtocolMsgPID;
import net.alienx.ihm_stab.protocol.msg.ProtocolMsgSystem;
import net.alienx.ihm_stab.ui.components.Gauge;
import net.alienx.ihm_stab.ui.components.Horizon;
import net.alienx.ihm_stab.ui.components.MotorSpeed;
import net.alienx.ihm_stab.ui.components.PercentBar;
import net.alienx.ihm_stab.ui.components.TurnCoordinator;

public class UIControl extends IHMPanel 
	implements ProtocolListener, JoyControlListener {
	

	private JLabel lJoys[];

	
	private PercentBar mainLoopBar;
	private PercentBar cpuLoadBar;
	private PercentBar batteryVoltageBar;
	private JLabel mainLoopLabel;
	private JLabel cpuLoadLabel;
	private JLabel batteryVoltageLabel;
	private PercentBar motorFrontLeft,
					   motorFrontRight,
					   motorRearLeft,
					   motorRearRight;
	
	private MotorSpeed mlf;
	
	private Gauge[] gACC;
	private Gauge[] gGYR;
	
	private TurnCoordinator tCoordinator;
	private Gauge gRoll,gPitch,gYaw;
	private Horizon m_horizon;
	
	@Override
	protected void drawUI() {
		this.setLayout(new BorderLayout());
		Color bgColor = getConfig().getColor(Config.NODE_UI,"background_color", Color.DARK_GRAY);
		
		JPanel pHead = new JPanel();
		pHead.setLayout(new GridBagLayout());
		GridBagConstraints gdb2 = new GridBagConstraints();
		gdb2.insets = new Insets(2,2,2,2);
		gdb2.gridx = 0;
		gdb2.gridy = 0;
		gdb2.fill = GridBagConstraints.BOTH;
		gdb2.weightx = 0.5;
		
		/********************************
		 * Joystick
		 */
		JPanel pJoys = new JPanel();
		addBorder(pJoys,"Joystick");
		pJoys.setMinimumSize(new Dimension(100,100));
		pJoys.setLayout(new GridLayout(0,4));
		lJoys = new JLabel[4];
		lJoys[0] = new JLabel("0");
		lJoys[1] = new JLabel("0");
		lJoys[2] = new JLabel("0");
		lJoys[3] = new JLabel("0");
		
		pJoys.add(new JLabel("X: ", JLabel.RIGHT));
		pJoys.add(lJoys[JoyControl.JOY_X]);
		pJoys.add(new JLabel("Y: ", JLabel.RIGHT));
		pJoys.add(lJoys[JoyControl.JOY_Y]);
		pJoys.add(new JLabel("Z: ", JLabel.RIGHT));
		pJoys.add(lJoys[JoyControl.JOY_Z]);
		pJoys.add(new JLabel("Throttle: ", JLabel.RIGHT));
		pJoys.add(lJoys[JoyControl.JOY_T]);

		/*********************************
		 * System Status
		 */
		
		JPanel pSystem = new JPanel();
		addBorder(pSystem,"System");
		pSystem.setLayout(new GridBagLayout());
		GridBagConstraints gdb1 = new GridBagConstraints();
		gdb1.insets = new Insets(2,2,2,2);
		gdb1.gridx = 0;
		gdb1.gridy = 0;
		gdb1.fill = GridBagConstraints.BOTH;

		//Main Loop Time
		pSystem.add(new JLabel("Main Loop: "),gdb1);	
		gdb1.gridx++;
		gdb1.weightx = 0.5;
		mainLoopLabel = new JLabel("0ms",JLabel.RIGHT);
		pSystem.add(mainLoopLabel,gdb1);
		
		gdb1.gridx++;
		gdb1.weightx = 1;
		mainLoopBar = new PercentBar(PercentBar.HORIZONTAL);
		pSystem.add(mainLoopBar,gdb1);
		
		//CPU LOAD
		gdb1.gridx=0;
		gdb1.gridy++;
		pSystem.add(new JLabel("CPU Load: "),gdb1);	
		gdb1.gridx++;
		gdb1.weightx = 0.5;
		cpuLoadLabel = new JLabel("0%",JLabel.RIGHT);
		pSystem.add(cpuLoadLabel,gdb1);
		
		gdb1.gridx++;
		gdb1.weightx = 1;
		cpuLoadBar = new PercentBar(PercentBar.HORIZONTAL,Color.white,Color.gray,Color.green,Color.red);
		cpuLoadBar.setPercent(0);
		pSystem.add(cpuLoadBar,gdb1);
		
		//Battery Voltage
		gdb1.gridx=0;
		gdb1.gridy++;
		pSystem.add(new JLabel("Battery Voltage: "),gdb1);	
		gdb1.gridx++;
		gdb1.weightx = 0.5;
		batteryVoltageLabel = new JLabel("0.00 V",JLabel.RIGHT);
		pSystem.add(batteryVoltageLabel,gdb1);
		
		gdb1.gridx++;
		gdb1.weightx = 1;
		batteryVoltageBar = new PercentBar(PercentBar.HORIZONTAL,Color.white,Color.gray,Color.red,Color.green);
		batteryVoltageBar.setPercent(0);
		pSystem.add(batteryVoltageBar,gdb1);
		
		
		gdb2.gridheight = 1;
		gdb2.weightx = 0.8;
		gdb2.weighty = 0;
		pHead.add(pJoys,gdb2);
		
		gdb2.gridy = 1;
		gdb2.gridheight = 3;
		gdb2.weighty = 1;
		pHead.add(pSystem,gdb2);
		
		/*************************************************
		 * Motor
		 */
		
		JPanel pMotor = new JPanel();
		addBorder(pMotor,"Motor");
		pMotor.setLayout(new GridBagLayout());
		GridBagConstraints gdb3 = new GridBagConstraints();
		gdb3.insets = new Insets(2,2,2,2);
		gdb3.gridx = 0;
		gdb3.gridy = 0;
		gdb3.fill = GridBagConstraints.BOTH;

		//Motor FL
		pMotor.add(new JLabel("Front Left: "),gdb3);			
		gdb3.gridx++;
		gdb3.weightx = 1;
		motorFrontLeft = new PercentBar(PercentBar.HORIZONTAL);
		pMotor.add(motorFrontLeft,gdb3);
		
		//Motor FR
		gdb3.gridy++;
		gdb3.gridx = 0;
		pMotor.add(new JLabel("Front Right: "),gdb3);	
		
		gdb3.gridx++;
		gdb3.weightx = 1;
		motorFrontRight = new PercentBar(PercentBar.HORIZONTAL);
		pMotor.add(motorFrontRight,gdb3);
		
		//Motor RL
		gdb3.gridy++;
		gdb3.gridx = 0;
		pMotor.add(new JLabel("Rear Left: "),gdb3);	
		
		gdb3.gridx++;
		gdb3.weightx = 1;
		motorRearLeft = new PercentBar(PercentBar.HORIZONTAL);
		pMotor.add(motorRearLeft,gdb3);
		
		
		//Motor RR
		gdb3.gridy++;
		gdb3.gridx = 0;
		pMotor.add(new JLabel("Rear Right: "),gdb3);	
		
		gdb3.gridx++;
		gdb3.weightx = 1;
		motorRearRight = new PercentBar(PercentBar.HORIZONTAL);
		pMotor.add(motorRearRight,gdb3);
		
		// padding empty ...
		gdb3.gridy++;
		gdb3.gridx = 0;
		gdb3.gridheight = 2;
		gdb3.weighty = 3;
		pMotor.add(new JPanel(),gdb3);

		gdb2.gridy = 0;
		gdb2.gridx++;
		gdb2.weightx = 1;
		gdb2.gridheight = 2;
		gdb2.weighty = 5;
		pHead.add(pMotor,gdb2);
		

		
		/******************************************************
		 * Telemetry Angle / Command
		 */
		
		JPanel pAngle = new JPanel();
		//addBorder(pAngle,"Angle");
		pAngle.setLayout(new FlowLayout(FlowLayout.LEFT));
		pAngle.setBackground(bgColor);
		Dimension gSize = new Dimension(80,80);
		gACC = new Gauge[3];
		gACC[0] = new Gauge("ACC X",-180,180,gSize,bgColor);
		pAngle.add(gACC[0]);
		gACC[1] = new Gauge("ACC Y",-180,180,gSize,bgColor);
		pAngle.add(gACC[1]);
		gACC[2] = new Gauge("ACC Z",0,1024,gSize,bgColor);
		pAngle.add(gACC[2]);
		
		gGYR = new Gauge[3];
		gGYR[0] = new Gauge("GYR X",-90,90,gSize,bgColor);
		pAngle.add(gGYR[0]);
		gGYR[1] = new Gauge("GYR Y",-90,90,gSize,bgColor);
		pAngle.add(gGYR[1]);
		gGYR[2] = new Gauge("GYR Z",-90,90,gSize,bgColor);
		pAngle.add(gGYR[2]);
		
		gdb2.gridy =0;
		gdb2.gridx++;
		gdb2.weightx = 10;
		gdb2.gridheight = 2;
		gdb2.weighty = 10;	
		pHead.add(pAngle,gdb2);
		
		this.add(pHead,BorderLayout.NORTH);
		
		JPanel pCenter = new JPanel();
		pCenter.setLayout(new FlowLayout(FlowLayout.CENTER));
		pCenter.setBackground(bgColor);
		tCoordinator = new TurnCoordinator();
		pCenter.add(tCoordinator);
		
		gRoll = new Gauge("ROLL",-90,90,bgColor);
		pCenter.add(gRoll);

		gPitch = new Gauge("PITCH",-90,90,bgColor);
		pCenter.add(gPitch);
		
		gYaw = new Gauge("YAW",-90,90,bgColor);
		pCenter.add(gYaw);
		
		m_horizon = new Horizon(bgColor);
		pCenter.add(m_horizon);
		
		//mlf = new MotorSpeed();
		//pCenter.add(mlf);
		this.add(pCenter,BorderLayout.CENTER);
		//pMain.add(pHead,BorderLayout.NORTH);
		
	}
	

	

	
	@Override
	protected void init() {
		getProtocol().addProtocolListener(this);
		addJoyControlListener(this);
		
	}
	
	@Override
	public void protocolDataReceive(int cmd, ProtocolMessage msg) {
		
		if(msg instanceof ProtocolMsgSystem){
			mainLoopLabel.setText(Float.toString(((ProtocolMsgSystem) msg).getMainLoopTime())+" ms");
			batteryVoltageLabel.setText(Float.toString(((ProtocolMsgSystem) msg).getBatteryVoltage())+" V");
			cpuLoadLabel.setText(Float.toString(((ProtocolMsgSystem) msg).getCpuLoad())+" %");
			double loadPercent = Utils.map(((ProtocolMsgSystem) msg).getCpuLoad(), 0, 1, 0, 100);
			cpuLoadBar.setPercent((int)loadPercent);
		}else if(msg instanceof ProtocolMsgCapteurs){
			gRoll.setValue(((ProtocolMsgCapteurs) msg).getRoll());
			gPitch.setValue(((ProtocolMsgCapteurs) msg).getPitch());
			gYaw.setValue(((ProtocolMsgCapteurs) msg).getYaw());
			m_horizon.setRoll(((ProtocolMsgCapteurs) msg).getRoll());
			m_horizon.setPitch(((ProtocolMsgCapteurs) msg).getPitch());
			tCoordinator.setAngleDeg(((ProtocolMsgCapteurs) msg).getRoll());
			gACC[0].setValue(((ProtocolMsgCapteurs) msg).getAccX());
			gACC[1].setValue(((ProtocolMsgCapteurs) msg).getAccY());
			gACC[2].setValue(((ProtocolMsgCapteurs) msg).getAccZ());
			gGYR[0].setValue(((ProtocolMsgCapteurs) msg).getGyroX());
			gGYR[1].setValue(((ProtocolMsgCapteurs) msg).getGyroY());
			gGYR[2].setValue(((ProtocolMsgCapteurs) msg).getGyroZ());
		}else if(msg instanceof ProtocolMsgMotor){
			double FL = Utils.map(((ProtocolMsgMotor) msg).getMotorFrontLeft(), 1000, 2500, 0, 100);
			motorFrontLeft.setPercent((int)FL);
			double FR = Utils.map(((ProtocolMsgMotor) msg).getMotorFrontRight(), 1000, 2500, 0, 100);
			motorFrontRight.setPercent((int)FR);
			double RL = Utils.map(((ProtocolMsgMotor) msg).getMotorRearLeft(), 1000, 2500, 0, 100);
			motorRearLeft.setPercent((int)RL);
			double RR = Utils.map(((ProtocolMsgMotor) msg).getMotorRearRight(), 1000, 2500, 0, 100);
			motorRearRight.setPercent((int)RR);
		}
		
	}

	@Override
	public void joyControlChange(JoyControl joy) {
		lJoys[JoyControl.JOY_X].setText(Integer.toString(joy.getAxis(JoyControl.JOY_X)));
		lJoys[JoyControl.JOY_Y].setText(Integer.toString(joy.getAxis(JoyControl.JOY_Y)));
		lJoys[JoyControl.JOY_Z].setText(Integer.toString(joy.getAxis(JoyControl.JOY_T)));
		lJoys[JoyControl.JOY_T].setText(Integer.toString(joy.getAxis(JoyControl.JOY_Z)));
	}

}
