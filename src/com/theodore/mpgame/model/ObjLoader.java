package com.theodore.mpgame.model;

import java.io.IOException;
import java.io.InputStream;

/**
 * This is a loader that will bring in the geometry from a Wavefront
 * .obj file. This loader won't work for any OBJ and is only really
 * intended to demonstrate how things might work. For reference it doesn't:
 * <p>
 * - Deal with anything but triangles<br/>
 * - Cope with material libraries (.mtl)<br/>
 * 
 * @author Kevin Glass
 */
public class ObjLoader {

	/**
	 * Load a Wavefront OBJ file from a file in the classpath
	 * 
	 * @param ref The reference to the file to read from the classpath
	 * @return The OBJ Model read from the file
	 * @throws IOException Indicates a failure to read the OBJ file from
	 * the specified file.
	 */
	public static ObjModel loadObj(String ref, boolean hasTexture) throws IOException {
		InputStream in = ObjLoader.class.getClassLoader().getResourceAsStream(ref);
	
		if (in == null) {
			throw new IOException("Unable to find: "+ref);
		}
		
		return new ObjModel(new ObjData(in), hasTexture);
	}
}
