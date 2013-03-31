package com.theodore.mpgame.model;

import org.lwjgl.opengl.GL11;

/**
 * A OBJ model implementation that renders the data as a display list.
 * 
 * @author Kevin Glass
 */
public class ObjModel {
	/** The identifier of the display list held for this model */
	private int listID;

	/**
	 * Create a new OBJ model that will render the object data specified in
	 * OpenGL
	 * 
	 * @param data
	 *            The data to be rendered for this model
	 */
	public ObjModel(ObjData data, boolean hasTexture) {
		if (hasTexture) {
			initModel(data);
		} else if (!hasTexture) {
			initModel(data, true);
		}
	}

	public void initModel(ObjData data) {
		// we're going to process the OBJ data and produce a display list
		// that will display the model. First we ask OpenGL to create
		// us a display list
		listID = GL11.glGenLists(1);

		// next we start producing the contents of the list
		GL11.glNewList(listID, GL11.GL_COMPILE);
		// cycle through all the faces in the model data
		// rendering a triangle for it
		GL11.glBegin(GL11.GL_TRIANGLES);
		int faceCount = data.getFaceCount();

		for (int i = 0; i < data.getFaceCount(); i++) {
			for (int v = 0; v < 3; v++) {
				// a position, normal and texture coordinate
				// for each vertex in the face
				Tuple3 vert = data.getFace(i).getVertex(v);
				Tuple3 norm = data.getFace(i).getNormal(v);
				Tuple2 tex = data.getFace(i).getTexCoord(v);

				GL11.glNormal3f(norm.getX(), norm.getY(), norm.getZ());
				GL11.glTexCoord2f(tex.getX(), tex.getY());
				GL11.glVertex3f(vert.getX(), vert.getY(), vert.getZ());
			}
		}

		GL11.glEnd();
		GL11.glEndList();
	}

	public void initModel(ObjData data, boolean t) {
		// we're going to process the OBJ data and produce a display list
		// that will display the model. First we ask OpenGL to create
		// us a display list
		listID = GL11.glGenLists(1);

		// next we start producing the contents of the list
		GL11.glNewList(listID, GL11.GL_COMPILE);
		// cycle through all the faces in the model data
		// rendering a triangle for it
		GL11.glBegin(GL11.GL_TRIANGLES);
		int faceCount = data.getFaceCount();

		for (int i = 0; i < data.getFaceCount(); i++) {
			for (int v = 0; v < 3; v++) {
				// a position, normal and texture coordinate
				// for each vertex in the face
				Tuple3 vert = data.getFace(i).getVertex(v);
				Tuple3 norm = data.getFace(i).getNormal(v);

				GL11.glNormal3f(norm.getX(), norm.getY(), norm.getZ());
				GL11.glVertex3f(vert.getX(), vert.getY(), vert.getZ());
			}
		}

		GL11.glEnd();
		GL11.glEndList();
	}

	/**
	 * Render the OBJ Model
	 */
	public void render() {
		// since we rendered our display list at construction we
		// can now just call this list causing it to be rendered
		// to the screen
		GL11.glCallList(listID);
	}
}
