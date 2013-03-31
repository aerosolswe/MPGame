package com.theodore.mpgame.net.packets;

import com.theodore.mpgame.net.GameClient;
import com.theodore.mpgame.net.GameServer;

public class Packet02Move extends Packet {

	private String username;
	private float x, y, z;

	public Packet02Move(byte[] data) {
		super(02);
		String[] dataArray = readData(data).split(",");
		this.username = dataArray[0];
		this.x = Float.parseFloat(dataArray[1]);
		this.y = Float.parseFloat(dataArray[2]);
		this.z = Float.parseFloat(dataArray[3]);
	}

	public Packet02Move(String username, float x, float y, float z) {
		super(02);
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
		return ("02" + this.username + "," + this.x + "," + this.y + "," + this.z)
				.getBytes();
	}

	public String getUsername() {
		return username;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

}
