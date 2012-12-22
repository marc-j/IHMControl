package net.alienx.ihm_stab.protocol.msg;

import net.alienx.ihm_stab.Utils;
import net.alienx.ihm_stab.protocol.Protocol;
import net.alienx.ihm_stab.protocol.ProtocolMessage;

public class ProtocolMsgCapteurs extends ProtocolMessage {
	private float accX;
	private float accY;
	private float accZ;
	private float gyroX;
	private float gyroY;
	private float gyroZ;
	private float roll;
	private float pitch;
	private float yaw;
	
	/**
	 * @return the accX
	 */
	public float getAccX() {
		return accX;
	}
	/**
	 * @param accX the accX to set
	 */
	public void setAccX(float accX) {
		this.accX = accX;
	}
	/**
	 * @return the accY
	 */
	public float getAccY() {
		return accY;
	}
	/**
	 * @param accY the accY to set
	 */
	public void setAccY(float accY) {
		this.accY = accY;
	}
	/**
	 * @return the accZ
	 */
	public float getAccZ() {
		return accZ;
	}
	/**
	 * @param accZ the accZ to set
	 */
	public void setAccZ(float accZ) {
		this.accZ = accZ;
	}
	/**
	 * @return the gyroX
	 */
	public float getGyroX() {
		return gyroX;
	}
	/**
	 * @param gyroX the gyroX to set
	 */
	public void setGyroX(float gyroX) {
		this.gyroX = gyroX;
	}
	/**
	 * @return the gyroY
	 */
	public float getGyroY() {
		return gyroY;
	}
	/**
	 * @param gyroY the gyroY to set
	 */
	public void setGyroY(float gyroY) {
		this.gyroY = gyroY;
	}
	/**
	 * @return the gyroZ
	 */
	public float getGyroZ() {
		return gyroZ;
	}
	/**
	 * @param gyroZ the gyroZ to set
	 */
	public void setGyroZ(float gyroZ) {
		this.gyroZ = gyroZ;
	}
	/**
	 * @return the roll
	 */
	public float getRoll() {
		return roll;
	}
	/**
	 * @param roll the roll to set
	 */
	public void setRoll(float roll) {
		this.roll = roll;
	}
	/**
	 * @return the pitch
	 */
	public float getPitch() {
		return pitch;
	}
	/**
	 * @param pitch the pitch to set
	 */
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
	/**
	 * @return the yaw
	 */
	public float getYaw() {
		return yaw;
	}
	/**
	 * @param yaw the yaw to set
	 */
	public void setYaw(float yaw) {
		this.yaw = yaw;
	}
	
	@Override
	public void setDatas(byte[] datas) {
		this.setAccX(toInt(datas, 1)/10);
		this.setAccY(toInt(datas, 2)/10);
		this.setAccZ(toInt(datas,3)/10);
		this.setGyroX(toInt(datas, 4)/10);
		this.setGyroY(toInt(datas, 5)/10);
		this.setGyroZ(toInt(datas, 6)/10);
		this.setRoll(toInt(datas, 7)/10);
		this.setPitch(toInt(datas, 8)/10);
		this.setYaw(toInt(datas, 9)/10);	
	}
	@Override
	public int[] getDatas() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public short getCmdId() {
		return Protocol.PROTOCOL_CMD_CAPTEURS;
	}
}
