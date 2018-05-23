package donkeykong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;

public class Game extends Canvas {

	private static final long serialVersionUID = 1L;
	private BufferStrategy strategy; // take advantage of accelerated graphics
	
	private ArrayList<Entity> entities = new ArrayList<Entity>(); // all entities
	private ArrayList<Entity> removeEntities = new ArrayList<Entity>(); // entities to remove
	private ArrayList<Entity> currentBarrel = new ArrayList<Entity>(); // barrels that are currently exploding
	private Entity player; // player
	private Entity saber; // saber
	private Entity barrel; // barrel
	private Entity ladder; // ladder
	private Entity life1; // life
	private Entity life2; // life
	private Entity life3; // life
	private Entity princess; // princess
	private Entity finalPlatform; // final platform
	private Entity enemy; // bad guy
	private Entity hiddenPlayer; // invisible player entity, used to see if the player jumps over a barrel
	private String message = ""; // message to display while waiting for a key press
	private String lastKey = "L"; // last key the user pressed
	private String currentSlash = "up"; // keeps track of the slashing sprites
	private boolean waitingForKeyPress = true; // true if game held up until a key is pressed
	private boolean leftPressed = false; // true if left arrow key currently  pressed
	private boolean rightPressed = false; // true if right arrow key currently pressed
	private boolean upPressed = false; // true if left arrow key currently pressed
	private boolean downPressed = false; // true if down arrow key currently pressed
	private boolean jumpPressed = false; // true if jumping
	private boolean gameRunning = true; // true if the game is running
	private boolean logicRequiredThisLoop = false; // is logic needed
	private boolean jumping = false; // is player jumping?
	private boolean holdingSaber = false; // is player holding saber?
	private boolean dead = false; // is player dead?
	private boolean startBarrelTimer = false; // should barrel timer be started?
	private boolean touchingLadder = false; // is player touching a ladder?
	private boolean climbing = false; // is player climbing?
	private boolean harmful = true; // can player be harmed
	private boolean stopBarrel = false; // should the barrels be stopped?
	private boolean beatLevel = false;
	private int platX = 0; // used to initialize platforms
	private int platY = 0; // used to initialize platforms
	private int saberTime = 7000; // duration of a saber
	private int platCount = 74; // number of normal platforms
	private int lives = 3; // # of lives
	private int score = 0; // score
	private int initFallY = 0; // player's initial Y position when a fall starts
	private int randBarrelTime = 0; // time until next barrel spawns
	private int toAdd = 0; // score that will be added
	private int bonus = 5000; // bonus score
	private int level = 1;
	private int removedBarrelCount = 0; // # of barrels that have reached the bottom
	private double moveSpeed = 100; // hor. vel. of player (px/s)
	private double a = .85; // gravity
	private double jumpV = 325; // initial jumping velocity
	private long saberStart = 0; // time the saber was picked up
	private long barrelSpawnTime = 0; // time the last barrel spawned
	private long barrelExplosionTime = 0; // time a barrel has been exploding for
	

