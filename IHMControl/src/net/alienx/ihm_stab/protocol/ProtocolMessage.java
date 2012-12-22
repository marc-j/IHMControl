package net.alienx.ihm_stab.protocol;

abstract public class ProtocolMessage {
	abstract public void setDatas(byte[] datas);
	abstract public int[] getDatas();
	abstract public short getCmdId();
	
	public int toInt(byte[] data,int index){
		//int8 to int32
		int l;
		index = index*2;
		int sign = (data[index] & 0x80);
		if(sign > 0)
			l = sign << 31; //Signed
		else
			l = 0; //Not signed
		
		l += (data[index] & 0x7F) << 8;
		l += data[index+1] & 0xFF;
		if(sign > 0)
			l |= 0xFFFF8000;
		return l;
	}
	
	public float toDegrees(float radian){
		return 180f * radian / (float)Math.PI;
	}
}
