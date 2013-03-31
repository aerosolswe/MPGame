package com.theodore.mpgame;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import org.lwjgl.*;
import org.lwjgl.input.*;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector3f;

import com.theodore.mpgame.entity.Physics;
import com.theodore.mpgame.entity.entities.*;
import com.theodore.mpgame.net.*;
import com.theodore.mpgame.net.packets.*;

public class GameWindow {

	public static GameWindow gameWindow;

	private float[] lightPosition = { 0f, 1000f, 100, 1f };

	private Camera camera;

	public Player player;
	public Floor floor;

	public static ArrayList<Player> players = new ArrayList<Player>();
	public static ArrayList<Floor> floors = new ArrayList<Floor>();

	private boolean isHost;

	private String username;
	private String ipAddress;

	public int connectedPlayers = 0;

	public GameServer server;
	public GameClient client;
	private ModelLoader modelLoader;

	private int width;
	private int height;

	private Physics physics;

	// Initiating gamewindow
	public GameWindow(int width, int height, boolean isHost, String username,
			String ipAddress) {
		this.width = width;
		this.height = height;
		this.isHost = isHost;
		this.username = username;
		this.ipAddress = ipAddress;
		System.out.println("Creating display.. ");
		// Create display
		createDisplay();

		System.out.println("Creating lighting.. ");
		// Create lighting
		createLighting();

		System.out.println("Initializing network.. ");
		// Initialize the network
		initMP();

		System.out.println("Initializing game.. ");
		// Initialize the game
		initGame();

		System.out.println("Looping thru gameloop.. ");
		// GameLoop
		gameLoop();

		System.out.println("cleaning up.. ");
		// Shut down & clean up
		shutDown();
	}

	// Initiating the network
	public void initMP() {
		if (isHost) {
			server = new GameServer(this);
			server.start();
			System.out.println("Hosting");
		}

		client = new GameClient(this, ipAddress);

		client.start();
	}

	// Initiating the game
	public void initGame() {
		gameWindow = this;

		// fov, aspect ratio, near, far
		camera = new Camera(50, (float) Display.getWidth()
				/ (float) Display.getHeight(), 0.3f, 1000);
		// Creates multiplayer class of player
		player = new PlayerMP(null, -1, username, 0, 0, 0);
		// Creates floor
		floor = new Floor();

		// Initializing my simple physics
		physics = new Physics();

		// Add entities to the arrays
		getPlayers().add(player);
		floors.add(floor);

		// Initializing the modelloader & load the models
		modelLoader = new ModelLoader();
		modelLoader.loadModels();

		// Login packet for network
		Packet00Login loginPacket = new Packet00Login(username,
				player.getPos().x, player.getPos().y, player.getPos().z);

		loginPacket.writeData(client);

		// If you host, add yourself
		if (server != null) {
			server.addConnection((PlayerMP) player, loginPacket);
		}

		// Screen grabs the mouse
		Mouse.setGrabbed(true);
	}

	// All the updates / inputs
	public void update() {
		physics.updatePhysics();
		camera.setPosition(new Vector3f(-player.getPos().x
				- (player.getWidth() / 2), -player.getPos().y
				- (player.getHeight() / 2), -player.getPos().z
				- (player.getLength() / 2)));
		camera.movement();

		player.movement(camera.yaw);

		for (Player player : getPlayers()) {
			player.update();
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			shutDown();
		}
	}

	// All the rendering
	public void render() {
		glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		glLoadIdentity();

		glLight(GL_LIGHT0, GL_POSITION,
				BufferTools.asFlippedFloatBuffer(lightPosition));

		camera.useView();

		for (Floor floor : floors) {
			floor.render(modelLoader.floorTexture, ModelLoader.floorModel);
		}

		for (Player player : getPlayers()) {
			player.render(modelLoader.playerTexture, ModelLoader.playerModel);
		}

		Display.update();
		Display.sync(120);
	}

	// We use synchronized because players will be joining whenever & that might cause an exception
	public synchronized ArrayList<Player> getPlayers() {
		return players;
	}

	// Gameloop
	public void gameLoop() {
		while (!Display.isCloseRequested()) {
			// Update game logic and input
			update();

			// Then render everything
			render();
		}
	}

	// Remove player if the player disconnect
	public void removePlayerMP(String username) {
		int index = 0;
		for (Player p : getPlayers()) {
			if (p instanceof PlayerMP
					&& ((PlayerMP) p).getUsername().equals(username)) {
				break;
			}
			index++;
		}
		this.getPlayers().remove(index);
	}

	// Get player index
	public int getPlayerMPIndex(String username) {
		int index = 0;
		for (Player p : getPlayers()) {
			if (p instanceof PlayerMP
					&& ((PlayerMP) p).getUsername().equals(username)) {
				break;
			}
			index++;
		}
		return index;
	}

	// Move the player
	public void movePlayer(String username, float x, float y, float z) {
		int index = getPlayerMPIndex(username);
		this.getPlayers().get(index).setPos(new Vector3f(x, y, z));
	}

	// Shut everything down clean
	public void shutDown() {
		System.out.println("Closing down");

		// Handling disconnection
		Packet01Disconnect packet = new Packet01Disconnect(
				this.player.getUsername());
		packet.writeData(this.client);

		cleanUp();
		System.exit(1);
	}

	// Create basic lighting
	public void createLighting() {
		glEnable(GL_DEPTH_TEST);
		glShadeModel(GL_SMOOTH);
		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);
		glLightModel(
				GL_LIGHT_MODEL_AMBIENT,
				BufferTools.asFlippedFloatBuffer(new float[] { 0.1f, 0.1f,
						0.1f, 1f }));
		glLight(GL_LIGHT0, GL_POSITION,
				BufferTools.asFlippedFloatBuffer(new float[] { 0.8f, 0, 0, 1 }));
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glEnable(GL_COLOR_MATERIAL);
		glColorMaterial(GL_FRONT, GL_DIFFUSE);

	}

	// Initialize the display
	public void createDisplay() {
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setTitle("MP Game pre-alpha v0.1.1");
			Display.create();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Clean up lwjgl
	public void cleanUp() {
		Display.destroy();
		Keyboard.destroy();
		Mouse.destroy();
	}
}
