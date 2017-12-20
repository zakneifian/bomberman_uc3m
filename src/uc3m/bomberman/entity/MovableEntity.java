package uc3m.bomberman.entity;

import uc3m.bomberman.map.*;
/**
 * This class extends the <code>{@link Entity}</code> and holds the methods
 * neccesary for every movable entity such as the enemies and the player.
 *
 */
public abstract class MovableEntity extends Entity{

	protected int speed = 1;

	private int spritePhase = 1;
	private String[] entityDir = new String[] {"down", "down"}; //previous movement, present movement
	private int spriteRgQty; //how much sprites does an entity have for certain movements
	
	/**
	 * Full constructor
	 * @param id
	 * @param path
	 * @param maxHp
	 * @param position
	 * @param spriteRgQty
	 */
	public MovableEntity(int id, String path, int maxHp, Coordinates position, int spriteRgQty) {
		super(id, path, maxHp, position);
		this.spriteRgQty = spriteRgQty;
	}
	
	/**
	 * This methods determines what this entity should do in case on collision with another entity
	 * @param col The <code>{@link Entity}</code> with which this entity has collided
	 */
	public abstract void onCollision(Entity col);
	
	/**
	 * Animates the movement of the <code>{@link MovableEntity}</code> when actually moving towards
	 * certain direction
	 * @param spritePhase
	 * @param entityDir
	 */
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
				setSprite(getSprite().substring(0, getSprite().length() - 6) + "01.png");
			}
			else if (entityDir[1].equals("left"))  {
				setSprite(getSprite().substring(0, getSprite().length() - 6) + "21.png");
			}
			else if (entityDir[1].equals("down"))  {
				setSprite(getSprite().substring(0, getSprite().length() - 6) + "11.png");
			}
			else if (entityDir[1].equals("right")) {
				setSprite(getSprite().substring(0, getSprite().length() - 6) + "31.png");
			}
		}
		
	}

	/**
	 * Moves the <code>{@link MovableEntity}</code> towards a certain direction
	 * @param dir
	 * @param map
	 */
	public void moveTowards(Direction dir, Map map){
		switch(dir){
		case UP:
			if((this.position.y-=speed) < 0) this.position.y = 0;
			break;
		case DOWN:
			this.position.y+=speed;
			break;
		case LEFT:
			if((this.position.x-=speed) < 0) this.position.x = 0;
			break;
		case RIGHT:
			this.position.x+=speed;
			break;
		}
	}
	
	/**
	 * @return the speed of the movable entity. This regards to the distance that it is able to travel
	 * in a certain amount of time
	 */
	public int getSpeed() {
		return speed;
	}
	/**
	 * @return the spritePhase
	 */
	public int getSpritePhase() {
		return spritePhase;
	}
	/**
	 * Sets the actual sprite phase, must be lower or equal to the SpriteRgQty
	 * @param spritePhase
	 */
	public void setSpritePhase(int spritePhase) {
		this.spritePhase = spritePhase;
	}
	/**
	 * @return a String[] array consisted of the previous direction and the present one (can be the same)
	 */
	public String[] getEntityDir() {
		return entityDir;
	}
	/**
	 * Sets the new direction of a movable entity, still remembering the previous direction
	 * @param entityDir
	 */
	public void setEntityDir(String entityDir) {
		this.entityDir[0] = this.entityDir[1];
		this.entityDir[1] = entityDir;
	}
	/**
	 * @return the range length of sprites for certain entity; related to movement
	 */
	public int getSpriteRgQty() {
		return spriteRgQty;
	}
	/**
	 * Sets the range length of sprites for certain entity; related to movement
	 * @param spriteRgQty
	 */
	public void setSpriteRgQty(int spriteRgQty) {
		this.spriteRgQty = spriteRgQty;
	}
}