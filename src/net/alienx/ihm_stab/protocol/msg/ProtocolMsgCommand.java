package net.alienx.ihm_stab.protocol.msg;

import net.alienx.ihm_stab.protocol.Protocol;
import net.alienx.ihm_stab.protocol.ProtocolMessage;

public class ProtocolMsgCommand extends ProtocolMessage {

	private int roll;
	private int pitch;
	private int yaw;
	private int throttle;
	private int flightMode;
	
	/**
	 * @return the roll
	 */
	public int getRoll() {
		return roll;
	}

	/**
	 * @param roll the roll to set
	 */
	public void setRoll(int roll) {
		this.roll = roll;
	}

	/**
	 * @return the pitch
	 */
	public int getPitch() {
		return pitch;
	}

	/**
	 * @param pitch the pitch to set
	 */
	public void setPitch(int pitch) {
		this.pitch = pitch;
	}

	/**
	 * @return the yaw
	 */
	public int getYaw() {
		return yaw;
	}

	/**
	 * @param yaw the yaw to set
	 */
	public void setYaw(int yaw) {
		this.yaw = yaw;
	}

	/**
	 * @return the throttle
	 */
	public int getThrottle() {
		return throttle;
	}

	/**
	 * @param throttle the throttle to set
	 */
	public void setThrottle(int throttle) {
		this.throttle = throttle;
	}
	
	/**
	 * @return the flightMode
	 */
	public int getFlightMode() {
		return flightMode;
	}

	/**
	 * @param flightMode the flightMode to set
	 */
	public void setFlightMode(int flightMode) {
		this.flightMode = flightMode;
	}
	
	@Override
	public void setDatas(byte[] datas) {
		// TODO Auto-generated method stub
	}

	@Override
	public int[] getDatas() {
		int[] datas = new int[5];
		datas[0] = roll;
		datas[1] = pitch;
		datas[2] = yaw;
		datas[3] = throttle;
		datas[4] = flightMode;
		return datas;
	}

	@Override
	public short getCmdId() {
		// TODO Auto-generated method stub
		return Protocol.PROTOCOL_CMD_COMMAND;
	}

}
