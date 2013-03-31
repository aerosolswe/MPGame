package com.theodore.mpgame.model;

/**
 * A single face defined on a model. This is a sreies of points
 * each with a position, normal and texture coordinate
 * 
 * @author Kevin Glass
 */
class Face {
	/** The vertices making up this face */
	private Tuple3[] verts;
	/** The normals making up this face */
	private Tuple3[] norms;
	/** The texture coordinates making up this face */
	private Tuple2[] texs;
	/** The number of points */
	private int points;
	
	/**
	 * Create a new face
	 * 
	 * @param points The number of points building up this face
	 */
	public Face(int points) {
		verts = new Tuple3[points];
		norms = new Tuple3[points];
		texs = new Tuple2[points];
	}
	
	/**
	 * Add a single point to this face
	 * 
	 * @param vert The vertex location information for the point
	 * @param tex The texture coordinate information for the point
	 * @param norm the normal information for the point
	 */
	public void addPoint(Tuple3 vert, Tuple2 tex, Tuple3 norm) {
		verts[points] = vert;
		texs[points] = tex;
		norms[points] = norm;
		
		points++;
	}
	
	/**
	 * Get the vertex information for a specified point within this face.
	 * 
	 * @param p The index of the vertex information to retrieve
	 * @return The vertex information from this face
	 */
	public Tuple3 getVertex(int p) {
		return verts[p];
	}

	/**
	 * Get the texture information for a specified point within this face.
	 * 
	 * @param p The index of the texture information to retrieve
	 * @return The texture information from this face
	 */
	public Tuple2 getTexCoord(int p) {
		return texs[p];
	}

	/**
	 * Get the normal information for a specified point within this face.
	 * 
	 * @param p The index of the normal information to retrieve
	 * @return The normal information from this face
	 */
	public Tuple3 getNormal(int p) {
		return norms[p];
	}
}