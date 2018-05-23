package donkeykong;

import java.awt.Graphics;
import java.awt.Image;

public class Sprite {

	public Image image; // the image to be drawn for this sprite

	// constructor
	public Sprite(Image i) {
		image = i;
	} // constructor

	// return width of image in pixels
	public int getWidth() {
		return image.getWidth(null);
	} // getWidth

	// return height of image in pixels
	public int getHeight() {
		return image.getHeight(null);
	} // getHeight

	// draw the sprite in the graphics object provided at location (x,y)
	public void draw(Graphics g, int x, int y) {
		g.drawImage(image, x, y, null);
	} // draw

} // Sprite