package com.theodore.mpgame.entity.entities;

import java.net.*;


public class PlayerMP extends Player {
	
	// Players network class

	public InetAddress ipAddress;
	public int port;

	public PlayerMP(InetAddress ipAddress, int port, String username, float x, float y, float z) {
		this.ipAddress = ipAddress;
		this.port = port;
		this.username = username;
		this.pos.set(x, y, z);
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		super.update();

	}

}
