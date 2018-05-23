package donkeykong;

public class LivesEntity extends Entity {

	public LivesEntity(Game g, String r, int newX, int newY) {
		super(r, newX, newY); // calls the constructor in Entity
	} // constructor

	public void collidedWith(Entity other) {
	} // collidedWith
	
} // LivesEntity

