package com.theodore.mpgame.entity;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import com.theodore.mpgame.model.ObjModel;
import com.theodore.mpgame.model.Texture;

/*
 * Abstract class for all the objects 
 */

public abstract class Entity {

	protected Vector3f pos = new Vector3f();
	protected Vector3f size = new Vector3f();
	protected Vector3f rot = new Vector3f();

	protected float height, width, length;
	protected String username;

	protected boolean isStatic = false;
	public boolean grounded = false;
	public boolean jumping = false;
	protected boolean lighting = true;

	abstract public void update();

	// Render with texture (uv mapping)
	public void render(Texture texture, ObjModel model) {
		GL11.glPushMatrix();
		if (lighting) {
			GL11.glEnable(GL11.GL_LIGHTING);
		} else {
			GL11.glDisable(GL11.GL_LIGHTING);
		}

		GL11.glTranslatef(pos.x, pos.y, pos.z);
		GL11.glRotatef(rot.x, 1, 0, 0);
		GL11.glRotatef(rot.y, 0, 1, 0);
		GL11.glRotatef(rot.z, 0, 0, 1);
		GL11.glScalef(size.x, size.y, size.z);

		texture.bind();

		model.render();
		GL11.glPopMatrix();
	}

	// Render without texture
	public void render(ObjModel model) {
		GL11.glPushMatrix();
		if (lighting) {
			GL11.glEnable(GL11.GL_LIGHTING);
		} else {
			GL11.glDisable(GL11.GL_LIGHTING);
		}

		GL11.glTranslatef(pos.x, pos.y, pos.z);
		GL11.glRotatef(rot.x, 1, 0, 0);
		GL11.glRotatef(rot.y, 0, 1, 0);
		GL11.glRotatef(rot.z, 0, 0, 1);
		GL11.glScalef(size.x, size.y, size.z);

		model.render();
		GL11.glPopMatrix();
	}

	public Vector3f getPos() {
		return pos;
	}

	public void setPos(Vector3f pos) {
		this.pos = pos;
	}

	public Vector3f getSize() {
		return size;
	}

	public void setSize(Vector3f size) {
		this.size = size;
	}

	public Vector3f getRot() {
		return rot;
	}

	public void setRot(Vector3f rot) {
		this.rot = rot;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getLength() {
		return length;
	}

	public void setLength(float length) {
		this.length = length;
	}

}