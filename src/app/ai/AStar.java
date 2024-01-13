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
 * AStar is a class that implements the A* pathfinding algorithm for navigating a GameMap.
 * The algorithm uses a priority queue and a set to efficiently explore possible paths.
 * @author Oliver Björklund
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


    public AStar(final GameMap gameMap, final Player player, final Enemy enemy) {
	this.gameMap = gameMap;
	this.enemy = enemy;
	this.player = player;
	this.open = new PriorityQueue<>(new NodeComparator());
	this.closed = new HashSet<>();
    }


    private boolean tempCondition(Point path) {
	final int targetDistance = 20;

	Point playerPos = player.getCurrentPos();
	if(Math.abs(path.x - playerPos.x) <= targetDistance && Math.abs(path.y - playerPos.y) <= targetDistance) return true;
	return false;
    }



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


    public Node pop() {
	if(path.size()>1) return path.removeLast();
	return null;
    }



    public class NodeComparator implements Comparator<Node> {
	@Override
	public int compare(Node node1, Node node2) {
	    return Integer.compare(node1.getFValue(), node2.getFValue());
	}
    }
}
