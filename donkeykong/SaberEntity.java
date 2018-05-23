package donkeykong;

public class SaberEntity extends Entity {

	private Game game; // the game in which the ship exists

	public SaberEntity(Game g, String r, int newX, int newY) {
		super(r, newX, newY); // calls the constructor in Entity
		game = g;
	} // constructor

	public void collidedWith(Entity other) {
		
		// picks up saber
		if (other instanceof PlayerEntity) {
			game.removeEntity(this);
			game.getSaber();
		} // if

	} // collidedWith

} // SaberEntity class

