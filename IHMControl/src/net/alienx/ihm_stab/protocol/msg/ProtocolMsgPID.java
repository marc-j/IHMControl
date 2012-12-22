package net.alienx.ihm_stab.protocol.msg;

import net.alienx.ihm_stab.protocol.Protocol;
import net.alienx.ihm_stab.protocol.ProtocolMessage;

public class ProtocolMsgPID extends ProtocolMessage {
	private float RollKP;
	private float RollKI;
	private float RollKD;
	private float PitchKP;
	private float PitchKI;
	private float PitchKD;
	private float YawKP;
	private float YawKI;
	private float YawKD;
	
	/**
	 * @return the rollKP
	 */
	public float getRollKP() {
		return RollKP;
	}

	/**
	 * @param rollKP the rollKP to set
	 */
	public void setRollKP(float rollKP) {
		RollKP = rollKP;
	}

	/**
	 * @return the rollKI
	 */
	public float getRollKI() {
		return RollKI;
	}

	/**
	 * @param rollKI the rollKI to set
	 */
	public void setRollKI(float rollKI) {
		RollKI = rollKI;
	}

	/**
	 * @return the rollKD
	 */
	public float getRollKD() {
		return RollKD;
	}

	/**
	 * @param rollKD the rollKD to set
	 */
	public void setRollKD(float rollKD) {
		RollKD = rollKD;
	}

	/**
	 * @return the pitchKP
	 */
	public float getPitchKP() {
		return PitchKP;
	}

	/**
	 * @param pitchKP the pitchKP to set
	 */
	public void setPitchKP(float pitchKP) {
		PitchKP = pitchKP;
	}

	/**
	 * @return the pitchKI
	 */
	public float getPitchKI() {
		return PitchKI;
	}

	/**
	 * @param pitchKI the pitchKI to set
	 */
	public void setPitchKI(float pitchKI) {
		PitchKI = pitchKI;
	}

	/**
	 * @return the pitchKD
	 */
	public float getPitchKD() {
		return PitchKD;
	}

	/**
	 * @param pitchKD the pitchKD to set
	 */
	public void setPitchKD(float pitchKD) {
		PitchKD = pitchKD;
	}

	/**
	 * @return the yawKP
	 */
	public float getYawKP() {
		return YawKP;
	}

	/**
	 * @param yawKP the yawKP to set
	 */
	public void setYawKP(float yawKP) {
		YawKP = yawKP;
	}

	/**
	 * @return the yawKI
	 */
	public float getYawKI() {
		return YawKI;
	}

	/**
	 * @param yawKI the yawKI to set
	 */
	public void setYawKI(float yawKI) {
		YawKI = yawKI;
	}

	/**
	 * @return the yawKD
	 */
	public float getYawKD() {
		return YawKD;
	}

	/**
	 * @param yawKD the yawKD to set
	 */
	public void setYawKD(float yawKD) {
		YawKD = yawKD;
	}

	
	
	@Override
	public void setDatas(byte[] datas) {
		System.out.println("DGET: "+toInt(datas,1));
		this.setRollKP(toInt(datas,1)/1e3f);
		this.setRollKI(toInt(datas,2)/1e3f);
		this.setRollKD(toInt(datas,3)/1e3f);
		this.setPitchKP(toInt(datas,4)/1e3f);
		this.setPitchKI(toInt(datas,5)/1e3f);
		this.setPitchKD(toInt(datas,6)/1e3f);
		this.setYawKP(toInt(datas,7)/1e3f);
		this.setYawKI(toInt(datas,8)/1e3f);
		this.setYawKD(toInt(datas,9)/1e3f);
	}

	@Override
	public int[] getDatas() {
		int[] datas = new int[9];
		datas[0] = (int)(RollKP*1e3f);
		datas[1] = (int)(RollKI*1e3f);
		datas[2] = (int)(RollKD*1e3f);
		datas[3] = (int)(PitchKP*1e3f);
		datas[4] = (int)(PitchKI*1e3f);
		datas[5] = (int)(PitchKD*1e3f);
		datas[6] = (int)(YawKP*1e3f);
		datas[7] = (int)(YawKI*1e3f);
		datas[8] = (int)(YawKD*1e3f);
		System.out.println("RGet: "+(int)(RollKP*1e3f));
		return datas;
	}

	@Override
	public short getCmdId() {
		// TODO Auto-generated method stub
		return Protocol.PROTOCOL_CMD_PID;
	}

}
