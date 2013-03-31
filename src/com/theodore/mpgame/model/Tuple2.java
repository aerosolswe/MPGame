package com.theodore.mpgame.model;

/**
 * A simple tuple of 2 elements. A tuple is a set of values that relate
 * to each other in some way. In this case its a set of 2 so it might
 * represent a vertex or normal in 2D space or a texture coordinate
 * 
 * @author Kevin Glass
 */
class Tuple2 {
	/** The x element in this tuple */
	private float x;
	/** The y element in this tuple */
	private float y;
	
	/**
	 * Create a new Tuple of 2 elements
	 * 
	 * @param x The X element value
	 * @param y The Y element value
	 */
	public Tuple2(float x,float y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Get the X element value from this tuple
	 * 
	 * @return The X element value from this tuple
	 */
	public float getX() {
		return x;
	}

	/**
	 * Get the Y element value from this tuple
	 * 
	 * @return The Y element value from this tuple
	 */
	public float getY() {
		return y;
	}
	
}