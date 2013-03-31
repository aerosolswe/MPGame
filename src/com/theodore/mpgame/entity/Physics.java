package com.theodore.mpgame.entity;

import com.theodore.mpgame.GameWindow;
import com.theodore.mpgame.entity.entities.*;

public class Physics {

	private Player player;
	private Floor floor;

	private float gravity = 0.03f;

	public Physics() {

	}

	// Update physics
	public void updatePhysics() {
		for (int i = 0; i < GameWindow.players.size(); i++) {
			player = GameWindow.players.get(i);
			if (!player.isStatic) {
				if (!player.grounded) {
					player.pos.y -= gravity;
				} else if (player.grounded) {

				}
			}
			for (int j = 0; i < GameWindow.floors.size(); i++) {
				floor = GameWindow.floors.get(j);

				if (collision(player, floor)) {
					player.grounded = true;
				} else {
					player.grounded = false;
				}
			}

		}
	}

	// Collision methods
	public static boolean collision(Entity e, Entity e1) {
		// all sides collision
		// Make sure width,height & length is accurate
		if ((e.getPos().y < e1.getPos().y + e1.height)
				&& (e.getPos().y + e.height > e1.getPos().y)
				&& (e.getPos().x < e1.getPos().x + e1.width)
				&& (e.getPos().x + e.width > e1.getPos().x)
				&& (e.getPos().z < e1.getPos().z + e1.length)
				&& (e.getPos().z + e.length > e1.getPos().z)) {
			return true;
		}

		return false;
	}

	public static boolean collisionSide(Entity e, Entity e1, String side) {
		// will return true if EntityOne collides with selected side of
		// EntityTwo
		if (side == "top") {
			if (e.getPos().y < e1.getPos().y + e1.height) {
				return true;
			}
		} else if (side == "bottom") {
			if (e.getPos().y + e.height > e1.getPos().y) {
				return true;
			}
		} else if (side == "right") {
			if (e.getPos().x < e1.getPos().x + e1.width) {
				return true;
			}
		} else if (side == "left") {
			if (e.getPos().x + e.width > e1.getPos().x) {
				return true;
			}
		} else if (side == "front") {
			if (e.getPos().z < e1.getPos().z + e1.length) {
				return true;
			}
		} else if (side == "behind") {
			if (e.getPos().z + e.length > e1.getPos().z) {
				return true;
			}
		}
		return false;
	}

}
