package com.theodore.mpgame.net;

import java.io.IOException;
import java.net.*;

import com.theodore.mpgame.GameWindow;
import com.theodore.mpgame.entity.entities.PlayerMP;
import com.theodore.mpgame.net.packets.Packet;
import com.theodore.mpgame.net.packets.Packet00Login;
import com.theodore.mpgame.net.packets.Packet01Disconnect;
import com.theodore.mpgame.net.packets.Packet.PacketTypes;
import com.theodore.mpgame.net.packets.Packet02Move;

/* Client class, I wont explain the multiplayer part
 * I basicly went through a tutorial for networking in a 2D
 * game and ported it to lwjgl, here is a link to the youtube Channel: https://www.youtube.com/user/DesignsbyZephyr
 */

public class GameClient extends Thread {

	private InetAddress ipAdress;
	private DatagramSocket socket;
	private GameWindow game;

	public GameClient(GameWindow game, String ipAdress) {
		this.game = game;
		try {
			this.socket = new DatagramSocket();
			this.ipAdress = InetAddress.getByName(ipAdress);
		} catch (UnknownHostException e) {
			e.printStackTrace();
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
			 * System.out.println("SERVER > " + message);
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
			handleLogin((Packet00Login) packet, address, port);
			break;
		case DISCONNECT:
			packet = new Packet01Disconnect(data);

			System.out.println("[" + address.getHostAddress() + ":" + port
					+ "] " + ((Packet01Disconnect) packet).getUsername()
					+ " has left the server.. ");

			game.removePlayerMP(((Packet01Disconnect) packet).getUsername());
			break;
		case MOVE:
			packet = new Packet02Move(data);
			handleMove((Packet02Move) packet);
			break;
		}
	}

	public void sendData(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAdress,
				1332);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handleLogin(Packet00Login packet, InetAddress address, int port) {
		System.out.println("[" + address.getHostAddress() + ":" + port + "] "
				+ packet.getUsername() + " has joined the game.");
		PlayerMP player = new PlayerMP(address, port, packet.getUsername(),
				packet.getX(), packet.getY(), packet.getZ());
		game.getPlayers().add(player);
	}

	private void handleMove(Packet02Move packet) {
		this.game.movePlayer(packet.getUsername(), packet.getX(),
				packet.getY(), packet.getZ());
	}
}
