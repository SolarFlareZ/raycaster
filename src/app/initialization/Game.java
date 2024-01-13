package app.initialization;

import app.interactables.Bullet;
import app.interactables.Enemy;
import app.interactables.Interactable;
import app.interactables.Key;
import app.interactables.Weapon;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import app.ai.EnemyAI;
import app.mechanics.Direction;
import app.mechanics.MenuScreen;
import app.mechanics.MiniMap;
import app.mechanics.Screen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * The Game class represents the main game engine, managing the player, game map, enemies, and various screens.
 * It includes methods for initializing the game, loading maps, handling user input, managing AI, and updating the game state.
 * @author Oliver Björklund
 * @version 1.0
 */
public class Game {
    private Player player;
    private Screen screen3d;
    private MiniMap miniMap;
    private JFrame frame;
    private GameMap gameMap = null;
    private MenuScreen menuScreen;
    private int currentLevel;

    private EnemyAI[] enemyAis;
    private static final Logger LOGGER = setUpLogger();
    private int numberOfLevels = 0;

    public Game() {
	LOGGER.info("Game started");
	Dimension frameSize = new Dimension(Config.FRAME_WIDTH, Config.FRAME_HEIGHT);
	this.currentLevel = 0;
	loadMap(currentLevel);
	this.player = new Player(this, gameMap);
	this.menuScreen = new MenuScreen(frameSize);
	this.screen3d = new Screen(player, gameMap, frameSize);
	this.miniMap = new MiniMap(player, gameMap, frameSize);
	this.enemyAis = new EnemyAI[gameMap.getNumberOfEnemies()];
	initFrame();
	addComponent(menuScreen);

	initMenu();

	createInputMap();
	createActionMap();
	initAI();
	LOGGER.config("Game instance successfully initialized");
    }

    private void initFrame() {
	this.frame = new JFrame("Game");
	frame.setLayout(new FlowLayout());
    }

    private static Logger setUpLogger() {
	Logger logger = Logger.getLogger("GameLogger");

	while(true) {
	    try {
		FileHandler handler = new FileHandler("GameLog.log", true);
		handler.setFormatter(new SimpleFormatter());
		logger.addHandler(handler);
		break;
	    } catch(IOException e) {
		logger.warning("Unable to initialize logging file");
		e.printStackTrace();
		int answer = JOptionPane.showConfirmDialog(null, "Unable to find logging file. Would you like to try again?",
					      "Error", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
		if(answer == JOptionPane.NO_OPTION) break;
	    }
	}
	logger.info("Logger initialized");
	return logger;
    }

    private void initMenu() {
	menuScreen.getStartButton().addActionListener(e -> {
	    frame.remove(menuScreen);
	    addComponent(screen3d);
	    addComponent(miniMap);

	    startGame();
	});

	menuScreen.getExitButton().addActionListener(e -> {
	    LOGGER.info("Game exited");
	    System.exit(0);
	});
    }

    private void loadMap(int index) {
	while(true) {
	    try {
		GsonBuilder builder = new GsonBuilder();
		MapDeserializer deserializer = new MapDeserializer();
		builder.registerTypeAdapter(GameMap.class, deserializer);
		Gson gson = builder.create();

		Path filePath = Paths.get("Maps.json");
		String jsonString = Files.readString(filePath);
		JsonArray jsonElements = gson.fromJson(jsonString, JsonArray.class);
		if(numberOfLevels == 0) numberOfLevels = jsonElements.size();
		JsonElement mapElement = jsonElements.get(index);

		GameMap gameMap = gson.fromJson(mapElement, GameMap.class);
		this.gameMap = gameMap;
		break;
	    } catch (IOException e) {
		LOGGER.severe("Unable to load map from json file");
		e.printStackTrace();
		int answer = JOptionPane.showConfirmDialog(null, "Unable to load map. Would you like to try again?",
							   "Error", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
		if(answer == JOptionPane.NO_OPTION) System.exit(1);
	    }
	}

    }

    private void createInputMap() {
	final InputMap in = screen3d.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
	in.put(KeyStroke.getKeyStroke("typed a"), "left");
	in.put(KeyStroke.getKeyStroke("typed d"), "right");
	in.put(KeyStroke.getKeyStroke("typed w"), "up");
	in.put(KeyStroke.getKeyStroke("typed s"), "down");
	in.put(KeyStroke.getKeyStroke("typed q"), "rotLeft");
	in.put(KeyStroke.getKeyStroke("typed e"), "rotRight");
	in.put(KeyStroke.getKeyStroke("typed 1"), "select1");
	in.put(KeyStroke.getKeyStroke("typed 2"), "select2");
	in.put(KeyStroke.getKeyStroke("typed 3"), "select3");
	in.put(KeyStroke.getKeyStroke("typed 4"), "select4");
	in.put(KeyStroke.getKeyStroke("released A"), "sleft");
	in.put(KeyStroke.getKeyStroke("released D"), "sright");
	in.put(KeyStroke.getKeyStroke("released W"), "sup");
	in.put(KeyStroke.getKeyStroke("released S"), "sdown");
	in.put(KeyStroke.getKeyStroke("released Q"), "srotLeft");
	in.put(KeyStroke.getKeyStroke("released E"), "srotRight");
	in.put(KeyStroke.getKeyStroke("ENTER"), "useItem");
	in.put(KeyStroke.getKeyStroke("TAB"), "toggleMiniMap");
	in.put(KeyStroke.getKeyStroke("ESCAPE"), "exit");

    }

    private void createActionMap() {
	final ActionMap act = screen3d.getActionMap();
	act.put("left", new MoveAction(Direction.LEFT, true));
	act.put("right", new MoveAction(Direction.RIGHT, true));
	act.put("up", new MoveAction(Direction.UP, true));        //rotateAction
	act.put("down", new MoveAction(Direction.DOWN, true));
	act.put("rotLeft", new MoveAction(Direction.ROT_LEFT, true));
	act.put("rotRight", new MoveAction(Direction.ROT_RIGHT, true));
	act.put("sleft", new MoveAction(Direction.LEFT, false));
	act.put("sright", new MoveAction(Direction.RIGHT, false));
	act.put("sup", new MoveAction(Direction.UP, false));        //rotateAction
	act.put("sdown", new MoveAction(Direction.DOWN, false));
	act.put("srotLeft", new MoveAction(Direction.ROT_LEFT, false));
	act.put("srotRight", new MoveAction(Direction.ROT_RIGHT, false));
	act.put("select1", new InventoryAction(0));
	act.put("select2", new InventoryAction(1));
	act.put("select3", new InventoryAction(2));
	act.put("select4", new InventoryAction(3));
	act.put("useItem", new UseItemAction());
	act.put("toggleMiniMap", new ToggleMiniMapAction());
	act.put("exit", new AbstractAction() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		System.exit(0);
	    }
	});
    }

