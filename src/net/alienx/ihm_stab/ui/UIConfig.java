package net.alienx.ihm_stab.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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

import net.alienx.ihm_stab.IHMPanel;
import net.alienx.ihm_stab.protocol.ProtocolListener;
import net.alienx.ihm_stab.protocol.ProtocolMessage;
import net.alienx.ihm_stab.protocol.msg.ProtocolMsgPID;

public class UIConfig extends IHMPanel implements ProtocolListener, PropertyChangeListener, ActionListener {

	
	private ProtocolMsgPID msgPID;	
	private JFormattedTextField [] PIDVals;
	private JSlider[] PIDSliders;
	private JButton bPIDRValid;
	private static final int      RP = 0,
		    RI = 1,
		    RD = 2,
		    PP = 3,
		    PI = 4,
		    PD = 5,
		    YP = 6,
		    YI = 7,
		    YD = 8;
	public static final String[] KEYS =  {"RP","RI","RD","PP","PI","PD","YP","YI","YD"};
	
	@Override
	protected void drawUI() {
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints gdb2 = new GridBagConstraints();
		gdb2.insets = new Insets(2,2,2,2);
		gdb2.gridx = 0;
		gdb2.gridy = 0;
		gdb2.fill = GridBagConstraints.BOTH;
		gdb2.weightx = 0.5;
		
		PIDVals = new JFormattedTextField[3*3];
		PIDSliders = new JSlider[3*3];
		/********************************
		 * PID X
		 */
		JPanel pPIDX = new JPanel();
		pPIDX.setBackground(new Color(157,223,254));
		addBorder(pPIDX,"PID Pitch");
		createPIDBox(pPIDX,PP);

		/********************************
		 * PID Y
		 */
		JPanel pPIDY = new JPanel();
		pPIDY.setBackground(new Color(254,160,157));
		addBorder(pPIDY,"PID Roll");
		createPIDBox(pPIDY,RP);
		
		/********************************
		 * PID Z
		 */
		JPanel pPIDZ = new JPanel();
		pPIDZ.setBackground(new Color(205,254,157));
		addBorder(pPIDZ,"PID Yaw");
		createPIDBox(pPIDZ,YP);
		
		gdb2.gridy = 0;
		gdb2.gridx++;
		gdb2.weightx = 0.5;
		gdb2.gridheight = 2;
		gdb2.weighty = 10;
		this.add(pPIDX,gdb2);
		
		gdb2.gridx++;
		this.add(pPIDY,gdb2);
		
		gdb2.gridx++;
		this.add(pPIDZ,gdb2);
		
		gdb2.gridy = 3;
		gdb2.gridx = 3;
		gdb2.gridheight = 1;
		gdb2.fill = GridBagConstraints.HORIZONTAL;
		bPIDRValid = new JButton("Send To Engine");
		bPIDRValid.addActionListener(this);
		this.add(bPIDRValid,gdb2);
	}
	
	private void createPIDBox(JPanel p,int index){
		p.setLayout(new GridBagLayout());
		GridBagConstraints gdb = new GridBagConstraints();
		gdb.insets = new Insets(2,2,2,2);
		gdb.gridx = 0;
		gdb.gridy = 0;
		gdb.fill = GridBagConstraints.BOTH;
		
		String[] keys = {"Kp:","Ki:","Kd:"};
		int y = 0;
		for(int i=0;i<keys.length;i++){
			gdb.gridx = 0;
			gdb.gridy = y++;
			gdb.weightx = 0.2;
			p.add(new JLabel(keys[i]),gdb);
			
			PIDVals[index+i] = new JFormattedTextField(NumberFormat.getNumberInstance());
			PIDVals[index+i].setValue(0);
			PIDVals[index+i].setColumns(10);
			PIDVals[index+i].addPropertyChangeListener("value", this);
			
			gdb.gridx = 1;
			gdb.weightx = 1;
			p.add(PIDVals[index+i],gdb);
			
			/*PIDSliders[index+i] = new JSlider(JSlider.HORIZONTAL,-2000,2000,(int)(0*1000d));
			PIDSliders[index+i].addChangeListener(this);
			PIDSliders[index+i].setMajorTickSpacing(-2);
			PIDSliders[index+i].setMinorTickSpacing(2);
			PIDSliders[index+i].setPaintTicks(true);
			PIDSliders[index+i].setPaintLabels(true);

			gdb.gridx = 1;
			gdb.gridy = y++;	
			gdb.weightx = 1;
			p.add(PIDSliders[index+i],gdb);*/
		}
	}
	
	private void sendNewPID(){
		msgPID.setRollKP(((Number)PIDVals[RP].getValue()).floatValue());
		msgPID.setRollKI(((Number)PIDVals[RI].getValue()).floatValue());
		msgPID.setRollKD(((Number)PIDVals[RD].getValue()).floatValue());
		msgPID.setPitchKP(((Number)PIDVals[PP].getValue()).floatValue());
		msgPID.setPitchKI(((Number)PIDVals[PI].getValue()).floatValue());
		msgPID.setPitchKD(((Number)PIDVals[PD].getValue()).floatValue());
		msgPID.setYawKP(((Number)PIDVals[YP].getValue()).floatValue());
		msgPID.setYawKI(((Number)PIDVals[YI].getValue()).floatValue());
		msgPID.setYawKD(((Number)PIDVals[YD].getValue()).floatValue());
		getProtocol().sendMsg(msgPID);
	}

	@Override
	protected void init() {
		getProtocol().addProtocolListener(this);
		msgPID = new ProtocolMsgPID();
		
	}

	@Override
	public void protocolDataReceive(int cmd, ProtocolMessage msg) {
		if(msg instanceof ProtocolMsgPID){
			System.out.println("PID RECEIVE");
			PIDVals[RP].setValue(new Float(((ProtocolMsgPID) msg).getRollKP()));
			System.out.println(((ProtocolMsgPID) msg).getRollKP());
			PIDVals[RI].setValue(new Float(((ProtocolMsgPID) msg).getRollKI()));
			PIDVals[RD].setValue(new Float(((ProtocolMsgPID) msg).getRollKD()));
			PIDVals[PP].setValue(new Float(((ProtocolMsgPID) msg).getPitchKP()));
			PIDVals[PI].setValue(new Float(((ProtocolMsgPID) msg).getPitchKI()));
			PIDVals[PD].setValue(new Float(((ProtocolMsgPID) msg).getPitchKD()));
			PIDVals[YP].setValue(new Float(((ProtocolMsgPID) msg).getYawKP()));
			PIDVals[YI].setValue(new Float(((ProtocolMsgPID) msg).getYawKI()));
			PIDVals[YD].setValue(new Float(((ProtocolMsgPID) msg).getYawKD()));
		}
		
	}

/*	@Override
	public void stateChanged(ChangeEvent e) {
		/*JSlider source = (JSlider)e.getSource();
		for(int i=0;i<3*3-1;i++){
			if(source == PIDSliders[i]){
				PIDVals[i].setValue(new Double((double)source.getValue()/1000d));
			}
		}
		
	}*/

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		Object source = e.getSource();
		for(int i=0;i<3*3-1;i++){
			if(source == PIDVals[i]){
				double value = ((Number)PIDVals[i].getValue()).doubleValue();
				//PIDSliders[i].setValue((int)(value*1000d));
			}
		}		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source == bPIDRValid){
			sendNewPID();
		}
	}

}
