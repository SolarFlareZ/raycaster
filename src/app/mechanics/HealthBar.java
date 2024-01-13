package app.mechanics;

import java.awt.*;

/**
 * Defines how healthbars should be displayed over animate objects.
 *
 * @author Oliver Björklund
 * @version 1.0
 */
public class HealthBar {
    private Dimension size;
    private int maxHP;


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
