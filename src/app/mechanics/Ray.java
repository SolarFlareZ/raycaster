package app.mechanics;

import app.initialization.GameMap;

import java.awt.*;
/**
* Defines a ray which is used to measure the distance to solid tiles.
 * The rays are used to render a 3d screen.
 * @author Oliver Björklund
 * @version 1.0
 */

public class Ray {
    private Point currentPos;
    private Point endPos;
    private double angle;
    private GameMap gameMap;
    private int rayLength;
    private int maxViewDistance;
    private Direction hitDirection = null;
    private Block blockType = null;

    private Block blockTypeX = null;
    private Block blockTypeY = null;


    public Ray(final Point currentPos, final double angle, final GameMap gameMap) {
	this.currentPos = currentPos;
	this.gameMap = gameMap;
	this.angle = angle;
	this.maxViewDistance = gameMap.getSize();
	this.endPos = getRayData();
    }

    private Point getRayData() {
	Point xAxis = getAxisData(Direction.VERTICAL);
	Point yAxis = getAxisData(Direction.HORIZONTAL);
	int distX = (int)Math.hypot(xAxis.x-currentPos.x, xAxis.y-currentPos.y);
	int distY = (int)Math.hypot(yAxis.x-currentPos.x, yAxis.y-currentPos.y);

	boolean yLessThanX = distY < distX;
	this.rayLength = yLessThanX ? distY : distX;
	this.blockType = yLessThanX ? this.blockTypeY : this.blockTypeX;
	this.hitDirection = yLessThanX ? Direction.HORIZONTAL : Direction.VERTICAL;
	return yLessThanX ? yAxis : xAxis;
    }

    private Point getAxisData(Direction axis) {
	Dimension blockSize = gameMap.getBlockSize();
	int rayY;
	int offsetY;
	int rayX;
	int offsetX;
	Direction dir;
	int viewDistance = 0;

	if(axis.equals(Direction.HORIZONTAL)) {
	    int roundedDivisor = roundToDivisor(currentPos.y, blockSize.height);
	    if (angle < (Math.PI / 2) || angle > (3 * Math.PI) / 2) {  //kollar uppåt							//kontroll av linjer i x-led -45 till 45 grader
		rayY = roundedDivisor;
		offsetY = -blockSize.height;
		dir = Direction.UP;
	    } else if (angle > (Math.PI / 2) && angle < (3 * Math.PI) / 2) {
		rayY = roundedDivisor + blockSize.height;
		offsetY = blockSize.height;
		dir = Direction.DOWN;
	    } else {
		return calcEndPos();
	    }
	    rayX = (int)((currentPos.y - rayY) * (Math.tan(angle)) + currentPos.x);
	    offsetX = (int)(-offsetY * Math.tan(angle));
	} else {
	    int roundedDivisor = roundToDivisor(currentPos.x, blockSize.width);
	    if (angle < Math.PI) {
		rayX = roundedDivisor + blockSize.width;
		offsetX = blockSize.width;
		dir = Direction.RIGHT;
	    } else if (angle > Math.PI) {
		rayX = roundedDivisor;
		offsetX = -blockSize.width;
		dir = Direction.LEFT;
	    } else {
		return calcEndPos();
	    }

	    rayY = (int) ((currentPos.x - rayX) * (1/Math.tan(angle)) + currentPos.y);
	    offsetY = (int) (-offsetX * (1/Math.tan(angle)));
	}

	while (viewDistance < maxViewDistance) {
	    int posY = rayY / blockSize.height;
	    int posX = rayX / blockSize.width;
	    if(dir.equals(Direction.UP)) posY--;
	    else if(dir.equals(Direction.LEFT)) posX--;
	    if(!gameMap.pointOnMap(posX, posY)) {
		return calcEndPos();
	    }

	    Block block = gameMap.getBlock(posX, posY);
	    if (!block.equals(Block.FLOOR)) {
		if(axis.equals(Direction.HORIZONTAL)) this.blockTypeY = block;
		else this.blockTypeX = block;
		return new Point(rayX, rayY);

	    } else {
		rayX += offsetX;
		rayY += offsetY;
		viewDistance++;
	    }
	}
	return calcEndPos();
    }

    private int roundToDivisor(int n, int div) {
	while(n > 0) {
	    if(n%div == 0) return n;
	    n--;
	}
	return 0;
    }

    private Point calcEndPos() {
//	behöver ändras till bättre längd som inte är så lång
	int maxPixelViewX = maxViewDistance * gameMap.getBlockSize().width;
	int maxPixelViewY = maxViewDistance * gameMap.getBlockSize().height;

	double x = currentPos.x+maxPixelViewX*Math.sin(angle);
	double y = currentPos.y-maxPixelViewY*Math.cos(angle);

	return new Point((int)x, (int)y);
    }

    public void draw(Graphics g) {
	double scalingFactor = gameMap.getScalingFactor();
	Point relativePos = new Point((int)(currentPos.x/scalingFactor), (int)(currentPos.y/scalingFactor));
	Point relativeEndPos = new Point((int)(endPos.x/scalingFactor), (int)(endPos.y/scalingFactor));

	g.setColor(Color.PINK);
	g.drawLine(relativePos.x, relativePos.y, relativeEndPos.x, relativeEndPos.y);
    }

    public int getRayLength() {
	return rayLength;
    }

    public double getAngle() {
	return angle;
    }

    public Direction getHitDirection() {
	return hitDirection;
    }

    public Block getBlockType() {
	return blockType;
    }

    public Point getEndPos() {
	return endPos;
    }
}
