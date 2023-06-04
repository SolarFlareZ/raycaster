package app.mechanics;


import app.initialization.Config;
import app.interactables.Interactable;
import app.interactables.Weapon;
import app.resourcehandlers.Texture;

import java.awt.*;

/**
 * Inventory keeps track on which items the player has picked up
 * When an item is in the inventory it also displays said items cooldown.
 *
 * @author Oliver Björklund, Jonathan Eriksson
 * @version 1.0
 */
public class Inventory {
    private Dimension slotSize;
    private Color color;

    private Interactable[] items;
    private int selectedSlot;

    /**
     * The constructor for Inventory
     *
     * @param color	the background color of the inventory
     */
    public Inventory(final Color color) {
	this.slotSize = new Dimension(70, 70);
	this.color = color;
	final int numberOfSlots = 4;
	this.items = new Interactable[numberOfSlots];
	this.selectedSlot = 0;
    }

    public void draw(Graphics g) {
	Graphics2D g2d = (Graphics2D)g;
	int posY = Config.FRAME_HEIGHT - slotSize.height;
	for (int i = 0; i < items.length; i++) {
	    g2d.setColor(color);
	    int posX = i*slotSize.width;
	    Rectangle shape = new Rectangle(posX, posY, slotSize.width, slotSize.height);

	    g2d.fill(shape);

	    Interactable item = items[i];
	    if(item == null) continue;
	    drawItem(g2d, item, i);
	    if(item.isUsable()) drawCooldown(g2d, (Weapon)item, shape);
	}
	int posX = selectedSlot*slotSize.width;
	g2d.setColor(Color.WHITE);
	g2d.setStroke(new BasicStroke(5));
	g2d.drawRect(posX, posY, slotSize.width, slotSize.height);
    }

    private void drawCooldown(Graphics2D g2d, Weapon item, Rectangle shape) {
	if(item.isAllowedToAttack()) return;
	int totalCooldown = item.getCooldown();
	int currentCooldown = item.getCurrentCooldown();
	double quotient = currentCooldown/(double)totalCooldown;

	g2d.setColor(new Color(20, 20, 20, 125));
	shape.width = (int)(slotSize.width*quotient);
	shape.height = 300;
	g2d.fill(shape);
    }


    private void drawItem(Graphics2D g2d, Interactable item, int pos) {
	if(item==null) return;
	final int size = 45;
	int posX = pos*slotSize.width+(slotSize.width-size)/2;
	int posY = Config.FRAME_HEIGHT-slotSize.height+(slotSize.width-size)/2;
	Texture texture = item.getTexture();
	g2d.drawImage(texture.getImg(), posX, posY, size, size, null);
    }

    public void add(Interactable item) {
	for (int i = 0; i < items.length; i++) {
	    if(items[i]==null) {
		items[i]=item;
		item.setOnMap(false);
		break;
	    }
	}
    }

    public void selectInventorySlot(int i) {
	this.selectedSlot = i;
    }

    public Interactable getSelectedItem() {
	return items[selectedSlot];
    }

}
