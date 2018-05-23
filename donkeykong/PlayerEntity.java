package donkeykong;

public class PlayerEntity extends Entity {
    private Game game;
    
    public PlayerEntity (Game g, String r, int newX, int newY) {
    	super(r, newX, newY); // calls the constructor in Entity
		game = g;
    } // PlayerEntity
    
    public void move(long delta) {
		// stop at left side of screen
		if ((dx < 0) && (x <= 0)) {
			this.setX(0);
			this.setHorizontalMovement(0);
			return;
		} // if
			// stop at right side of screen
		if ((dx > 0) && (x >= (800 - this.sprite.getWidth()))) {
			this.setX(800 - this.sprite.getWidth());
			this.setHorizontalMovement(0);
			return;
		} // if

		super.move(delta); // calls the move method in Entity
	} // move
    
    public void collidedWith(Entity other) {
    	int y = this.sprite.getHeight() - 1;
    	
    	if (other instanceof PlatformEntity) {
    		this.setOnPlat(true);
    	} // if
    	
    	if (other instanceof PlatformEntity && (game.getClimbing())) {
    		game.setPlatY(other.getY());
    	} // if
    	
    	if (other instanceof PlatformEntity && this.getDY() > 250 && (game.getJumping()) && ((this.getY() + y) <= (other.getY() + other.sprite.getHeight()))) {
    		game.stopJump();
    		this.setY(other.getY() - 39);
			y = this.sprite.getHeight() - 1;
		} // if
    	
    	if (other instanceof PlatformEntity && (!game.getClimbing()) && (!game.getJumping()) && (((this.getY() + y) > other.getY()) && ((this.getY() + y) < (other.getY() + other.sprite.getHeight())))) {
    		this.setY(other.getY() - y);
    	} // if
	} // collidedWith
} // PlayerEntity
