package donkeykong;

import java.awt.*;

public abstract class Entity {

	protected double x; // current x location
	protected double y; // current y location
	protected Sprite sprite; // this entity's sprite
	protected double dx; // horizontal speed (px/s) + -> right
	protected double dy; // vertical speed (px/s) + -> down
	protected boolean onPlat = false;
	private Rectangle me = new Rectangle(); // bounding rectangle of this entity
	private Rectangle him = new Rectangle(); // bounding rect. of other entities

	public Entity(String r, int newX, int newY) {
		x = newX;
		y = newY;
		sprite = (SpriteStore.get()).getSprite(r);
	} // constructor
	
	public void changeSprite(String r) {
		sprite = (SpriteStore.get()).getSprite(r);
	} // changeSprite

	public void move(long delta) {
		// update location of entity based on move speeds
		x += (delta * dx) / 1000;
		y += (delta * dy) / 1000;
	} // move

	public void draw(Graphics g) {
		sprite.draw(g, (int) x, (int) y);
	} // draw
	
	public boolean getOnPlat () {
		return onPlat;
	} // getOnPlat
	
	public void setOnPlat (boolean onPlat) {
		this.onPlat = onPlat;
	} // setOnPlat
	
	// get and set velocities
	public void setHorizontalMovement(double newDX) {
		dx = newDX;
	} // setHorizontalMovement

	public void setVerticalMovement(double newDY) {
		dy = newDY;
	} // setVerticalMovement
	
	// get and set coordinates
	public void setX (int x) {
		this.x = x;
	} // setX
	
	public void setY (int y) {
		this.y = y;
	} // setY
	
	public int getX () {
		return (int)x;
	} // getX
	
	public int getY () {
		return (int)y;
	} // getY
	
	// gets velocities
	public double getDX () {
		return dx;
	} // getDX
	
	public double getDY () {
		return dy;
	} // getDY

	public boolean collidesWith(Entity other) {
		me.setBounds((int) x, (int) y, sprite.getWidth(), sprite.getHeight());
		him.setBounds(other.getX(), other.getY(), other.sprite.getWidth(), other.sprite.getHeight());
		return me.intersects(him);
	} // collidesWith

	/*
	 * collidedWith input: the entity with which this has collided purpose:
	 * notification that this entity collided with another Note: abstract
	 * methods must be implemented by any class that extends this class
	 */
	public abstract void collidedWith(Entity other);

	public void doLogic() {
	} // doLogic
	
} // Entity class