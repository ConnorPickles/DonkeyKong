package donkeykong;

public class BarrelEntity extends Entity {
    private Game game;
    private Entity ladder; // ladder
    private Entity lastLadder; // previous ladder
    private boolean exploding = false; // is barrel exploding?
    private boolean climbingDown = false; // is barrel going down a ladder?
    private boolean touchingLadder = false; // is barrel touching ladder?
    private boolean startMovingOnPlat = false; // should barrel start moving next time it hits a platform?
    private int j = 0; // #, used for calulations
    private double lastDX = 0; // dx before the climb
    private long climbingTimer = 0; // when the barrel started climbing
    
    public BarrelEntity (Game g, String r, int newX, int newY) {
    	super(r, newX, newY); // calls the constructor in Entity
		game = g;
		
		if (game.getLevel() == 1) {
			dx = 175;
		} else if (game.getLevel() == 2) {
			dx = 250;
		} // else if
		
		dy = 0;
    } // PlayerEntity
    
    public void move(long delta) {
		// stop at left side of screen
    	if ((dx < 0) && (x < 0) && (y < 500)) {
    		this.setX(0);
			dx *= -1; 
		} // if

		// stop at right side of screen
		if ((dx > 0) && (x > (800 - this.sprite.getWidth()))) {
			this.setX(800 - this.sprite.getWidth());
			dx *= -1;
		} // if
		
		// removes if at the bottom
		if ((dx < 0) && (x < -50) && y > 500) {
			game.removeEntity(this);
			if (game.getRemovedBarrelCount() % 2 == 0 && game.getLevel() == 2) {
				game.spawnFireball();
			} // if
			game.setRemovedBarrelCount(game.getRemovedBarrelCount() + 1);
		} // if
		
		super.move(delta); // calls the move method in Entity
	} // move
    
    // gets exploding
    public boolean getExploding () {
    	return exploding;
    } // getExploding
    
    // sets exploding
    public void setExploding (boolean exploding) {
    	this.exploding = exploding;
    } // setExploding
    
    // gets climbingDown
    public boolean getClimbingDown () {
    	return climbingDown;
    } // getClimbingDown
    
    // sets climbingDown
    public void setClimbingDown (boolean climbingDown) {
    	this.climbingDown = climbingDown;
    } // setClimbingDown
    
    // gets climbingTimer
    public long getClimbingTimer () {
    	return climbingTimer;
    } // getClimbingTimer
    
    // sets climbingTimer
    public void setClimbingTimer (long climbingTimer) {
    	this.climbingTimer = climbingTimer;
    } // setClimbingTimer
    
    // gets lastDX
    public double getLastDX () {
    	return lastDX;
    } // getLastDX
    
    // sets lastDX
    public void setLastDX (double lastDX) {
    	this.lastDX = lastDX;
    } // setLastDX
    
    // gets touchingLadder
    public boolean getTouchingLadder () {
    	return touchingLadder;
    } // getTouchingLadder
    
    // sets touchingLadder
    public void setTouchingLadder (boolean touchingLadder) {
    	this.touchingLadder = touchingLadder;
    } // setTouchingLadder
    
    // sets startMovingOnPlat
    public boolean getStartMovingOnPlat () {
    	return startMovingOnPlat;
    } // getStartMovingOnPlat
    
    // gets startMovingOnPlat
    public void setStartMovingOnPlat (boolean startMovingOnPlat) {
    	this.startMovingOnPlat = startMovingOnPlat;
    } // setStartMovingOnPlat
    
    // figures out if barrel can go down a ladder
    public boolean canGoDown () {
    	if (lastLadder == null && ladder != null) {
    		lastLadder = ladder;
    	} // if
    	j++;
    	if (j == 1 && lastLadder != null) {
    		return true;
    	} else {
    		return false;
    	} // else
    } // canGoDown
    
    public void collidedWith(Entity other) {
    	int y = this.sprite.getHeight() - 1;
    	
    	// hits a platform
    	if (other instanceof PlatformEntity) {
    		this.setOnPlat(true);
    	} // if 
    	
    	if (other instanceof PlatformEntity && (!climbingDown) && (((this.getY() + y) > other.getY()) && ((this.getY() + y) < (other.getY() + other.sprite.getHeight())))) {
    		this.setY(other.getY() - y);
    	} // if
    	
    	// hits a player
    	if (other instanceof PlayerEntity) {
    		game.getHit(this);
    	} // else if
    	
    	// landing on a platform after going down a ladder
    	if ((startMovingOnPlat) && (this.getOnPlat())) {
    		this.setHorizontalMovement(-lastDX);
    		startMovingOnPlat = false;
    	} // if
    	
    	// is it touching a ladder?
    	if (other instanceof LadderEntity || other instanceof HalfLadderEntity) {
    		ladder = other;
    		this.touchingLadder = true;
    	} else {
    		ladder = null;
    		lastLadder = null;
    		j = 0;
    		this.touchingLadder = false;
    	} // else
    	
    	// hits a ladder
    	if ((other instanceof LadderEntity || other instanceof HalfLadderEntity) && (this.getY() <= (other.getY() + 20)) && ((this.getX() >= (other.getX() - 8)) && ((this.getX() + 7) <= other.getX())) && (!this.climbingDown) && (canGoDown())) {
    		int i = (int)(Math.random() * 2);
    		if (i == 0) {
    			this.climbingTimer = System.currentTimeMillis();
    			this.lastDX = dx;
    			this.setHorizontalMovement(0);
    			this.setVerticalMovement(100);
    			this.climbingDown = true;
    		} // if
    	} // if
	} // collidedWith
} // BarrelEntity
