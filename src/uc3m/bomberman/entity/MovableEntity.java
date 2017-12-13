package uc3m.bomberman.entity;

import uc3m.bomberman.map.*;

public abstract class MovableEntity extends Entity{

	protected int speed = 2;

	private int spritePhase = 1;
	private String[] entityDir = new String[] {"down", "down"}; //previous movement, present movement
	private int spriteRgQty; //how much sprites does an entity have for certain movements
	
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
	 * Moves the entity's coordinates towards a direction
	 * @param dir
	 */

	public void animateMovement(int spritePhase, String[] entityDir) { //TODO generar sprites de los 4 movimientos para enemigos
		if (entityDir[1].equals(entityDir[0])) { //if the present movement equals the past one
			if (getSpritePhase() < getSpriteRgQty()) { //if the phase is lower than its range limit of movement
				setSpritePhase(getSpritePhase() + 1);  // sum one to the phase 
				System.out.println("--");
				System.out.println(getSprite()); // (Integer.parseInt(String.valueOf(getSprite().charAt(getSprite().length() - 5))) + 1)
				setSprite(getSprite().substring(0, getSprite().length() - 5) + getSpritePhase()  + ".png"); // and change the sprite summing one to the last digit pf the path
				System.out.println(getSprite());
			}
			else {
				setSpritePhase(1); //else, set the sprite phase to 0 and change the sprite to the first one, the 0
				System.out.println("Reiniciado");
				System.out.println(getSprite());
				setSprite(getSprite().substring(0, getSprite().length() - 5) + "1.png");
				System.out.println(getSprite());
			}
		}
		else {
			setSpritePhase(1);
			System.out.println("CHANGE DIRECTIONS");
			System.out.println(getSprite());
			if	    (entityDir[1].equals("up"))    {
				setSprite(getSprite().substring(0, getSprite().length() - 6) + "01.png");
				System.out.println(getSprite());
			}
			else if (entityDir[1].equals("left"))  {
				setSprite(getSprite().substring(0, getSprite().length() - 6) + "21.png");
				System.out.println(getSprite());
			}
			else if (entityDir[1].equals("down"))  {
				setSprite(getSprite().substring(0, getSprite().length() - 6) + "11.png");
				System.out.println(getSprite());
			}
			else if (entityDir[1].equals("right")) {
				setSprite(getSprite().substring(0, getSprite().length() - 6) + "31.png");
				System.out.println(getSprite());
			}
		}
		
	}
	
	public void moveTowards(Direction dir){
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
	public int getSpeed() {
		return speed;
	}
	public int getSpritePhase() {
		return spritePhase;
	}
	public void setSpritePhase(int spritePhase) {
		this.spritePhase = spritePhase;
	}
	public String[] getEntityDir() {
		return entityDir;
	}
	public void setEntityDir(String entityDir) {
		this.entityDir[0] = this.entityDir[1];
		this.entityDir[1] = entityDir;
	}
	public int getSpriteRgQty() {
		return spriteRgQty;
	}
	public void setSpriteRgQty(int spriteRgQty) {
		this.spriteRgQty = spriteRgQty;
	}
}