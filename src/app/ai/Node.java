package app.ai;

import app.initialization.GameMap;
import app.mechanics.Block;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * An A* node which holds g, h and f values and is used with the A* search algorithm.
 * @author Oliver Björklund
 * @version 1.0
 */
public class Node {
    private Point position;
    private int gValue;
    private int hValue;
    private int fValue;
    private Node parent;


    public Node(final Point position, Node parent) {
	this.position = position;
        this.parent = parent;
        this.fValue = this.gValue = this.hValue = 0;
    }

    public List<Point> getNeighbors(GameMap gameMap) {
        final int searchStep = 20;
        final int noStep = 0;

        int mapWidth = gameMap.getSize() * gameMap.getBlockSize().width;
        int mapHeight = gameMap.getSize() * gameMap.getBlockSize().height;

        List<Point> neighbors = new ArrayList<>();

        int[] rowValues = {-searchStep, noStep, searchStep, noStep};
        int[] colValues = {noStep, searchStep, noStep, -searchStep};
        for (int i = 0; i < rowValues.length; i++) {
            int neighborRow = this.position.y + rowValues[i];
            int neighborCol = this.position.x + colValues[i];

            if (neighborRow >= 0 && neighborRow < mapWidth &&
                neighborCol >= 0 && neighborCol < mapHeight) {
                neighbors.add(new Point(neighborCol, neighborRow));
            }
        }

        return neighbors;
    }

    public boolean isWalkable(GameMap gameMap) {
        int posX = position.x / gameMap.getBlockSize().width;
        int posY = position.y / gameMap.getBlockSize().height;
        return gameMap.getBlock(posX, posY) == Block.FLOOR;
    }


    public int getGValue() {
        return gValue;
    }


    public int getFValue() {
        return fValue;
    }

    public Node getParent() {
        return parent;
    }





    public Point getPosition() {
        return position;
    }


    @Override public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Node node = (Node) o;
        return Objects.equals(position, node.position);
    }

    @Override public int hashCode() {
        return Objects.hash(position);
    }


    public void updateStatus(Node start, Node goal) {
        this.gValue = Math.abs(position.x - start.getPosition().x) +
                      Math.abs(position.y-start.getPosition().x);
        this.hValue = Math.abs(position.x - goal.getPosition().x) +
                      Math.abs(position.y-goal.getPosition().y);
        this.fValue = hValue + gValue;
    }

}
