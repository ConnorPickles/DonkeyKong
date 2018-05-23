package donkeykong;

import java.util.ArrayList;

public class FireballEntity extends Entity {
	private Game game;
	private ArrayList<Entity> climbed = new ArrayList<Entity>(); // ladders climbed
    private Entity ladder; // ladder
    private Entity lastLadder; // previous ladder
    private Entity currentLadder; // ladder fireball is touching
    private boolean climbing = false; // is fireball climbing?
    private boolean touchingLadder = false; // is fireball touching a ladder?
    private boolean startMovingOnPlat = false; // should fireball start moving next time it hits a platform?
    private int j = 0; // #, used for calculations
    private double lastDX = 0; // dx before the climb
    private long climbingTimer = 0; // when the fireball started climbing

	public FireballEntity(Game g, String r, int newX, int newY) {
		super(r, newX, newY); // calls the constructor in Entity
		game = g;
		dx = 125;
		dy = 0;
	} // constructor
	
	public void move(long delta) {
		// stop at left side of screen
    	if ((dx < 0) && (x < 0)) {
    		this.setX(0);
			dx *= -1; 
		} // if

		// stop at right side of screen
		if ((dx > 0) && (x > (800 - this.sprite.getWidth()))) {
			this.setX(800 - this.sprite.getWidth());
			dx *= -1;
		} // if
		
		super.move(delta); // calls the move method in Entity
	} // move
	
	// gets climbing
	public boolean getClimbing () {
	    return climbing;
	} // getClimbing
	
	// sets climbing    
	public void setClimbing (boolean climbing) {
	    this.climbing = climbing;
	} // setClimbing
	
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
	
	// gets startMovingOnPlat
	public boolean getStartMovingOnPlat () {
    	return startMovingOnPlat;
    } // getStartMovingOnPlat
    
	// sets startMovingOnPlat
    public void setStartMovingOnPlat (boolean startMovingOnPlat) {
    	this.startMovingOnPlat = startMovingOnPlat;
    } // setStartMovingOnPlat
    
    // gets currentLadder
    public Entity getCurrentLadder () {
    	return currentLadder;
    } // getCurrentLadder
	 
    // figures out if the fireball can climb
	public boolean canClimb () {
	    if (lastLadder == null && ladder != null) {
	    	lastLadder = ladder;
	    } // if
	    j++;
	    if (j == 1 && lastLadder != null) {
	    	return true;
	    } else {
	    	return false;
	    } // else
	} // canClimb
	
	// sees if climbed contains a certain ladder
	public boolean containsLadder (Entity other) {
	    boolean contains = false;
			 
		for (int i = 0; i < climbed.size(); i++) {
			if (climbed.get(i) == other) {
				contains = true;
			} // if
		} // for
			
		return contains;
	} // containsLadder

    public void collidedWith(Entity other) {
		int y = this.sprite.getHeight() - 1;
		
		if ((System.currentTimeMillis() - climbingTimer > 2500) && (climbingTimer != 0)) {
			climbed.remove(0);
			climbingTimer = 0;
		} // if
		
		// hits a platform
    	if (other instanceof PlatformEntity) {
    		this.setOnPlat(true);
    	}  // if
    	
    	if (other instanceof PlayerEntity) {
    		game.hitObstacle(this);
    	} // if
    	
    	if (other instanceof PlatformEntity && (!climbing) && (((this.getY() + y) > other.getY()) && ((this.getY() + y) < (other.getY() + other.sprite.getHeight())))) {
    		this.setY(other.getY() - y);
    	} // if
    	
    	// landing on a platform after going down a ladder
    	if ((startMovingOnPlat) && (this.getOnPlat())) {
    		int i = (int)(Math.random() * 2);
    		if (i == 0) {
    		    this.setHorizontalMovement(-lastDX);
    		} else {
    			this.setHorizontalMovement(lastDX);
    		} // else
    		startMovingOnPlat = false;
    	} // if
    	
    	// is it touching a ladder?
    	if (other instanceof LadderEntity || other instanceof HalfLadderEntity) {
    		ladder = other;
    		currentLadder = other;
    		this.touchingLadder = true;
    	} else {
    		ladder = null;
    		lastLadder = null;
    		j = 0;
    		this.touchingLadder = false;
    	} // else
    	
    	// hits a ladder
    	if (other instanceof LadderEntity && ((this.getX() >= (other.getX() - 8)) && ((this.getX() + 7) <= other.getX())) && (!this.climbing) && (canClimb()) && (!this.containsLadder(other))) {
    		int i = (int)(Math.random() * 2);
    		if (i == 0) {
    			this.climbingTimer = System.currentTimeMillis();
			    this.lastDX = dx;
			    this.setHorizontalMovement(0);
			    this.climbing = true;
    			if ((this.getY() <= (other.getY() + 20))) {
    			    this.setVerticalMovement(100);
    			} else if (this.getY() > other.getY()) {
    			    this.setVerticalMovement(-100);
    			} // else if 
    			climbed.add(other);
    		} // if
    	} // if
    	
    	if (other instanceof PlayerEntity) {
    		game.getHit(this);
    	} // if
	} // collidedWith
	
} // ShipEntity class