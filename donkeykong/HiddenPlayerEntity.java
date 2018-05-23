package donkeykong;

import java.util.ArrayList;

public class HiddenPlayerEntity extends Entity {
	private Game game;
	private ArrayList<Entity> barrels = new ArrayList<Entity>();

	public HiddenPlayerEntity(Game g, String r, int newX, int newY) {
		super(r, newX, newY); // calls the constructor in Entity
		game = g;
	} // constructor
	
	// sees if barrels contains a specific barrel
	public boolean containsBarrel (Entity other) {
		boolean contains = false;
		 
		for (int i = 0; i < barrels.size(); i++) {
			if (barrels.get(i) == other) {
				contains = true;
			} // if
		} // for
		
		return contains;
	} // containsBarrel

	public void collidedWith(Entity other) {
		if (other instanceof BarrelEntity && (!containsBarrel(other))) {
			game.addToScore(200);
			barrels.add(other);
		} // if
	} // collidedWith
} // HiddenPlayerEntity