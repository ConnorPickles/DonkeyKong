package donkeykong;

public class FinalPlatformEntity extends Entity {

	public FinalPlatformEntity(Game g, String r, int newX, int newY) {
		super(r, newX, newY); // calls the constructor in Entity
	} // constructor

	public void collidedWith(Entity other) {
	} // collidedWith

} // FinalPlatformEntity class