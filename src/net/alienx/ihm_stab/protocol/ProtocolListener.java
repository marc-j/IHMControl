package net.alienx.ihm_stab.protocol;

public interface ProtocolListener {

	public void protocolDataReceive(int cmd,ProtocolMessage msg);
}
