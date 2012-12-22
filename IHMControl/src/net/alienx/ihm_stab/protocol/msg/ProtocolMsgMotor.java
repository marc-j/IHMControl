package net.alienx.ihm_stab.protocol.msg;

import net.alienx.ihm_stab.protocol.Protocol;
import net.alienx.ihm_stab.protocol.ProtocolMessage;

public class ProtocolMsgMotor extends ProtocolMessage {

	private int MotorFrontLeft;
	private int MotorFrontRight;
	private int MotorRearLeft;
	private int MotorRearRight;
	
	public int getMotorFrontLeft() {
		return MotorFrontLeft;
	}

	public void setMotorFrontLeft(int motorFrontLeft) {
		MotorFrontLeft = motorFrontLeft;
	}

	public int getMotorFrontRight() {
		return MotorFrontRight;
	}

	public void setMotorFrontRight(int motorFrontRight) {
		MotorFrontRight = motorFrontRight;
	}

	public int getMotorRearLeft() {
		return MotorRearLeft;
	}

	public void setMotorRearLeft(int motorRearLeft) {
		MotorRearLeft = motorRearLeft;
	}

	public int getMotorRearRight() {
		return MotorRearRight;
	}

	public void setMotorRearRight(int motorRearRight) {
		MotorRearRight = motorRearRight;
	}


	@Override
	public void setDatas(byte[] datas) {
		this.setMotorFrontLeft(toInt(datas, 1));
		this.setMotorFrontRight(toInt(datas, 2));
		this.setMotorRearLeft(toInt(datas,3));
		this.setMotorRearRight(toInt(datas, 4));
	}

	@Override
	public int[] getDatas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public short getCmdId() {
		// TODO Auto-generated method stub
		return Protocol.PROTOCOL_CMD_MOTOR;
	}

}
