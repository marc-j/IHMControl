package net.alienx.ihm_stab;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

public class Serial implements Runnable{
	private Thread thread;
	private SerialPort sport;
	private InputStream in;
	private OutputStream out;
	private boolean SerialConnected = false;
	private boolean init = false;
	private short step=0;
	
	private final short STX1=0,STX2=1,CTX=2,LTX=3,DTX=4,CRC=5;
	private short index=0;
	private short len=0;
	private byte buffer[] = new byte[256];
	private byte crc=0;
	
	private Vector<SerialListener> listeners;
	
	public Serial(String port,int bauds, int databytes, int stop, int parity){
		listeners = new Vector<SerialListener>();
		try{
			System.out.println("Port: "+port);
			CommPortIdentifier cident = CommPortIdentifier.getPortIdentifier(port);
			if( cident.isCurrentlyOwned() )
				throw new Exception("Port en cours d'utilistion");
			
			CommPort commPort = cident.open(this.getClass().getName(),20000);
			if( commPort instanceof SerialPort ){
				sport = (SerialPort) commPort;
				sport.setSerialPortParams(bauds, databytes, stop, parity);
	
	
				sport.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
				
				in = sport.getInputStream();
				out = sport.getOutputStream();
				SerialConnected = true;
				
				thread = new Thread(this);
				thread.setPriority(Thread.MAX_PRIORITY);
				thread.start();
				
			}
			
		}catch(Exception e){
			init = false;
			e.printStackTrace();
		}		
	}
	
	public boolean getStatus(){
		return init;
	}
	
	public void addSerialListener(SerialListener serial){
		if(listeners.contains(serial))
			return;
		listeners.add(serial);
	}
	
	private void handleListener(byte[] buffer){
		//System.out.println("recv");
		for(int i=0;i<listeners.size();i++)
			listeners.get(i).SerialCmdRecv(buffer);
	}
	private void handleListenerRaw(byte data){
		for(int i=0;i<listeners.size();i++)
			listeners.get(i).SerialRowRecv(data);
	}
	
	/*private void printSerialValues(byte[] buffer,int offset, int len){
		for(int i=offset;i<len;i++){
			System.out.print(Integer.toString(buffer[i])+" | ");
		}
		System.out.println(";");
	}	*/
	
	public void sendToEngine(int cmd_id,int[] vals){
		if(!SerialConnected || !init)
			return;
		
		byte[] buffer = new byte[1024];
		int len=0;
		try {
			buffer[0] = (byte) 255;
			buffer[1] = (byte) 255;
			buffer[2] = (byte) cmd_id;
			buffer[3] = (byte)(vals.length*2);
			int j=0;
			for(int i=0; i<vals.length;i++){
				buffer[j+4] = (byte) ((vals[i] >> 8) & 0xFF);
				buffer[j+5] = (byte) (vals[i] & 0xFF);
				j+=2;
			}
			len = 4+(vals.length*2);
			//printSerialValues(buffer,0, len);
			out.write(buffer,0,len);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		
		int data;
		while(true){
		try {
			Thread.sleep(1000);
			init =true;
			while((data = in.read()) > -1 && SerialConnected){
				handleListenerRaw((byte) data);
				switch(step){
					case STX1:
						if(data==255)
							step = STX2;
						break;
					case STX2:
						if(data==255){
							step = CTX;
							len = 0;
							index = 0;
							buffer = new byte[256];
							crc = 0;
						}else
							step = STX1;
						break;
					case CTX:
						buffer[index++] = (byte)data;
						//System.out.println(buffer[index-1] );
						crc ^= (byte)data;
						step = LTX;
						break;
					case LTX:
						buffer[index++] = (byte)data;
						crc ^= (byte)data;
						len = buffer[index-1];
						//System.out.println(len);
						if(buffer[index-1] <=0){
							step = STX1;
							return;
						}
						step = DTX;
						break;
					case DTX:
						buffer[index++] = (byte)data;
						crc ^= (byte)data;
						len--;
						if(len==0){
							step = CRC;
						}
						break;
					case CRC:
						if(crc != (byte)data ){
							System.out.println("ERROR CRC");
						}else{
							handleRecv(buffer);
						}
						step=STX1;
						break;
				}
			}
			//System.out.println("STOP");
		
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		
	}
	
	private void handleRecv(byte[] buffer){
		handleListener(buffer);
	}
	
}
