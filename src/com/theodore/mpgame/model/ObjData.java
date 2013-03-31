package com.theodore.mpgame.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;


/**
 * The data that has been read from the Wavefront .obj file. This is
 * kept seperate from the actual rendering with the hope the data
 * might be used for some other rendering engine in the future.
 * 
 * @author Kevin Glass
 */
public class ObjData {
	/** The verticies that have been read from the file */
	private ArrayList verts = new ArrayList();
	/** The normals that have been read from the file */
	private ArrayList normals = new ArrayList();
	/** The texture coordinates that have been read from the file */
	private ArrayList texCoords = new ArrayList();
	/** The faces data read from the file */
	private ArrayList faces = new ArrayList();
	
	/**
	 * Create a new set of OBJ data by reading it in from the specified
	 * input stream.
	 * 
	 * @param in The input stream from which to read the OBJ data
	 * @throws IOException Indicates a failure to read from the stream
	 */
	public ObjData(InputStream in) throws IOException {
		// read the file line by line adding the data to the appropriate
		// list held locally
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		
		while (reader.ready()) {
			String line = reader.readLine();
			
			// if we read a null line thats means on some systems
			// we've reached the end of the file, hence we want to 
			// to jump out of the loop
			if (line == null) {
				break;
			}
			
			// "vn" indicates normal data
			if (line.startsWith("vn")) {
				Tuple3 normal = readTuple3(line);
				normals.add(normal);
			// "vt" indicates texture coordinate data
			} else if (line.startsWith("vt")) {
				Tuple2 tex = readTuple2(line);
				texCoords.add(tex);
			// "v" indicates vertex data
			} else if (line.startsWith("v")) {
				Tuple3 vert = readTuple3(line);
				verts.add(vert);
			// "f" indicates a face
			} else if (line.startsWith("f")) {
				Face face = readFace(line);
				faces.add(face);
			}
		}
		
		// Print some diagnositics data so we can see whats happening
		// while testing
		System.out.println("Read " + verts.size() + " verticies");
		System.out.println("Read " + faces.size() + " faces");
	}
	
	/**
	 * Get the number of faces found in the model file
	 * 
	 * @return The number of faces found in the model file
	 */
	public int getFaceCount() {
		return faces.size();
	}
	
	/**
	 * Get the data for specific face
	 * 
	 * @param index The index of the face whose data should be retrieved
	 * @return The face data requested
 	 */
	public Face getFace(int index) {
		return (Face) faces.get(index);
	}
	
	/**
	 * Read a set of 3 float values from a line assuming the first token
	 * on the line is the identifier
	 * 
	 * @param line The line from which to read the 3 values
	 * @return The set of 3 floating point values read
	 * @throws IOException Indicates a failure to process the line
	 */
	private Tuple3 readTuple3(String line) throws IOException {
		StringTokenizer tokens = new StringTokenizer(line, " ");
		
		tokens.nextToken();
		
		try {
			float x = Float.parseFloat(tokens.nextToken());
			float y = Float.parseFloat(tokens.nextToken());
			float z = Float.parseFloat(tokens.nextToken());
			
			return new Tuple3(x,y,z);
		} catch (NumberFormatException e) {
			throw new IOException(e.getMessage());
		}
	}
	
	/**
	 * Read a set of 2 float values from a line assuming the first token
	 * on the line is the identifier
	 * 
	 * @param line The line from which to read the 3 values
	 * @return The set of 2 floating point values read
	 * @throws IOException Indicates a failure to process the line
	 */
	private Tuple2 readTuple2(String line) throws IOException {
		StringTokenizer tokens = new StringTokenizer(line, " ");
		
		tokens.nextToken();
		
		try {
			float x = Float.parseFloat(tokens.nextToken());
			float y = Float.parseFloat(tokens.nextToken());
			
			return new Tuple2(x,y);
		} catch (NumberFormatException e) {
			throw new IOException(e.getMessage());
		}
	}
	
	/**
	 * Read a set of face data from the line
	 * 
	 * @param line The line which to interpret as face data
	 * @return The face data extracted from the line
	 * @throws IOException Indicates a failure to process the line
	 */
	private Face readFace(String line) throws IOException {
		StringTokenizer points = new StringTokenizer(line, " ");
		
		points.nextToken();
		int faceCount = points.countTokens();
		
		// currently we only support triangels so anything other than
		// 3 verticies is invalid
		if (faceCount != 3) {
			throw new RuntimeException("Only triangles are supported");
		}
		
		// create a new face data to populate with the values from the line
		Face face = new Face(faceCount);
		
		try {
			// for each line we're going to read 3 bits of data, the index
			// of the vertex, the index of the texture coordinate and the
			// normal.
			for (int i=0;i<faceCount;i++) {
				StringTokenizer parts = new StringTokenizer(points.nextToken(), "/");
				
				int v = Integer.parseInt(parts.nextToken());
				int t = Integer.parseInt(parts.nextToken());
				int n = Integer.parseInt(parts.nextToken());
				
				// we have the indicies we can now just add the point
				// data to the face.
				face.addPoint((Tuple3) verts.get(v-1),
						      (Tuple2) texCoords.get(t-1),
						      (Tuple3) normals.get(n-1));
			}
		} catch (NumberFormatException e) {
			throw new IOException(e.getMessage());
		}
		
		return face;
	}
}
