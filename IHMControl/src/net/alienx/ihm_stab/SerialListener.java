package net.alienx.ihm_stab;

public interface SerialListener {

	public void SerialCmdRecv(byte[] datas);
	public void SerialRowRecv(byte data);
	
}
