package uc3m.bomberman.entity;

import uc3m.bomberman.main.Game;
import uc3m.bomberman.map.Coordinates;
/**
 * This class extends the <code>{@link MovableEntity}</code> and has the methods
 * concerning to the enemies, to be able to move, collision, damage the player, etc
 *
 */
public abstract class Enemy extends MovableEntity{
	int damagetoPlayer;
	/**
	 * Full constructor
	 * @param id
	 * @param path
	 * @param maxHp
	 * @param position
	 * @param spriteRgQty
	 * @param damageToPlayer
	 */
	public Enemy(int id, String path, int maxHp, Coordinates position, int spriteRgQty, int damageToPlayer) {
		super(id, path, maxHp, position, spriteRgQty);
		this.damagetoPlayer = damageToPlayer;
	}
	/**
	 * @return the damage inflicted to the player by the <code>{@link Enemy}</code>
	 */
	public int getDamagetoPlayer() {
		return damagetoPlayer;
	}
	
	/* (non-Javadoc)
	 * @see uc3m.bomberman.entity.MovableEntity#onCollision(uc3m.bomberman.entity.Entity)
	 */
	@Override
	public void onCollision(Entity col) {
		if(col instanceof Player && alive){
			col.takeDamage(this.getDamagetoPlayer());
		}		
	}
	
	/**
	 * Handles the <code>{@link Enemy}</code> movement, implemented on each specific enemy
	 * for their particular ways of moving
	 * @param game
	 */
	public abstract void moveEnemy(Game game);
	/**
	 * @return the score given to the player when the <code>{@link Enemy}</code> dies
	 */
	public abstract int getScoreOnDeath();
	/* (non-Javadoc)
	 * @see uc3m.bomberman.entity.MovableEntity#animateMovement(int, java.lang.String[])
	 */
	@Override
	public void animateMovement(int spritePhase, String[] entityDir) {
		if (entityDir[1].equals(entityDir[0])) { //if the present movement equals the past one
			if (getSpritePhase() < getSpriteRgQty()) { //if the phase is lower than its range limit of movement
				setSpritePhase(getSpritePhase() + 1);  // sum one to the phase 
				setSprite(getSprite().substring(0, getSprite().length() - 5) + getSpritePhase()  + ".png"); // and change the sprite summing one to the last digit pf the path
			}
			else {
				setSpritePhase(1); //else, set the sprite phase to 0 and change the sprite to the first one, the 0
				setSprite(getSprite().substring(0, getSprite().length() - 5) + "1.png");
			}
		}
		else {
			setSpritePhase(1);
			if	    (entityDir[1].equals("up"))    {
				setSprite(getSprite().substring(0, getSprite().length() - 5) + "1.png");
			}
			else if (entityDir[1].equals("left"))  {
				setSprite(getSprite().substring(0, getSprite().length() - 6) + "11.png");
			}
			else if (entityDir[1].equals("down"))  {
				setSprite(getSprite().substring(0, getSprite().length() - 5) + "1.png");
			}
			else if (entityDir[1].equals("right")) {
				setSprite(getSprite().substring(0, getSprite().length() - 6) + "21.png");
			}
		}
		
	}
	
}
