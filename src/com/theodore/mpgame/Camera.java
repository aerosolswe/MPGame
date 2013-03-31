package com.theodore.mpgame;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	private Vector3f position = new Vector3f();

	private Vector3f rotation = new Vector3f();

	private float fov;
	private float aspect;
	private float near;
	private float far;

	public float yaw = 0.0f;
	public float pitch = 0.0f;

	private float mx;
	private float my;

	private float maxLookUp = 85;
	private float maxLookDown = -85;

	public Camera(float fov, float aspect, float near, float far) {
		position.set(0, -2, -2);

		rotation.set(0, 0, 0);

		this.fov = fov;
		this.aspect = aspect;
		this.near = near;
		this.far = far;

		initProjection();
	}

	// Setups 3D view
	private void initProjection() {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(fov, aspect, near, far);
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_TEXTURE_2D);
	}

	// Tell lwjgl to use this view
	public void useView() {
		glRotatef(pitch, 1, 0, 0);
		glRotatef(yaw, 0, 1, 0);
		glRotatef(rotation.z, 0, 0, 1);

		glTranslatef(position.x, position.y, position.z);
	}

	// Look around with mouse
	public void movement() {
		mx = Mouse.getDX();
		my = Mouse.getDY();

		if (Display.isVisible() && Display.isActive()) {
			Mouse.setGrabbed(true);
			yaw(mx * 0.2f);
			pitch(my * 0.2f);
		} else {
			Mouse.setGrabbed(false);
		}
	}

	private void yaw(float amount) {
		yaw += amount;
		if (yaw > 360) {
			yaw = 0;
		}
		if (yaw < -360) {
			yaw = 0;
		}
	}

	private void pitch(float amount) {
		if (pitch >= maxLookDown && pitch <= maxLookUp) {
			pitch -= amount;
		} else if (pitch > maxLookUp) {
			pitch = maxLookUp;
		} else if (pitch < maxLookDown) {
			pitch = maxLookDown;
		}
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}
}
