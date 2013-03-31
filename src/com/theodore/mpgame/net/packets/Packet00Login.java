package com.theodore.mpgame.net.packets;

import com.theodore.mpgame.net.GameClient;
import com.theodore.mpgame.net.GameServer;

public class Packet00Login extends Packet {

	private String username;
	private float x, y, z;

	public Packet00Login(byte[] data) {
		super(00);
		String[] dataArray = readData(data).split(",");
		this.username = dataArray[0];
		this.x = Float.parseFloat(dataArray[1]);
		this.y = Float.parseFloat(dataArray[2]);
		this.z = Float.parseFloat(dataArray[3]);
	}

	public Packet00Login(String username, float x, float y, float z) {
		super(00);
		this.username = username;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void writeData(GameClient client) {
		client.sendData(getData());
	}

	@Override
	public void writeData(GameServer server) {
		server.sendDataToAllClients(getData());
	}

	@Override
	public byte[] getData() {
		return ("00" + this.username + "," + getX() + "," + getY() + "," + getZ())
				.getBytes();
	}

	public String getUsername() {
		return username;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

}
