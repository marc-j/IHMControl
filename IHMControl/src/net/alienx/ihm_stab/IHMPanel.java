package net.alienx.ihm_stab;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import net.alienx.ihm_stab.protocol.Protocol;

abstract public class IHMPanel extends JPanel {

	public IHMPanel(){
		this.init();
		this.drawUI();
	}
	
	/**
	 * Abstract methode draw UI
	 */
	abstract protected void drawUI();
	
	/**
	 * Abstract methode draw UI
	 */
	
	abstract protected void init();
	
	
	protected Protocol getProtocol(){
		return Protocol.getInstance();
	}
	
	protected Protocol getProtocol(String port){
		Protocol.setPort(port);
		return Protocol.getInstance();
	}
	
	protected void addJoyControlListener(JoyControlListener listener){
		JoyControl.getInstance().addJoyControlListener(listener);
	}
	
	protected void addBorder(JPanel p, String title){
		p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(title),
                BorderFactory.createEmptyBorder(5,5,5,5)));
	}
}
