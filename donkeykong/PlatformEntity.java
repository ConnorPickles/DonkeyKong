package donkeykong;

public class PlatformEntity extends Entity {
    
    public PlatformEntity (Game g, String r, int newX, int newY) {
    	super(r, newX, newY); // calls the constructor in Entity
    } // PlatformEntity
    
    public void collidedWith(Entity other) {
	} // collidedWith
    
} // PlatformEntity