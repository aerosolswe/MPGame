package com.theodore.mpgame.net;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import com.theodore.mpgame.GameWindow;
import com.theodore.mpgame.entity.entities.PlayerMP;
import com.theodore.mpgame.net.packets.Packet.PacketTypes;
import com.theodore.mpgame.net.packets.*;

/* Server class, I wont explain the multiplayer part
 * I basicly went through a tutorial for networking in a 2D
 * game and ported it to lwjgl, here is a link to the youtube Channel: https://www.youtube.com/user/DesignsbyZephyr
 */

public class GameServer extends Thread {

	private DatagramSocket socket;
	private GameWindow game;
	private List<PlayerMP> connectedPlayers = new ArrayList<PlayerMP>();

	public GameServer(GameWindow game) {
		this.game = game;
		try {
			this.socket = new DatagramSocket(1332);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			byte[] data = new byte[16384];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}

			parsePacket(packet.getData(), packet.getAddress(), packet.getPort());

			/*
			 * String message = new String(packet.getData());
			 * System.out.println("SERVER [" +
			 * packet.getAddress().getHostAddress() + ":" + packet.getPort() +
			 * "] > " + message); if (message.trim().equalsIgnoreCase("ping")) {
			 * sendData("pong".getBytes(), packet.getAddress(),
			 * packet.getPort()); }
			 */
		}
	}

	private void parsePacket(byte[] data, InetAddress address, int port) {
		String message = new String(data).trim();
		PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
		Packet packet;
		switch (type) {
		default:
		case INVALID:
			break;
		case LOGIN:
			packet = new Packet00Login(data);

			System.out.println("[" + address.getHostAddress() + ":" + port
					+ "] " + ((Packet00Login) packet).getUsername()
					+ " has connected.");

			PlayerMP player = new PlayerMP(address, port,
					((Packet00Login) packet).getUsername(), 0, 0, 0);
			this.addConnection(player, ((Packet00Login) packet));
			break;
		case DISCONNECT:
			packet = new Packet01Disconnect(data);

			System.out.println("[" + address.getHostAddress() + ":" + port
					+ "] " + ((Packet01Disconnect) packet).getUsername()
					+ " has disconnected.. ");

			this.removeConnection(((Packet01Disconnect) packet));
			break;
		case MOVE:
			packet = new Packet02Move(data);
			/*
			 * System.out.println(((Packet02Move) packet).getUsername() +
			 * " has moved to \n x: " + ((Packet02Move) packet).getX() +
			 * "\n y: " + ((Packet02Move) packet).getY() + "\n z: " +
			 * ((Packet02Move) packet).getZ());
			 */
			this.handleMove(((Packet02Move) packet));
			break;
		}
	}

	public void addConnection(PlayerMP player, Packet00Login packet) {
		boolean alreadyConnected = false;

		for (PlayerMP p : this.connectedPlayers) {
			if (player.getUsername().equalsIgnoreCase(p.getUsername())) {
				if (p.ipAddress == null) {
					p.ipAddress = player.ipAddress;
				}

				if (p.port == -1) {
					p.port = player.port;
				}

				alreadyConnected = true;
			} else {
				sendData(packet.getData(), p.ipAddress, p.port);
				packet = new Packet00Login(p.getUsername(), p.getPos().x,
						p.getPos().y, p.getPos().z);
				sendData(packet.getData(), player.ipAddress, player.port);
			}
		}

		if (!alreadyConnected) {
			this.connectedPlayers.add(player);
		}
	}

	public void removeConnection(Packet01Disconnect packet) {
		this.connectedPlayers.remove(getPlayerMPIndex(packet.getUsername()));
		packet.writeData(this);
	}

	private void handleMove(Packet02Move packet) {
		if (getPlayerMP(packet.getUsername()) != null) {
			int index = getPlayerMPIndex(packet.getUsername());
			this.connectedPlayers.get(index).setPos(
					new Vector3f(packet.getX(), packet.getY(), packet.getZ()));
			packet.writeData(this);
		}
	}

	public PlayerMP getPlayerMP(String username) {
		for (PlayerMP player : this.connectedPlayers) {
			if (player.getUsername().equals(username)) {
				return player;
			}
		}

		return null;
	}

	public int getPlayerMPIndex(String username) {
		int index = 0;
		for (PlayerMP player : this.connectedPlayers) {
			if (player.getUsername().equals(username)) {
				break;
			}
			index++;
		}

		return index;
	}

	public void sendData(byte[] data, InetAddress ipAdress, int port) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAdress,
				port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendDataToAllClients(byte[] data) {
		for (PlayerMP p : connectedPlayers) {
			sendData(data, p.ipAddress, p.port);
		}
	}
}
