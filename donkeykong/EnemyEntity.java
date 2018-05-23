package donkeykong;
import donkeykong.Game;

public class EnemyEntity extends Entity {
	private Game game;
	
	public EnemyEntity(Game g, String r, int newX, int newY) {
		super(r, newX, newY); // calls the constructor in Entity
		game = g;
	} // constructor

	public void collidedWith(Entity other) {
		if (other instanceof PlayerEntity) {
			game.hitEnemy();
		} // if
	} // collidedWith

} // EnemyEntity class

