package net.alienx.ihm_stab.protocol.msg;

import net.alienx.ihm_stab.Utils;
import net.alienx.ihm_stab.protocol.Protocol;
import net.alienx.ihm_stab.protocol.ProtocolMessage;

public class ProtocolMsgSystem extends ProtocolMessage {

	private float cpuLoad;
	private int flightMode;
	private float batteryVoltage;
	private float mainLoopTime;
	
	/**
	 * @return the cpuLoad
	 */
	public float getCpuLoad() {
		return cpuLoad;
	}

	/**
	 * @param cpuLoad the cpuLoad to set
	 */
	public void setCpuLoad(float cpuLoad) {
		this.cpuLoad = cpuLoad;
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

	/**
	 * @return the batteryVoltage
	 */
	public float getBatteryVoltage() {
		return batteryVoltage;
	}

	/**
	 * @param batteryVoltage the batteryVoltage to set
	 */
	public void setBatteryVoltage(float batteryVoltage) {
		this.batteryVoltage = batteryVoltage;
	}

	/**
	 * @return the mainLoopTime
	 */
	public float getMainLoopTime() {
		return mainLoopTime;
	}

	/**
	 * @param mainLoopTime the mainLoopTime to set
	 */
	public void setMainLoopTime(float mainLoopTime) {
		this.mainLoopTime = mainLoopTime;
	}
	
	@Override
	public void setDatas(byte[] datas) {
		this.setCpuLoad(toInt(datas,1)/1.0e3f);
		this.setFlightMode(toInt(datas,2));
		this.setBatteryVoltage(toInt(datas,3));///1.0e3f);
		this.setMainLoopTime(toInt(datas,4)/1.0e3f);

	}

	@Override
	public int[] getDatas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public short getCmdId() {
		return Protocol.PROTOCOL_CMD_SYSTEM;
	}

}
