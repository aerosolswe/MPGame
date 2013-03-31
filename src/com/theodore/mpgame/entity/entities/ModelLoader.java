package com.theodore.mpgame.entity.entities;

import java.io.IOException;

import com.theodore.mpgame.model.ObjLoader;
import com.theodore.mpgame.model.ObjModel;
import com.theodore.mpgame.model.Texture;
import com.theodore.mpgame.model.TextureLoader;

/*
 * This class load all the models at startup so we can use them later
 */

public class ModelLoader {

	public Texture playerTexture;
	public static ObjModel playerModel;

	public Texture floorTexture;
	public static ObjModel floorModel;

	public ModelLoader() {

	}

	public void loadModels() {
		TextureLoader loader = new TextureLoader();
		try {
			playerTexture = loader.getTexture("res/character/box.png");
			playerModel = ObjLoader.loadObj("res/character/box.obj", true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			floorTexture = loader.getTexture("res/level/floor.png");
			floorModel = ObjLoader.loadObj("res/level/floor.obj", true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
