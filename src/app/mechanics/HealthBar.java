package app.mechanics;

import java.awt.*;

/**
 * Healthbars are drawn over all "Animated" objects. They are used to inform the
 * player about the current health of an enemy/object.
 *
 * @author Oliver Björklund, Jonathan Eriksson
 * @version 1.0
 */
public class HealthBar {
    private Dimension size;
    private int maxHP;

    /**
     * The constructor for HealthBar
     *
     * @param x 	the width of the health bar
     * @param y 	the height of the health bar
     * @param maxHP	the amount of health it represents
     */
    public HealthBar(int x, int y, int maxHP) {
	this.size = new Dimension(x, y);
	this.maxHP = maxHP;
    }

    public void drawStatic(Graphics g, int hp, Rectangle shape) {
	Graphics2D g2d = (Graphics2D)g;
	g2d.setColor(Color.BLACK);
	g2d.fill(shape);

	double quotient = hp/(double)maxHP;
	g2d.setColor(getHealthColor(quotient));
	int width = (int)(shape.width*quotient);
	g2d.fillRect(shape.x, shape.y, width, shape.height);
    }

    private Color getHealthColor(double quotient) {
	final double mediumHp = 0.5;
	final double lowHp = 0.2;

	if(quotient > mediumHp) return Color.GREEN;
	else if(quotient > lowHp) return Color.YELLOW;
	else return Color.RED;
    }

    public Dimension getSize() {
	return size;
    }
}