	public Game() {
		// create a frame to contain game
		JFrame container = new JFrame("Commodore 64 Donkey Kong");

		// get hold the content of the frame
		JPanel panel = (JPanel) container.getContentPane();

		// set up the resolution of the game
		panel.setPreferredSize(new Dimension(800, 600));
		panel.setLayout(null);

		// set up canvas size (this) and add to frame
		setBounds(0, 0, 800, 600);
		panel.add(this);

		// Tell AWT not to bother repainting canvas since that will
		// be done using graphics acceleration
		setIgnoreRepaint(true);

		// make the window visible
		container.pack();
		container.setResizable(false);
		container.setVisible(true);

		// if user closes window, shutdown game and jre
		container.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			} // windowClosing
		});

		// add key listener to this canvas
		addKeyListener(new KeyInputHandler());

		// request focus so key events are handled by this canvas
		requestFocus();

		// create buffer strategy to take advantage of accelerated graphics
		createBufferStrategy(2);
		strategy = getBufferStrategy();

		// initialize entities
		initEntities();

		// start the game
		gameLoop();
	} // constructor

	// creates all entities
	private void initEntities() {
		try {
			if (level == 1) {
				saber = new SaberEntity(this, "sprites/saber.png", 685, 320);
				entities.add(saber);
				saber = new SaberEntity(this, "sprites/saber.png", 100, 192);
				entities.add(saber);
			} // if
			
			ladder = new LadderEntity(this, "sprites/ladder.png", 545, 432);
			entities.add(ladder);
			ladder = new LadderEntity(this, "sprites/ladder.png", 162, 310);
			entities.add(ladder);
			ladder = new HalfLadderEntity(this, "sprites/halfladder.png", 400, 297);
			entities.add(ladder);
			ladder = new LadderEntity(this, "sprites/ladder.png", 662, 185);
			entities.add(ladder);
			ladder = new LadderEntity(this, "sprites/ladder.png", 422, 70);
			entities.add(ladder);
			ladder = new HalfLadderEntity(this, "sprites/halfladder.png", 260, 170);
			entities.add(ladder);

			platX = 0;
			platY = 570;
			
			// create the rows of platforms
			for (int i = 0; i < platCount; i++) {
				if (level == 1) {
					PlatformEntity newPlat = new PlatformEntity(this, "sprites/platform.jpg", platX, platY);
					entities.add(newPlat);
				} else if (level == 2) {
					PlatformEntity newPlat = new PlatformEntity(this, "sprites/platformtwo.jpg", platX, platY);
					entities.add(newPlat);
				} // else if
				
				if (i < 19) {
					platX += 40;
					if (i > 3) {
						platY -= 2;
					} // if
				} else if (i < 37) {
					if (i == 19) {
						platY -= 90;
						platX -= entities.get(i).sprite.getWidth();
					} // if
					platX -= entities.get(i).sprite.getWidth();
					platY -= 2;
				} else if (i < 55) {
					if (i == 37) {
						platY -= 90;
						platX += entities.get(i).sprite.getWidth();
					} // if
					platX += entities.get(i).sprite.getWidth();
					platY -= 2;
				} else if (i < 74) {
					if (i == 55) {
						platY -= 90;
						platX -= entities.get(i).sprite.getWidth();
					} // if
					platX -= entities.get(i).sprite.getWidth();
					platY -= 2;
				} // else if
			} // for i
			
			finalPlatform = new FinalPlatformEntity(this, "sprites/finalplatform.png", 280, 80);
			entities.add(finalPlatform);
			
			player = new PlayerEntity(this, "sprites/idleleft.png", 30, 531);
			entities.add(player);
			
			princess = new PrincessEntity(this, "sprites/princess.png", 300, 42);
			entities.add(princess);
			
			enemy = new EnemyEntity(this, "sprites/enemy.gif", 160, 50);
			entities.add(enemy);
			
			printLives();
		} catch (Exception e) {
			e.printStackTrace();
		} // catch
	} // initEntities
	
	public int getLevel() {
		return level;
	} // getLevel

	// remove entity
	public void removeEntity(Entity entity) {
		removeEntities.add(entity);
	} // removeEntity

	// show the death screen and restart the game
	public void notifyLoss() {
		message = ("You lost... Your score was " + score + ". Try again?");
		waitingForKeyPress = true;
		dead = false;
		score = 0;
		level = 1;
		startGame();
	} // notifyDeath

	// show the win screen and restart the game
	public void notifyWin() {
		player.changeSprite("sprites/winsprite.png");
		player.setX(405);
		player.setY(32);	
		
		if (level == 1) {
			score += bonus;
			message = "You beat level 1! Your current score is " + score + ".";
			level ++;			
		} else if (level == 2) {
			score += bonus;
			message = "Congratulations, you win! Your score was " + score + "." + "\n Press any key to play again.";
			level = 1;
			score = 0;
		} // else if
		bonus = 0;
	
		waitingForKeyPress = true;
	} // notifyWin

	// spawn a barrel
	public void spawnBarrel() {
		barrel = new BarrelEntity(this, "sprites/barrel.png", 215, 90);
		entities.add(barrel);
	} // spawnBarrel

	// stop the player's jump
	public void stopJump() {
		jumping = false;
		player.setVerticalMovement(0);

		if (lastKey.equals("L") && (!holdingSaber) && (!dead)) {
			player.changeSprite("sprites/idleleft.png");
		} else if (lastKey.equals("R") && (!holdingSaber) && (!dead)) {
			player.changeSprite("sprites/idleright.png");
		} // else if
		
		entities.remove(hiddenPlayer);
		entities.trimToSize();
		score += toAdd;
		toAdd = 0;
	} // stopJump

	// returns jumping
	public boolean getJumping() {
		return jumping;
	} // getJumping

	// activates the saber item
	public void getSaber() {
		holdingSaber = true;
		saberStart = System.currentTimeMillis();
	} // getSaber

	// makes a barrel explode, happens when it is killed by the saber
	public void barrelExplosion(Entity barrel) {
		startBarrelTimer = true;
		currentBarrel.add(barrel);
		barrel.setVerticalMovement(0);
		barrel.setHorizontalMovement(0);
		barrel.changeSprite("sprites/explode.png");
		((BarrelEntity) barrel).setExploding(true);
	} // barrelExplosion

	// makes an entity fall using gravity
	public void fall(Entity e, long delta) {
		double v = e.getDY();
		v += (a * ((double) delta));
		e.setVerticalMovement(v);
	} // fall
	
	// sets touchingLadder
	public void setTouchingLadder(boolean touchingLadder, LadderEntity ladder) {
		this.touchingLadder = touchingLadder;
		this.ladder = ladder;
	} // setClimb
	
	// gets touchingLadder
	public boolean getTouchingLadder() {
		return touchingLadder;
	} // getTouchingLadder
	
	// gets climbing
	public boolean getClimbing () {
		return climbing;
	} // getClimbing

	// handles getting hit by something you can kill
	public void getHit(Entity e) {
		if (e instanceof BarrelEntity) {
			if (holdingSaber && ((BarrelEntity) e).getExploding() == false) {
				barrelExplosion(e);
				score += 500;
			} else if ((!holdingSaber) && ((BarrelEntity) e).getExploding() == false) {
				entities.remove(life1);
				entities.remove(life2);
				entities.remove(life3);
				entities.remove(e);
				entities.trimToSize();
				lives--;
				dead = true;

				if (lives == 0) {
					notifyLoss();
					lives = 3;
				} else {
					if (lastKey.equals("L")) {
						die("L");
					} else if (lastKey.equals("R")) {
						die("R");
					} // else if

					message = ("You died...");
					waitingForKeyPress = true;
				} // else
			} // else if
		} else if (e instanceof FireballEntity) {
			if (holdingSaber) {
				score += 500;
				entities.remove(e);
				entities.trimToSize();
			} else {
				entities.remove(life1);
				entities.remove(life2);
				entities.remove(life3);
				entities.remove(e);
				entities.trimToSize();
				lives--;
				dead = true;

				if (lives == 0) {
					notifyLoss();
					lives = 3;
				} else {
					if (lastKey.equals("L")) {
						die("L");
					} else if (lastKey.equals("R")) {
						die("R");
					} // else if

					message = ("You died...");
					waitingForKeyPress = true;
				} // else
			} // else
		} // else if
	} // getHit

	// changes sprite to the dying sprite
	public void die(String direction) {
		player.setVerticalMovement(0);
		jumping = false;
		touchingLadder = false;
		climbing = false;
		player.onPlat = true;
		jumpPressed = false;
		if (direction.equals("L")) {
			player.changeSprite("sprites/dieright.png");
		} else if (direction.equals("R")) {
			player.changeSprite("sprites/dieleft.png");
		} // else if
	} // die
	
	// handles hitting an enemy
	public void hitEnemy () {
		if (harmful == true) {

			entities.remove(life1);
			entities.remove(life2);
			entities.remove(life3);
			entities.trimToSize();
			lives--;
			dead = true;

			if (lives == 0) {
				notifyLoss();
				lives = 3;
			} else {
				if (lastKey.equals("L")) {
					die("L");
				} else if (lastKey.equals("R")) {
					die("R");
				} // else if
				
				message = ("You died..." + "Your score is " + score);
				waitingForKeyPress = true;
			} // else
		} // if
		
		harmful = false;
	} // hitEnemy
	
	public void hitObstacle (Entity e) {
		entities.remove(life1);
		entities.remove(life2);
		entities.remove(life3);
		entities.remove(e);
		entities.trimToSize();
		lives--;
		dead = true;

		if (lives == 0) {
			notifyLoss();
			lives = 3;
		} else {
			if (lastKey.equals("L")) {
				die("L");
			} else if (lastKey.equals("R")) {
				die("R");
			} // else if

			message = ("You died...");
			waitingForKeyPress = true;
		} // else
	} // hitFireball

	// displays lives
	public void printLives() {
		if (lives == 3) {
			life1 = new LivesEntity(this, "sprites/life.png", 105, 10);
			entities.add(life1);

			life2 = new LivesEntity(this, "sprites/life.png", 125, 10);
			entities.add(life2);

			life3 = new LivesEntity(this, "sprites/life.png", 145, 10);
			entities.add(life3);
		} else if (lives == 2) {
			life1 = new LivesEntity(this, "sprites/life.png", 105, 10);
			entities.add(life1);

			life2 = new LivesEntity(this, "sprites/life.png", 125, 10);
			entities.add(life2);
		} else if (lives == 1) {
			life1 = new LivesEntity(this, "sprites/life.png", 105, 10);
			entities.add(life1);
		} // else if
	} // printLives
	
	// sets platY
	public void setPlatY (int platY) {
		this.platY = platY;
	} // setPlatY
	
	// figures out if player can climb
	public boolean canClimb () {
		if ((touchingLadder) && ((player.getX()) >= (ladder.getX() - 10)) && ((player.getX() + 2) <= ladder.getX())) {
			return true;
		} else {
			return false;
		} // else
	} // canClimb
	
	// figures out if player can climb up
	public boolean canClimbUp () {
		if ((touchingLadder) && (player.getY() > ladder.getY())) {
			return true;
		} else {
			return false;
		} // else
	} // canClimbUp
	
	// figures out if player can climb down
	public boolean canClimbDown () {
		if ((touchingLadder) && (player.getY() < ladder.getY())) {
			return true;
		} else {
			return false;
		} // else
	} // canClimbDown
	
	// increases the amount to be added to score
	public void addToScore (int x) {
		toAdd += x;
	} // addToScore
	
	// spawns a fireball
	public void spawnFireball () {
		Entity fireball = new FireballEntity(this, "sprites/fireball.png", 0, 540);
		entities.add(fireball);
	} // spawnFireball
	
	// gets removedBarrelCount
	public int getRemovedBarrelCount () {
		return removedBarrelCount;
	} // getRemovedBarrelCount
	
	// sets removedBarrelCount
	public void setRemovedBarrelCount (int removedBarrelCount) {
		this.removedBarrelCount = removedBarrelCount;
	} // setRemovedBarrelCount

	// moves entities, checks logic; brainpower of the game
	public void gameLoop() {
		long lastLoopTime = System.currentTimeMillis();
		
		printLives();
		int bonusTimer = 0;
		
		// keep loop running until game ends
		while (gameRunning) {
			if (!waitingForKeyPress) {
				bonusTimer ++;
			} // if
			
			if (bonusTimer > 200 && bonus > 0) {
				bonus -= 100;
				bonusTimer = 0;
			} // if
			
			// calc. time since last update, will be used to calculate entities
			// movement
			long delta = System.currentTimeMillis() - lastLoopTime;
			lastLoopTime = System.currentTimeMillis();

			barrelSpawnTime += delta;
			if (startBarrelTimer == true) {
				barrelExplosionTime += delta;
			} // if
			
			// WIN THE LEVEL
			if (player.getY() < 60 && beatLevel == false) {
				notifyWin();
				beatLevel = true;
			} // if
			
			// get graphics context for the accelerated surface and make it
			// black
			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			
			g.setColor(Color.black);
			g.fillRect(0, 0, 800, 600);
			
			g.setColor(Color.white);
			g.setFont(new Font("Courier New", Font.BOLD, 24));
			g.drawString("Lives: ", 10, 26);
			String scoreOutput = "Score: " + score;
			g.drawString(scoreOutput, 7, 50);
			String bonusOutput = "Bonus: " + bonus;
			g.drawString(bonusOutput, 7, 74);
			
			g.setColor(Color.RED);
			g.setFont(new Font ("Courier New", Font.BOLD, 18));
			if (stopBarrel) {
				g.drawString("Stopped Barrels", 580, 26);
			}
			
			// move each entity
			if (!waitingForKeyPress) {
				for (int i = 0; i < entities.size(); i++) {
					Entity entity = (Entity) entities.get(i);
					entity.move(delta);
				} // for
			} // if

			// draw all entities
			for (int i = 0; i < entities.size(); i++) {
				Entity entity = (Entity) entities.get(i);
				entity.draw(g);
				entity.setOnPlat(false);
			} // for
			
			touchingLadder = false;

			// brute force collisions, compare every entity
			// against every other entity. If any collisions
			// are detected notify both entities that it has
			// occurred
			for (int i = 0; i < entities.size(); i++) {
				Entity me = (Entity) entities.get(i);
				for (int j = i + 1; j < entities.size(); j++) {
					Entity him = null;
					if (entities.get(j) != null) {
						him = (Entity) entities.get(j);
					} // if
					
					if(him == null) continue;
					try {
						if (me.collidesWith(him)) {
							me.collidedWith(him);
							him.collidedWith(me);
						} // if
					} catch (Exception e) {
						e.printStackTrace();
					} // catch
				} // inner for
			} // outer for

			// remove dead entities
			entities.removeAll(removeEntities);
			removeEntities.clear();

			// run logic if required
			if (logicRequiredThisLoop) {
				for (int i = 0; i < entities.size(); i++) {
					Entity entity = (Entity) entities.get(i);
					entity.doLogic();
				} // for
				logicRequiredThisLoop = false;
			} // if

			// if waiting for "any key press", draw message
			if (waitingForKeyPress) {
				printLives();
				
				g.setColor(Color.white);
				g.setFont(new Font("Arial", Font.PLAIN, 12));
				g.drawString(message,
							(800 - g.getFontMetrics().stringWidth(message)) / 2,
						250);
				g.drawString("Press any key", (800 - g.getFontMetrics()
						.stringWidth("Press any key")) / 2, 300);
			} // if

			// clear graphics and flip buffer
			g.dispose();
			strategy.show();

			// player doesn't move without input
			if (!jumping) {
				player.setHorizontalMovement(0);
			} // if
			
			// respond to user moving player
			if ((leftPressed) && (!rightPressed) && (!jumping) && (!dead) && (!climbing)) {
				if (!holdingSaber) {
					player.changeSprite("sprites/idleright.png");
				} // if
				player.setHorizontalMovement(-moveSpeed);
			} else if ((rightPressed) && (!leftPressed) && (!jumping) && (!dead) && (!climbing)) {
				if (!holdingSaber) {
					player.changeSprite("sprites/idleleft.png");
				} // if
				player.setHorizontalMovement(moveSpeed);
			} // else
			
			// climbs a ladder
			if ((upPressed) && (!downPressed) && (!jumping) && (!holdingSaber) && (canClimb()) && (!dead)) {
				if (climbing) {
					player.changeSprite("sprites/climbing.png");
					player.setVerticalMovement(-moveSpeed);
				} else if (canClimbUp()) {
					player.changeSprite("sprites/climbing.png");
					player.setVerticalMovement(-moveSpeed);
					climbing = true;
				} // else if
			} else if ((downPressed) && (!upPressed) && (!jumping) && (!holdingSaber) && (canClimb()) && (!dead)) {
				if (climbing) {
					player.changeSprite("sprites/climbing.png");
					player.setVerticalMovement(moveSpeed);
				} else if (canClimbDown()) {
					player.changeSprite("sprites/climbing.png");
					player.setVerticalMovement(moveSpeed);
					climbing = true;
				} // else if
			} else if ((!upPressed) && (!downPressed) && (!jumping) && (climbing) && (!holdingSaber)) {
				player.setVerticalMovement(0);
			} // else if

			// if spacebar pressed, try to jump
			if ((jumpPressed) && (!jumping) && (!climbing) && (!holdingSaber)) {
				jumping = true;

				player.setVerticalMovement(-jumpV);
				if ((leftPressed) && (!rightPressed)) {
					player.setHorizontalMovement(-moveSpeed);
				} else if ((!leftPressed) && (rightPressed)) {
					player.setHorizontalMovement(moveSpeed);
				} // if

				if ((lastKey.equals("L")) && (!dead)) {
					player.changeSprite("sprites/jumpleft.gif");
				} else if (lastKey.equals("R") && (!dead)) {
					player.changeSprite("sprites/jumpright.gif");
				} // if
				
				hiddenPlayer = new HiddenPlayerEntity(this, "sprites/hiddenplayer.png", player.getX(), player.getY());
				entities.add(hiddenPlayer);
				hiddenPlayer.setHorizontalMovement(player.getDX());
				hiddenPlayer.setVerticalMovement(0);
			} // if

			// makes the jump happen
			if (jumping) {
				fall(player, delta);
			} // if

			// handles holding the saber
			if (holdingSaber) {
				if (((lastLoopTime - saberStart) % 200) <= 10) {
					if (currentSlash.equals("up")) {
						if (lastKey.equals("L")) {
							player.changeSprite("sprites/upslashright.png");
						} else if (lastKey.equals("R")) {
							player.changeSprite("sprites/upslashleft.png");
						} // else if

						currentSlash = "down";
					} else if (currentSlash.equals("down")) {
						if (lastKey.equals("L")) {
							player.changeSprite("sprites/downslashright.png");
						} else if (lastKey.equals("R")) {
							player.changeSprite("sprites/downslashleft.png");
						} // else if
						currentSlash = "up";
					} // else if
				} // if
				if (lastLoopTime - saberStart >= saberTime) {
					holdingSaber = false;
				} // if
			} else if ((!holdingSaber) && (!jumping) && (!dead) && (!climbing)) {
				if (lastKey.equals("L")) {
					player.changeSprite("sprites/idleleft.png");
				} else if (lastKey.equals("R")) {
					player.changeSprite("sprites/idleright.png");
				} // else if
			} // else if

			// removes the barrel
			if (barrelExplosionTime >= 500) {
				entities.remove(currentBarrel.get(0));
				entities.trimToSize();
				currentBarrel.remove(0);
				if (currentBarrel.size() == 0) {
					startBarrelTimer = false;
				} // if
				barrelExplosionTime = 0;
			} // if

			// makes the player fall if they aren't on a platform
			if ((!player.getOnPlat()) && (!jumping) && (!climbing)) {
				fall(player, delta);
				if (initFallY == 0) {
					initFallY = player.getY();
				} // if
			} else if ((player.getOnPlat()) && (!jumping) && (!climbing) && ((player.getY() < initFallY + 20) || (player.getY() > (initFallY + 60)))) {
				player.setVerticalMovement(0);
				initFallY = 0;
			} // else if
			
			// stops climbing
			if (!touchingLadder) {
				climbing = false;
			} else if ((touchingLadder) && (player.getOnPlat()) && ((player.getY() + 40) <= platY)) {
				climbing = false;
			} // else if

			// moves barrels
			for (int i = 0; i < entities.size(); i++) {
				if (entities.get(i) instanceof BarrelEntity) {
					BarrelEntity barrel = (BarrelEntity) entities.get(i);
					if ((!barrel.getOnPlat()) && (!barrel.getExploding())) {
						fall(barrel, delta);
					} else if ((barrel.getOnPlat()) && (!barrel.getExploding()) && (!barrel.getClimbingDown())) {
						barrel.setVerticalMovement(0);
					} else if (((System.currentTimeMillis() - barrel.getClimbingTimer()) >= 375) && (barrel.getOnPlat()) && (!barrel.getExploding())) {
						barrel.setClimbingDown(false);
						barrel.setClimbingTimer(0);
						barrel.setStartMovingOnPlat(true);
					} else if (barrel.getClimbingDown() && (!barrel.getExploding())) {
						fall(barrel, delta / 2);
					} // else if
				} // if
			} // for i
			
			// moves fireballs
			for (int i = 0; i < entities.size(); i++) {
				if (entities.get(i) instanceof FireballEntity) {
					FireballEntity fireball = (FireballEntity) entities.get(i);
					System.out.println();
					if (!fireball.getOnPlat() && (!fireball.getClimbing())) {
						fall(fireball, delta);
					} else if ((fireball.getOnPlat()) && (!fireball.getClimbing())) {
						fireball.setVerticalMovement(0);
					} else if (((System.currentTimeMillis() - fireball.getClimbingTimer()) >= 600) && (fireball.getOnPlat()) && (fireball.getDY() > 0)) {
						fireball.setClimbing(false);
						fireball.setStartMovingOnPlat(true);
					} else if (((System.currentTimeMillis() - fireball.getClimbingTimer()) >= 290) && (fireball.getOnPlat()) && ((fireball.getY()) <= (fireball.getCurrentLadder().getY() - 17)) && (fireball.getDY() < 0)) {
						fireball.setClimbing(false);
						fireball.setStartMovingOnPlat(true);
					} // else if 
				} // if
			} // for i 

			// spawns barrels
			if (barrelSpawnTime >= randBarrelTime && waitingForKeyPress == false) {
				if (!stopBarrel) {
					spawnBarrel();
				} // if
				barrelSpawnTime = 0;
				if (level == 1) {
					randBarrelTime = (int)(Math.random() * 2500) + 2000;
				} else if (level == 2) {
					randBarrelTime = (int)(Math.random() * 2500) + 1000;
				} // else if
			} // if

			// pause
			try {
				Thread.sleep(10);
			} catch (Exception e) {
			} // catch

		} // while

	} // gameLoop

	/*
	 * startGame input: none output: none purpose: start a fresh game, clear old
	 * data
	 */
	private void startGame() {
		// clear out any existing entities and initalize a new set
		entities.clear();

		lastKey = "L";
		initEntities();

		// blank out any keyboard settings that might exist
		holdingSaber = false;
		touchingLadder = false;
		leftPressed = false;
		rightPressed = false;
		upPressed = false;
		downPressed = false;
		jumpPressed = false;
		dead = false;
		player.onPlat = true;
		jumping = false;
		climbing = false;
		harmful = true;
		beatLevel = false;
		toAdd = 0;
		bonus = 5000;
		removedBarrelCount = 0;
	} // startGame

	private class KeyInputHandler extends KeyAdapter {
		private int pressCount = 1; // the number of key presses since waiting for 'any' key press

		public void keyPressed(KeyEvent e) {

			// if waiting for keypress to start game, do nothing
			if (waitingForKeyPress) {
				return;
			} // if

			// respond to move left, right or jump
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftPressed = true;
				lastKey = "R";
			} // if

			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightPressed = true;
				lastKey = "L";
			} // if

			if (e.getKeyCode() == KeyEvent.VK_UP) {
				upPressed = true;
			} // if

			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				downPressed = true;
			} // if

			if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyChar() == ' ') {
				jumpPressed = true;
			} // if
			
			if (e.getKeyCode() == KeyEvent.VK_D) {
				if (!stopBarrel) {
					stopBarrel = true;
				} else if (stopBarrel) {
					stopBarrel = false;
				} // else if
			} // if
			
		} // keyPressed

		public void keyReleased(KeyEvent e) {
			// if waiting for keypress to start game, do nothing
			if (waitingForKeyPress) {
				return;
			} // if

			// respond to move left, right or fire
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftPressed = false;
				if (rightPressed == true) {
					lastKey = "L";
				} // if
			} // if

			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightPressed = false;
				if (leftPressed == true) {
					lastKey = "R";
				} // if
			} // if

			if (e.getKeyCode() == KeyEvent.VK_UP) {
				upPressed = false;
			} // if

			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				downPressed = false;
			} // if

			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				jumpPressed = false;
			} // if
		} // keyReleased

		public void keyTyped(KeyEvent e) {

			// if waiting for key press to start game
			if (waitingForKeyPress) {
				if (pressCount == 1) {
					waitingForKeyPress = false;
					startGame();
					pressCount = 0;
				} else {
					pressCount++;
				} // else
			} // if waitingForKeyPress

			// if escape is pressed, end game
			if (e.getKeyChar() == 27) {
				System.exit(0);
			} // if escape pressed
		} // keyTyped

	} // class KeyInputHandler

	public static void main(String[] args) {
		// instantiate this object
		new Game();
	} // main

	public void updateLogic() {
		logicRequiredThisLoop = true;
	} // updateLogic

} // Game
