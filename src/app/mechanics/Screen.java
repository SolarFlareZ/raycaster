package app.mechanics;

import app.initialization.GameMap;
import app.initialization.Player;
import app.interactables.Interactable;
import app.resourcehandlers.Texture;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Logger;

/**
* Screen uses JComponent to draw everything in the game. To correctly draw the walls of the game,
* rays are used to measure distance and get their "types". When drawing objects rays are also used to instruct
* screen if the object is to be drawn or not.
 *
 * @author Oliver Björklund, Jonathan Eriksson
 * @version 1.0
 */
public class Screen extends JComponent
{
    private GameMap gameMap;
    private Player player;
    private Dimension preferredSize;
    private HealthBar healthBar;
    private Map<Block, Texture> textureMap;
    private static final Logger LOGGER = Logger.getLogger("GameLogger");

    /**
     * The constructor for Screen
     *
     * @param player the player whos "vision" screen is to show
     * @param gameMap the map which is to be shown
     * @param preferredSize	the prefered wize of the screen
     */
    public Screen(final Player player, final GameMap gameMap, final Dimension preferredSize) {
	this.preferredSize = preferredSize;
	this.gameMap = gameMap;
	this.player = player;
	this.healthBar = new HealthBar(0, 0, player.getMaxHp());
	this.textureMap = new EnumMap<>(Block.class);
	createTextureMap();
	LOGGER.config("Screen successfully initialized");
    }

    private void createTextureMap() {
	textureMap.put(Block.WALL, new Texture("images/block.jpg"));
	textureMap.put(Block.DOOR, new Texture("images/door.jpg"));
    }

    private double convertRadians(double angle) {
	angle %= (2 * Math.PI);
	if (angle < 0) {
	    angle += 2 * Math.PI;
	}
	return angle;
    }


    @Override public Dimension getPreferredSize() {
	return preferredSize;
    }

    @Override protected void paintComponent(final Graphics g) {
	super.paintComponent(g);
	drawBackground(g);
	player.createRays();
	drawBlocks(g);
	drawObjects(g);

	Interactable item = player.getInventory().getSelectedItem();
	if(item != null) item.drawInHand(g);
	player.getInventory().draw(g);
	drawCrossHair(g);
	this.healthBar.drawStatic(g, player.getHp(), new Rectangle(0, 0, 250, 40));
    }

    private void drawCrossHair(Graphics g) {
	Texture texture = new Texture("images/crosshair.png");
	final int width = 20;
	final int height = 20;
	int xPos = getWidth()/2-width/2;
	int yPos = getHeight()/2-height/2;
	g.drawImage(texture.getImg(), xPos, yPos, width, height, null);
    }

    private void drawBackground(Graphics g) {
	int halfScreen = getHeight() / 2;
	g.setColor(Color.GRAY);
	g.fillRect(0, 0, getWidth(), halfScreen);
	g.setColor(Color.DARK_GRAY);
	g.fillRect(0, halfScreen, getWidth(), halfScreen);
    }

    private void drawBlocks(Graphics g) {
	int numberOfRays = player.getNumberOfRays();
	int rectWidth = getWidth() / numberOfRays;

	for (int i = 0; i < numberOfRays; i++) {
	    Ray ray = player.getRay(i);
	    Texture texture = textureMap.get(ray.getBlockType());
	    Dimension textureSize = texture.getSize();
	    double scalingFactor = gameMap.getBlockSize().width / (double)textureSize.width;

	    int rayLength = getCompensatedLength(ray.getAngle(), ray.getRayLength());
	    if (rayLength == 0) continue;
	    Direction hitDirection = ray.getHitDirection();
	    boolean hitsVertical = hitDirection.equals(Direction.VERTICAL);

	    int rectHeight = getHeight() * gameMap.getBlockSize().height / rayLength;
	    double stepFactorY = (double)textureSize.height/rectHeight;

	    double heightOffset = rectHeight > getHeight() ? (rectHeight-getHeight()) / 2.0 * stepFactorY : 0;
	    rectHeight = Math.min(rectHeight, getHeight());

	    int xPos = i * rectWidth;

	    int directionEndPos = hitsVertical ? ray.getEndPos().y : ray.getEndPos().x;
	    int pixelArrX = (int)(directionEndPos/scalingFactor)%textureSize.width;


	    for (int j = 0; j < rectHeight; j++) {
		double pixelArrY = stepFactorY*j+heightOffset;
		int yPos = getHeight()/2 - rectHeight/2+j;
		int pixel = texture.getPixels()[pixelArrX + (int)(pixelArrY) * textureSize.width];
		Color color = new Color(pixel);
		if(hitsVertical) color = color.darker();
		g.setColor(color);
		g.fillRect(xPos, yPos, rectWidth, 1);
	    }
	}
    }

    private int getCompensatedLength(double angle, int currentLength) {
	double compensatedAngle = convertRadians(player.getAngle() - angle);
	return (int) (currentLength * Math.cos(compensatedAngle));
    }



    private void drawObjects(Graphics g) {
	gameMap.sortInteractables(new DistanceComparator());

	double angleBetweenRays = (Math.PI/3)/player.getNumberOfRays();
	double leftPlayerAngle = convertRadians(player.getAngle() - Math.PI / 6);
	for (int i = 0; i < gameMap.getNumberOfObjects(); i++) {
	    Interactable interactable = gameMap.getObject(i);

	    double angleDiff = convertRadians(interactable.getAngleFromPlayer(player.getCurrentPos()) - leftPlayerAngle);
	    int distanceToPlayer = interactable.getDistanceToPlayer(player);

	    if (angleDiff >= 0 && angleDiff <= Math.PI/3) { //+-30 grader ca
		int correspondingRay = (int)(angleDiff/angleBetweenRays); //58 för att det bör funka hyggligt (60-114*-0.523 =
		if (distanceToPlayer < player.getRay(correspondingRay).getRayLength()) { //eventuellt check direkt mot angle
		    interactable.drawObject(g, angleDiff, distanceToPlayer);
		}
	    }
	}
    }

    public void setGameMap(final GameMap gameMap) {
	this.gameMap = gameMap;
    }

    private class DistanceComparator implements Comparator<Interactable> {
	@Override
	public int compare(Interactable int1, Interactable int2) {
	    return Integer.compare(int2.getDistanceToPlayer(player),
				   int1.getDistanceToPlayer(player));
	}
    }
}