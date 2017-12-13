package uc3m.bomberman.entity;

import uc3m.bomberman.map.*;

public abstract class MovableEntity extends Entity{

	protected int speed = 2;
	
	public MovableEntity(int id, String path, int maxHp, Coordinates position) {
		super(id, path, maxHp, position);
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
}