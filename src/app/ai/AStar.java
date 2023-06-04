package app.ai;

import app.initialization.GameMap;
import app.initialization.Player;
import app.interactables.Enemy;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 * Astar is a pathfinding algorithm that is less CPU heavy than BFS.
 * Its pathfinding works by checking the distance from both the target and the current node
 * of neighbouring nodes, and then paths to the neighbour with the lowest weighting til it
 * find the target node.
 * <p>
 *     AStar is dependant of Node.
 * </p>
 * @author Oliver Björklund, Jonathan Eriksson
 * @version 1.0
 */
public class AStar {
    private GameMap gameMap;
    private Node start = null;
    private Node goal = null;
    private Queue<Node> open;
    private Set<Node> closed;
    private Deque<Node> path = null;
    private Player player;
    private Enemy enemy;

    /**
     * Constructs a new Astar object.
     *
     * @param gameMap the gameMap that is meant to be navigated
     * @param player the Player which is the target of the enemy.
     * @param enemy the enemy which is meant to track the target.
     */
    public AStar(final GameMap gameMap, final Player player, final Enemy enemy) {
	this.gameMap = gameMap;
	this.enemy = enemy;
	this.player = player;
	this.open = new PriorityQueue<>(new NodeComparator());
	this.closed = new HashSet<>();
    }
/**
* tempCondition returns a boolean based on the targetDistance between the player and the argument Point.
 * <p>
 *     used in search()
 * </p>
 *
 * @param path a point of the path
 */

    private boolean tempCondition(Point path) {
	final int targetDistance = 20;

	Point playerPos = player.getCurrentPos();
	if(Math.abs(path.x - playerPos.x) <= targetDistance && Math.abs(path.y - playerPos.y) <= targetDistance) return true;
	return false;
    }

    /**
     * updatePath updates the path of the Astar algorithm
     */

    public void updatePath() {
	this.start = new Node(new Point(enemy.getCurrentPos().x, enemy.getCurrentPos().y), null);
	this.goal = new Node(new Point(player.getCurrentPos().x, player.getCurrentPos().y), null);
	open.clear();
	closed.clear();


	this.path = search();
    }

    private Deque<Node> search() {

	open.add(start);

	while(!open.isEmpty()) {

	    Node currentNode = open.poll();
	    closed.add(currentNode);

	    if(tempCondition(currentNode.getPosition())) {
		Deque<Node> path = new ArrayDeque<>();
		Node nodePath = currentNode;
		while(nodePath != null) {
		    path.addLast(nodePath);
		    nodePath = nodePath.getParent();
		}
		path.removeLast();
		return path;
	    }

//	    320 330
	    List<Point> neighbors = currentNode.getNeighbors(gameMap);
//

	    for (Point neighbor : neighbors) {
		Node neighborNode = new Node(neighbor, currentNode);

		if(!neighborNode.isWalkable(gameMap) || closed.contains(neighborNode)) {
		    continue;
		}

		neighborNode.updateStatus(start ,goal);


		Node nodeInQueue = getNodeFromQueue(neighborNode, open);

		if(nodeInQueue != null && nodeInQueue.getGValue() <= neighborNode.getGValue()) {
		    continue;
		}
		open.add(neighborNode);
	    }
	}
	return new ArrayDeque<>();
    }

    public Node getNodeFromQueue(Node node, Queue<Node> q) {
	for (Node n : q) {
	    if(n.getPosition().equals(node.getPosition())) return n;
	}
	return null;
    }

    /**
     * pop() removes the last node in path if its larget than 1.
     */
    public Node pop() {
	if(path.size()>1) return path.removeLast();
	return null;
    }

    /**
     * NodeComparator compares the F-value of two nodes
     */

    public class NodeComparator implements Comparator<Node> {
	@Override
	public int compare(Node node1, Node node2) {
	    return Integer.compare(node1.getFValue(), node2.getFValue());
	}
    }
}
