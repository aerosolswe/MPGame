package com.theodore.mpgame.entity.entities;

import com.theodore.mpgame.entity.Entity;

public class Floor extends Entity {
	
	// Floor class....

	public Floor() {
		isStatic = true;
		lighting = false;

		this.pos.set(-4, -10, -4);
		this.rot.set(0, 0, 0);
		this.size.set(1, 1, 1);

		width = 8.2f;
		length = 8.5f;
		height = 0.8f;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

}