package app.ai;



import app.initialization.GameMap;
import app.initialization.Player;
import app.interactables.Enemy;
import app.mechanics.Ray;

import java.awt.*;

/**
 * EnemyAI provides the game with some difficulty as enemies will now search out the player and start attacking
 * once at the right distance.
 *
 *  @author Oliver Björklund, Jonathan Eriksson
 *  @version 1.0
 *
 */


public class EnemyAI
{
    private Enemy enemy;

    private Player player;

    private GameMap gameMap;

    private int distance;
    private AStar aStar;
    private boolean playerVisible;
    private int attackDelay;

    /**
     * Constructs the EnemyAI object
     *
     * @param enemy the enemy which is meant to be controlled by EnemyAI
     * @param player the player which is meant to be the target of EnemyAI
     * @param gameMap the map which is meant to be travered by the Enemy
     */
    public EnemyAI(final Enemy enemy, final Player player, final GameMap gameMap) {
	this.enemy = enemy;
	this.player = player;
	this.gameMap = gameMap;
	this.attackDelay = 60;
	this.playerVisible = false;
	this.distance = enemy.getDistanceToPlayer(player);
	this.aStar = new AStar(gameMap, player, enemy);
    }

    /**
     * update() updates important values of distance and angle to the player, checks if the player is visible to the AI,
     * counts the attack delay and calls the method act().
     */
    public void update(){

	if (!enemy.isOnMap()) return;
	this.distance = enemy.getDistanceToPlayer(player);

	double rayAngle = convertRadians(enemy.getAngleFromPlayer(player.getCurrentPos()) - Math.PI);
	Ray ray = new Ray(enemy.getCurrentPos(), rayAngle, gameMap);
	playerVisible = ray.getRayLength() >= distance;

	attackDelay--;
	enemy.act(player, gameMap, this);
    }

    public void findPath(){
	aStar.updatePath();
	Node newNode = aStar.pop();
	if(newNode != null) moveTowardsPoint(newNode.getPosition());
    }

    private void moveTowardsPoint(Point newPos) {
	final int stepDistance = 2;
	int currentX = enemy.getCurrentPos().x;
	int currentY = enemy.getCurrentPos().y;
	int diffX = Math.abs(newPos.x - currentX);
	int diffY = Math.abs(newPos.y - currentY);
	int xDiff = newPos.x > currentX ? stepDistance : -stepDistance;
	int yDiff = newPos.y > currentY ? stepDistance : -stepDistance;


	if (diffX > diffY) {
	    enemy.setCurrentPos(new Point(currentX + xDiff, currentY));
	} else {
	    enemy.setCurrentPos(new Point(currentX, currentY + yDiff));
	}
    }

    private double convertRadians(double angle) {
	angle %= (2 * Math.PI);
	if (angle < 0) {
	    angle += 2 * Math.PI;
	}
	return angle;
    }

    public boolean isPlayerVisible() {
	return playerVisible;
    }

    public int getAttackDelay() {
	return attackDelay;
    }

    public void setAttackDelay(final int attackDelay) {
	this.attackDelay = attackDelay;
    }
}
