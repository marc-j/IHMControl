package net.alienx.ihm_stab;

import java.io.IOException;
import java.util.Vector;

import com.centralnexus.input.Joystick;
import com.centralnexus.input.JoystickListener;

public class JoyControl implements JoystickListener {
	private Joystick joy;
	private boolean init;
	private int vJoys[] = new int[5];
	private boolean buttons[] = new boolean[5];
	public static final int JOY_X = 0,JOY_Y =1, JOY_Z=2, JOY_T=3,JOY_BUT1=0;
	private Vector<JoyControlListener> listeners;
	
	private static JoyControl instance;
	public static JoyControl getInstance(){
		if(instance == null)
			instance = new JoyControl();
		return instance;
	}
	
	public JoyControl(){
		init = false;
		listeners = new Vector<JoyControlListener>();
	}
	
	public void init(int pollInterval, double deadZone){
		if(init)
			return;
		
		try{
	        for (int idx = 0; idx < Joystick.getNumDevices(); idx++) {
	            if (Joystick.isPluggedIn(idx)) {
	                joy = Joystick.createInstance(idx);
	                System.out.println("Use JoyStick "+idx);
	    			joy.addJoystickListener(this);
	    			joy.setDeadZone(deadZone);
	    			joy.setPollInterval(pollInterval);
	    	        init = true;
	                break;
	            }
	        }
			}catch(IOException ioe){
				//ioe.printStackTrace();
			}		
	}

	public boolean getStatus(){
		return init;
	}
	
	public void addJoyControlListener(JoyControlListener jcl){
		if(listeners.contains(jcl))
			return;
		listeners.add(jcl);
	}
	
	private void handleListener(){
		for(int i=0;i<listeners.size();i++)
			listeners.get(i).joyControlChange(this);
	}
	
	@Override
	public void joystickAxisChanged(Joystick js) {
		vJoys[JOY_X] = (int)Math.round(Utils.map(js.getX(), -1, 1, 1000, 2000));
		vJoys[JOY_Y] = (int)Math.round(Utils.map(js.getY(), -1, 1, 1000, 2000));
		vJoys[JOY_Z] = (int)Math.round(Utils.map(js.getR(), -1, 1, 2500, 1000));
		vJoys[JOY_T] = (int)(Math.round(Utils.map(js.getZ(), -1, 1, 1000, 2000)));	
		handleListener();
	}

	public int getAxis(int axis){
		return vJoys[axis];
	}
	
	public boolean getButton(int button){
		return buttons[button];
	}
	
	@Override
	public void joystickButtonChanged(Joystick js) {
		if((js.getButtons() & Joystick.BUTTON5) > 0){
			buttons[JOY_BUT1] = !buttons[JOY_BUT1];
			handleListener();
		}
	}

}
