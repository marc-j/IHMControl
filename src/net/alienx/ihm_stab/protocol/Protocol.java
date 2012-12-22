package net.alienx.ihm_stab.protocol;

import java.util.Vector;

import net.alienx.ihm_stab.Serial;
import net.alienx.ihm_stab.SerialListener;
import net.alienx.ihm_stab.Utils;
import net.alienx.ihm_stab.protocol.msg.*;

public class Protocol implements SerialListener {

	public static final short PROTOCOL_CMD_CAPTEURS=1;
	public static final short PROTOCOL_CMD_SYSTEM=2;
	public static final short PROTOCOL_CMD_COMMAND=3;
	public static final short PROTOCOL_CMD_PID=4;
	public static final short PROTOCOL_CMD_MOTOR=5;
	
	private Serial _serial;
	private static String _port;
	private static Protocol instance;
	private Vector<ProtocolListener> listeners;
	
	public static Protocol getInstance(){
			if(instance == null){
				instance = new Protocol();
			}
			return instance;
	}
	
	private Protocol() {
		_serial = new Serial(_port,115200,8,1,0);
		_serial.addSerialListener(this);	
		listeners = new Vector<ProtocolListener>();
	}
	
	public static void setPort(String port){
		_port = port;
	}

	public boolean getStatus(){
		return _serial.getStatus();
	}
	
	public Serial getSerial(){
		return _serial;
	}
	
	public void addProtocolListener(ProtocolListener serial){
		if(listeners.contains(serial))
			return;
		listeners.add(serial);
	}
	
	private void handleListener(short cmd, ProtocolMessage msg){
		for(int i=0;i<listeners.size();i++)
			listeners.get(i).protocolDataReceive(cmd,msg);
	}
	
	@Override
	public void SerialCmdRecv(byte[] datas) {
		byte len = datas[1];
		if(len == 0)
			return;
		
		short cmd = (short)datas[0];
		switch(cmd){
			case PROTOCOL_CMD_CAPTEURS:
				ProtocolMsgCapteurs msg = new ProtocolMsgCapteurs();
				msg.setDatas(datas);
				handleListener(cmd,msg);
				break;
			case PROTOCOL_CMD_SYSTEM:
				ProtocolMsgSystem msg1 = new ProtocolMsgSystem();
				msg1.setDatas(datas);
				handleListener(cmd,msg1);
				break;
			case PROTOCOL_CMD_PID:
				ProtocolMsgPID msg2 = new ProtocolMsgPID();
				msg2.setDatas(datas);
				handleListener(cmd,msg2);
				break;
			case PROTOCOL_CMD_MOTOR:
				ProtocolMsgMotor msg3 = new ProtocolMsgMotor();
				msg3.setDatas(datas);
				handleListener(cmd,msg3);
				break;
		}
	}
	
	public void sendMsg(ProtocolMessage msg){
		_serial.sendToEngine(msg.getCmdId(), msg.getDatas());
	}

	@Override
	public void SerialRowRecv(byte data) {
		// TODO Auto-generated method stub
		
	}
}
