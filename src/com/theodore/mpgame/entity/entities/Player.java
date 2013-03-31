package com.theodore.mpgame.entity.entities;

import org.lwjgl.input.Keyboard;

import com.theodore.mpgame.GameWindow;
import com.theodore.mpgame.entity.Entity;
import com.theodore.mpgame.net.packets.Packet02Move;

public class Player extends Entity {

	public Player(String username) {

		this.pos.set(0, 0, 0);
		this.size.set(1f, 1f, 1f);
		this.rot.set(0, 0, 0);

		this.username = username;
		width = 1.5f;
		height = 3;
	}

	public Player() {
		this.pos.set(0, 0, 0);
		this.size.set(1f, 1f, 1f);
		width = 0.5f;
		length = 0.5f;
		height = 1.2f;
	}

	// Update player position over network (Should only update when the player moves..)
	@Override
	public void update() {

		Packet02Move packet = new Packet02Move(username, pos.x, pos.y, pos.z);
		packet.writeData(GameWindow.gameWindow.client);
	}

	// Movement
	
	int u = 0;
	public void movement(float yaw) {

		boolean keyUp = Keyboard.isKeyDown(Keyboard.KEY_W)
				| Keyboard.isKeyDown(Keyboard.KEY_UP);
		boolean keyDown = Keyboard.isKeyDown(Keyboard.KEY_S)
				| Keyboard.isKeyDown(Keyboard.KEY_DOWN);
		boolean keyLeft = Keyboard.isKeyDown(Keyboard.KEY_A)
				| Keyboard.isKeyDown(Keyboard.KEY_LEFT);
		boolean keyRight = Keyboard.isKeyDown(Keyboard.KEY_D)
				| Keyboard.isKeyDown(Keyboard.KEY_RIGHT);

		boolean jump = Keyboard.isKeyDown(Keyboard.KEY_SPACE);

		boolean flyUp = Keyboard.isKeyDown(Keyboard.KEY_NUMPAD8);
		boolean flyDown = Keyboard.isKeyDown(Keyboard.KEY_NUMPAD5);

		if (keyUp) {
			walkForward(0.05f, yaw);
		}
		if (keyDown) {
			walkBackwards(0.01f, yaw);
		}
		if (keyLeft) {
			strafeLeft(0.05f, yaw);
		}
		if (keyRight) {
			strafeRight(0.05f, yaw);
		}
		if (jump) {
			jump();
		}
		if (flyUp && !flyDown) {
			flyUp(0.1f);
		}
		if (flyDown && !flyUp) {
			flyDown(0.1f);
		}

		if (jumping) {
			pos.y += 0.05f;
			if (u >= 1000) {
				jumping = false;
				u = 0;
			}
			u += 50;
		}
	}

	private void jump() {
		if (grounded) {
			jumping = true;
		}
	}

	private void walkForward(float distance, float yaw) {
		pos.x += distance * (float) Math.sin(Math.toRadians(yaw));
		pos.z -= distance * (float) Math.cos(Math.toRadians(yaw));
	}

	// moves the camera backward relative to its current rotation (yaw)
	private void walkBackwards(float distance, float yaw) {
		pos.x -= distance * (float) Math.sin(Math.toRadians(yaw));
		pos.z += distance * (float) Math.cos(Math.toRadians(yaw));
	}

	// strafes the camera left relitive to its current rotation (yaw)
	private void strafeLeft(float distance, float yaw) {
		pos.x += distance * (float) Math.sin(Math.toRadians(yaw - 90));
		pos.z -= distance * (float) Math.cos(Math.toRadians(yaw - 90));
	}

	// strafes the camera right relitive to its current rotation (yaw)
	private void strafeRight(float distance, float yaw) {
		pos.x += distance * (float) Math.sin(Math.toRadians(yaw + 90));
		pos.z -= distance * (float) Math.cos(Math.toRadians(yaw + 90));
	}

	private void flyUp(float distance) {
		float newPosition = distance;
		pos.y += newPosition;
	}

	public void flyDown(float distance) {
		float newPosition = distance;
		pos.y -= newPosition;
	}

	public String getUsername() {
		return this.username;
	}

}