    private void initAI() {
	for (int i = 0; i < gameMap.getNumberOfEnemies(); i++) {
	    Enemy enemy = gameMap.getEnemy(i);
	    EnemyAI enemyAi = new EnemyAI(enemy, player, gameMap);
	    this.enemyAis[i] = enemyAi;
	}
    }

    private void addComponent(JComponent screen) {
	frame.add(screen);
	frame.pack();
	frame.setVisible(true);
    }

    private void startGame() {
	final int fps = 60;
	final int frameRate = 1000/fps;
	final Timer timer = new Timer(frameRate, e -> tick());
	timer.setCoalesce(true);
	timer.start();
    }

    private class UseItemAction extends AbstractAction {

	@Override public void actionPerformed(final ActionEvent e) {
	    Interactable item = player.getInventory().getSelectedItem();
	    if(item.isUsable()) {
		Weapon weapon = (Weapon)item;
		weapon.use(player, gameMap);
	    }
	}
    }

    private class ToggleMiniMapAction extends AbstractAction {
	@Override public void actionPerformed(final ActionEvent e) {
	    List<Component> components = Arrays.asList(frame.getContentPane().getComponents());
	    if(components.contains(miniMap)) {
		frame.remove(miniMap);
		frame.pack();
		frame.setVisible(true);
	    } else addComponent(miniMap);
	}
    }


    private class MoveAction extends AbstractAction {
	private Direction dir;
	private boolean moving;
	private MoveAction(Direction dir, boolean moving){
	    this.moving = moving;
	    this.dir = dir;
	}
	public void actionPerformed(final ActionEvent e){
	    player.toggleMovement(dir, moving);
	}
    }

    private class InventoryAction extends AbstractAction {
	private int slot;
	private InventoryAction(int slot){
	    this.slot = slot;
	}
	public void actionPerformed(final ActionEvent e){
	    player.getInventory().selectInventorySlot(slot);
	}
    }

    public void resetMap() {
	loadMap(currentLevel);
	this.enemyAis = new EnemyAI[gameMap.getNumberOfEnemies()];
	player.initPlayer(gameMap);
	screen3d.setGameMap(gameMap);
	miniMap.setGameMap(gameMap);

	initAI();
    }

    public void completeLevel() {
	LOGGER.info("Level completed");
	currentLevel+=1;
	if(currentLevel>=numberOfLevels) {
	    LOGGER.info("Game completed");
	    System.exit(0); //denna måste ändras manuellt så den stämmer med antal maps i Json
	}

	resetMap();
    }

    private void tick() {
	miniMap.repaint();
	screen3d.repaint();
	pickUpInteractables();
	player.moveTick();
	updateAI();
	updateBullets();
    }

    private void updateAI() {
	for (final EnemyAI enemyAi : enemyAis) {
	    enemyAi.update();
	}
    }

    private void pickUpInteractables() {
	Key key = gameMap.getKey();
	if(player.tryPickUp(key)) player.pickedUpKey();

	for (int i = 0; i < gameMap.getNumberOfWeapons(); i++) {
	    Weapon weapon = gameMap.getWeapon(i);
	    player.tryPickUp(weapon);
	}
    }

    private void updateBullets() {
	for (int i = 0; i < gameMap.getNumberOfBullets(); i++) {
	    Bullet bullet = gameMap.getBullet(i);
	    if(!bullet.isOnMap()) gameMap.removeBullet(bullet);
	    else bullet.update(gameMap, player);
	}
    }


}
