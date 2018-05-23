package donkeykong;

public class LadderEntity extends Entity {
	private Game game; // the game in which the ship exists

	public LadderEntity(Game g, String r, int newX, int newY) {
		super(r, newX, newY); // calls the constructor in Entity
		game = g;
	} // constructor

	public void collidedWith(Entity other) {
		if (other instanceof PlayerEntity) {
			game.setTouchingLadder(true, this);
		} // if
	} // collidedWith
} // LadderEntity class

